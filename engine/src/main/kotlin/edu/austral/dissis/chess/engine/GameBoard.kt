package edu.austral.dissis.chess.engine

data class Position(val row: Int, val col: Int) {
    override fun toString(): String {
        return getStringPosition(row, col)
    }
}

interface GameBoard {
    fun isOccupied(position: Position): Boolean

    fun getPieceAt(position: Position): Piece?

    fun setPieceAt(
        position: Position,
        piece: Piece,
    ): GameBoard

    fun delPieceAt(position: Position): GameBoard

    fun positionExists(position: Position): Boolean

    fun isPositionOnUpperLimit(position: Position): Boolean

    fun containsPieceOfPlayer(
        position: Position,
        player: Player,
    ): Boolean

    fun getAllPositions(): Iterable<Position>

    fun getAllPositionsOfPlayer(
        player: Player,
        includeKing: Boolean,
    ): Iterable<Position>

    fun getRowAsWhite(
        position: Position,
        player: Player,
    ): Int

    fun getKingPosition(player: Player): Position
}

class HashGameBoard private constructor(
    private val validator: PositionValidator,
    private val boardMap: Map<Position, Piece>,
    // TODO: may replace by something better. Maybe we can proxy HashGameBoard and add these positions as proxy methods
    // TODO: or actually, could simply inherit. This is the only case where inheritance might come in handy
    private val whiteKingPosition: Position,
    private val blackKingPosition: Position,
) : GameBoard {
    companion object {
        fun build(
            validator: PositionValidator,
            pieces: List<Pair<Position, Piece>>,
            whiteKingPosition: Position,
            blackKingPosition: Position,
        ): HashGameBoard {
            return HashGameBoard(validator, pieces.toMap(), whiteKingPosition, blackKingPosition)
        }
    }

    init {
        for (pair in listOf(Player.WHITE, Player.BLACK).zip(listOf(whiteKingPosition, blackKingPosition))) {
            val player = pair.first
            val position = pair.second

            val king = boardMap[position]
            require(king != null && king.rules is KingPieceRules && king.player == player) {
                "The $player king is not located at $king"
            }
        }
    }

    override fun isOccupied(position: Position): Boolean {
        return boardMap[position] != null
    }

    override fun getPieceAt(position: Position): Piece? {
        return boardMap[position]
    }

    override fun setPieceAt(
        position: Position,
        piece: Piece,
    ): HashGameBoard {
        val newMap = boardMap + (position to piece)

        var newWhiteKingPosition = whiteKingPosition
        var newBlackKingPosition = blackKingPosition

        if (piece.rules is KingPieceRules) {
            when (piece.player) {
                Player.WHITE -> newWhiteKingPosition = position
                Player.BLACK -> newBlackKingPosition = position
            }
        }

        return HashGameBoard(validator, newMap, newWhiteKingPosition, newBlackKingPosition)
    }

    override fun delPieceAt(position: Position): HashGameBoard {
        require(position != whiteKingPosition && position != blackKingPosition) {
            "A king cannot be deleted without being set in other position first"
        }

        val newMap: HashMap<Position, Piece> = HashMap(boardMap)
        newMap.remove(position)

        return HashGameBoard(validator, newMap, whiteKingPosition, blackKingPosition)
    }

    override fun positionExists(position: Position): Boolean {
        return validator.positionExists(position)
    }

    override fun isPositionOnUpperLimit(position: Position): Boolean {
        return validator.isPositionOnLastRow(position)
    }

    override fun containsPieceOfPlayer(
        position: Position,
        player: Player,
    ): Boolean {
        val piece = getPieceAt(position) ?: return false
        return piece.player == player
    }

    override fun getAllPositions(): Iterable<Position> {
        return boardMap.keys
    }

    override fun getAllPositionsOfPlayer(
        player: Player,
        includeKing: Boolean,
    ): Iterable<Position> {
        return boardMap.keys.filter {
            val position: Position = it
            val piece = boardMap[position]!!

            if (includeKing) {
                piece.player == player
            } else {
                piece.player == player && (piece.rules !is KingPieceRules)
            }
        }
    }

    override fun getRowAsWhite(
        position: Position,
        player: Player,
    ): Int {
        return validator.getRowAsWhite(position, player)
    }

    override fun getKingPosition(player: Player): Position {
        return when (player) {
            Player.WHITE -> whiteKingPosition
            Player.BLACK -> blackKingPosition
        }
    }
}

interface PositionValidator {
    fun positionExists(position: Position): Boolean

    fun getRowAsWhite(
        position: Position,
        player: Player,
    ): Int

    fun isPositionOnLastRow(position: Position): Boolean
}

class RectangleBoardValidator(val numberRows: Int, val numberCols: Int) : PositionValidator {
    override fun positionExists(position: Position): Boolean {
        val (row, col) = position
        return (0 < row) && (row <= numberRows) &&
            (0 < col) && (col <= numberCols)
    }

    override fun getRowAsWhite(
        position: Position,
        player: Player,
    ): Int {
        if (player == Player.WHITE) return position.row

        return numberRows - position.row + 1
    }

    override fun isPositionOnLastRow(position: Position): Boolean {
        return position.row == numberRows
    }
}
