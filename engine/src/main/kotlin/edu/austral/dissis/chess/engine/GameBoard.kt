package edu.austral.dissis.chess.engine

interface GameBoard {
    fun isOccupied(position: String): Boolean

    fun getPieceAt(position: String): Piece?

    fun setPieceAt(
        position: String,
        piece: Piece,
    ): GameBoard

    fun delPieceAt(position: String): GameBoard

    fun positionExists(position: String): Boolean

    fun containsPieceOfPlayer(
        position: String,
        player: Player,
    ): Boolean

    fun getAllPositionsOfPlayer(player: Player): Iterable<String>

    fun unpackPosition(position: String): RowAndCol

    fun getRowAsWhite(
        position: String,
        player: Player,
    ): Int

    fun getKingPosition(player: Player): String
}

class HashGameBoard : GameBoard {
    private val validator: PositionValidator
    private val boardMap: Map<String, Piece>

    private val whiteKingPosition: String
    private val blackKingPosition: String

    companion object {
        fun build(
            validator: PositionValidator,
            pieces: List<Pair<String, Piece>>,
            whiteKingPosition: String,
            blackKingPosition: String,
        ): HashGameBoard {
            return HashGameBoard(validator, pieces.toMap(), whiteKingPosition, blackKingPosition)
        }
    }

    private constructor(
        validator: PositionValidator,
        boardMap: Map<String, Piece>,
        whiteKingPosition: String,
        blackKingPosition: String,
    ) {
        this.validator = validator
        this.boardMap = boardMap

        for (pair in listOf(Player.WHITE, Player.BLACK).zip(listOf(whiteKingPosition, blackKingPosition))) {
            val player = pair.first
            val position = pair.second

            val king = boardMap[position]
            require(king != null && king.rules is KingPieceRules && king.player == player) {
                "The $player king is not located at $king"
            }
        }

        this.whiteKingPosition = whiteKingPosition
        this.blackKingPosition = blackKingPosition
    }

//    constructor(validator: PositionValidator, boardMap: Map<String, Piece>) {
//        this.validator = validator
//        this.boardMap = boardMap
//    }

    override fun isOccupied(position: String): Boolean {
        return boardMap[position] != null
    }

    override fun getPieceAt(position: String): Piece? {
        return boardMap[position]
    }

    override fun setPieceAt(
        position: String,
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

    override fun delPieceAt(position: String): HashGameBoard {
        // TODO: This should actually be a impossible situation,
        //  since there should never be a delPieceAt() for the king.
        //  Should we keep this or erase it?
        require(position != whiteKingPosition && position != blackKingPosition) {
            "A king cannot be deleted"
        }

        val newMap: HashMap<String, Piece> = HashMap(boardMap)
        newMap.remove(position)

        return HashGameBoard(validator, newMap, whiteKingPosition, blackKingPosition)
    }

    override fun positionExists(position: String): Boolean {
        return validator.positionExists(position)
    }

    override fun containsPieceOfPlayer(
        position: String,
        player: Player,
    ): Boolean {
        val piece = getPieceAt(position) ?: return false
        return piece.player == player
    }

    override fun getAllPositionsOfPlayer(player: Player): Iterable<String> {
        return boardMap.keys.filter {
            val position: String = it
            boardMap[position]!!.player == player
        }
    }

    override fun unpackPosition(position: String): RowAndCol {
        return validator.unpackPosition(position)
    }

    override fun getRowAsWhite(
        position: String,
        player: Player,
    ): Int {
        return validator.getRowAsWhite(position, player)
    }

    override fun getKingPosition(player: Player): String {
        return when (player) {
            Player.WHITE -> whiteKingPosition
            Player.BLACK -> blackKingPosition
        }
    }
}

data class RowAndCol(val row: Int, val col: Int)

interface PositionValidator {
    fun positionExists(position: String): Boolean

    fun unpackPosition(position: String): RowAndCol

    fun getRowAsWhite(
        position: String,
        player: Player,
    ): Int
}

class RectangleBoardValidator(private val numberRows: Int, private val numberCols: Int) : PositionValidator {
    override fun positionExists(position: String): Boolean {
        val (row, col) = unpackPosition(position)
        return (row <= numberRows) && (col <= numberCols)
    }

    override fun getRowAsWhite(
        position: String,
        player: Player,
    ): Int {
        if (player == Player.WHITE) return position[1].charToCol()

        return numberRows - position[1].charToCol() + 1
    }

    override fun unpackPosition(position: String): RowAndCol {
        val col: Int = position[0].charToCol()
        val row: Int = position[1].digitToInt()
        return RowAndCol(row, col)
    }
}
