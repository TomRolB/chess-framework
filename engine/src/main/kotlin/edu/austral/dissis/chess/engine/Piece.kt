package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.rules.castling.Castling

// Our engine is not interested in whether two pieces of the same
// type are different objects or not: the pieces are immutable,
// and the engine identifies the difference in positions merely
// from the game board.

// The only thing to consider is having separate states for these
// pieces, which is achieved with different PieceRules instantiations.

// Therefore, we use a data class, to allow hashmap usage.
data class Piece(val player: Player, val rules: PieceRules) {
    override fun toString(): String {
        return "$player, $rules"
    }

    override fun hashCode(): Int {
        return (player to rules::class).hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is Piece &&
            this.hashCode() == other.hashCode()
    }
}

interface PieceRules {
    fun isPlayValid(
        from: Position,
        to: Position,
    ): Boolean

    fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play>

    fun getPlayIfValid(
        board: GameBoard,
        from: Position,
        to: Position,
    ): Play?
}

interface MoveDependant : PieceRules {
    val hasEverMoved: Boolean
}

class PawnPieceRules : MoveDependant {
    private val player: Player
    private val hasJustMovedTwoPlaces: Boolean
    private val increments = listOf(1 to 1, 0 to 1, -1 to 1, 0 to 2)
    override val hasEverMoved: Boolean

    enum class State {
        MOVED,
        MOVED_TWO_PLACES,
    }

    constructor(player: Player) {
        this.player = player
        this.hasEverMoved = false
        this.hasJustMovedTwoPlaces = false
    }

    constructor(player: Player, state: State) {
        this.player = player
        when (state) {
            State.MOVED -> {
                this.hasEverMoved = true
                this.hasJustMovedTwoPlaces = false
            }
            State.MOVED_TWO_PLACES -> {
                this.hasEverMoved = true
                this.hasJustMovedTwoPlaces = true
            }
        }
    }

    override fun isPlayValid(
        from: Position,
        to: Position,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        val (row, col) = position

        return increments
            .map { Position(row + it.first, col + it.second) }
            .mapNotNull { getPlayIfValid(board, position, it) }
            .filter {
                val futureBoard = it.execute()
                !KingPieceRules.isChecked(futureBoard, player)
            }
    }

    override fun getPlayIfValid(
        board: GameBoard,
        from: Position,
        to: Position,
    ): Play? {
        // In this case, moveData is always created from
        // WHITE's point of view, to avoid splitting rules
        // based on the player
        val moveData = MovementData(from, to, board, player)

        // TODO: en passant

        return when (moveData.rowDelta) {
            1 -> moveAheadOrDiagonallyIfValid(board, moveData)
            2 -> moveTwoPlacesIfValid(board, moveData)
            else -> {
                println("A pawn cannot move this way")
                null
            }
        }
    }

    private fun moveTwoPlacesIfValid(
        board: GameBoard,
        moveData: MovementData,
    ): Play? {
        if (moveData.colDelta != 0 || hasEverMoved || pathIsBlocked(board, moveData)) {
            return null
        }

        val rulesNextTurn = PawnPieceRules(player, State.MOVED_TWO_PLACES)
        val pieceNextTurn = Piece(player, rulesNextTurn)
        return Play(listOf(Move(moveData.from, moveData.to, board, pieceNextTurn)))
    }

    private fun pathIsBlocked(
        board: GameBoard,
        moveData: MovementData,
    ): Boolean {
        val frontPos = Position((moveData.fromRow + moveData.toRow) / 2, moveData.fromCol)
        return board.isOccupied(moveData.to) || board.isOccupied(frontPos)
    }

    private fun moveAheadOrDiagonallyIfValid(
        board: GameBoard,
        moveData: MovementData,
    ): Play? {
        return when (moveData.colDelta) {
            // TODO: Replaceable by a map of deltas, which takes you
            //  to the conditions related to the delta in question
            0 -> {
                if (board.isOccupied(moveData.to)) {
                    null
                } else {
                    val rulesNextTurn = PawnPieceRules(player, State.MOVED)
                    val pieceNextTurn = Piece(player, rulesNextTurn)
                    Move(moveData.from, moveData.to, board, pieceNextTurn).asPlay()
                }
            }
            1, -1 -> {
                if (board.containsPieceOfPlayer(moveData.to, !player)) {
                    val rulesNextTurn = PawnPieceRules(player, State.MOVED)
                    val pieceNextTurn = Piece(player, rulesNextTurn)
                    Move(moveData.from, moveData.to, board, pieceNextTurn).asPlay()
                } else {
                    enPassantIfValid(board, moveData)
                }
            }

            else -> {
                println("A pawn cannot move this way")
                null
            }
        }
    }

//    private fun safeGetPieceOfColor(piece: Piece, player: Player): Piece? =
//        if (piece.player == player) piece else null
//
//    private fun safeGetPieceOfTypePawn(piece: Piece): Piece? =
//        if (piece.rules !is PawnPieceRules) piece else null

    private fun enPassantIfValid(
        board: GameBoard,
        moveData: MovementData,
    ): Play? {
        val enemyPawnPosition = Position(moveData.fromRow, moveData.toCol)
        if (!board.containsPieceOfPlayer(enemyPawnPosition, !player)) return null

        val enemyPawn = board.getPieceAt(enemyPawnPosition)!!
        if (enemyPawn.rules !is PawnPieceRules) return null

        val enemyRules = enemyPawn.rules
        if (!enemyRules.hasJustMovedTwoPlaces) return null

        return Play(
            listOf(
                Move(moveData.from, moveData.to, board),
                Take(enemyPawnPosition, board),
            ),
        )
    }
}

class RookPieceRules : MoveDependant {
    private val moveType = ClassicMoveType.VERTICAL_AND_HORIZONTAL
    private val player: Player
    override val hasEverMoved: Boolean

    constructor(player: Player) {
        this.player = player
        this.hasEverMoved = false
    }

    constructor(player: Player, hasEverMoved: Boolean) {
        this.player = player
        this.hasEverMoved = hasEverMoved
    }

    override fun isPlayValid(
        from: Position,
        to: Position,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        return moveType
            .getPossiblePositions(board, position)
            .map { Play(listOf(Move(position, it, board))) }
            .filter {
                val futureBoard = it.execute()
                !KingPieceRules.isChecked(futureBoard, player)
            }
    }

    override fun getPlayIfValid(
        board: GameBoard,
        from: Position,
        to: Position,
    ): Play? {
        val moveData = MovementData(from, to, board)

        return when {
            moveType.isViolated(moveData) -> {
                println("A tower cannot move this way")
                null
            }
            moveType.isPathBlocked(moveData, board) -> {
                println("Cannot move there: the path is blocked")
                null
            }
            !hasEverMoved -> {
                val rulesNextTurn = RookPieceRules(player, true)
                val pieceNextTurn = Piece(player, rulesNextTurn)
                Move(from, to, board, pieceNextTurn).asPlay()
            }
            else -> {
                Move(from, to, board).asPlay()
            }
        }
    }
}

class BishopPieceRules(val player: Player) : PieceRules {
    private val moveType = ClassicMoveType.DIAGONAL

    override fun isPlayValid(
        from: Position,
        to: Position,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        return moveType
            .getPossiblePositions(board, position)
            .map {
                Play(listOf(Move(position, it, board)))
            }
            .filter {
                val futureBoard = it.execute()
                !KingPieceRules.isChecked(futureBoard, player)
            }
    }

    override fun getPlayIfValid(
        board: GameBoard,
        from: Position,
        to: Position,
    ): Play? {
        val moveData = MovementData(from, to, board)

        return when {
            moveType.isViolated(moveData) -> {
                println("A bishop cannot move this way")
                null
            }
            moveType.isPathBlocked(moveData, board) -> {
                println("Cannot move there: the path is blocked")
                null
            }
            else -> {
                Move(from, to, board).asPlay()
            }
        }
    }
}

class QueenPieceRules(val player: Player) : PieceRules {
    private val moveType = ClassicMoveType.ANY_STRAIGHT_LINE

    override fun isPlayValid(
        from: Position,
        to: Position,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        return moveType
            .getPossiblePositions(board, position)
            .map { Play(listOf(Move(position, it, board))) }
            .filter {
                val futureBoard = it.execute()
                !KingPieceRules.isChecked(futureBoard, player)
            }
    }

    override fun getPlayIfValid(
        board: GameBoard,
        from: Position,
        to: Position,
    ): Play? {
        val moveData = MovementData(from, to, board)

        return when {
            moveType.isViolated(moveData) -> {
                println("A queen cannot move this way")
                null
            }
            moveType.isPathBlocked(moveData, board) -> {
                println("Cannot move there: the path is blocked")
                null
            }
            else -> {
                Move(from, to, board).asPlay()
            }
        }
    }
}

class KnightPieceRules(val player: Player) : PieceRules {
    private val moveType = ClassicMoveType.L_SHAPED

    override fun isPlayValid(
        from: Position,
        to: Position,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        return moveType
            .getPossiblePositions(board, position)
            .map { Move(position, it, board).asPlay() }
            .filter {
                val futureBoard = it.execute()
                !KingPieceRules.isChecked(futureBoard, player)
            }
    }

    override fun getPlayIfValid(
        board: GameBoard,
        from: Position,
        to: Position,
    ): Play? {
        val moveData = MovementData(from, to, board)

        return when {
            moveType.isViolated(moveData) -> {
                println("A bishop cannot move this way")
                null
            }
            else -> Move(from, to, board).asPlay()
        }
    }
}

private const val C_COLUMN = 3

private const val G_COLUMN = 7

class KingPieceRules : MoveDependant {
    val player: Player
    private val moveType = ClassicMoveType.ADJACENT_SQUARE
    override val hasEverMoved: Boolean

    constructor(player: Player) {
        this.player = player
        this.hasEverMoved = false
    }

    constructor(player: Player, hasEverMoved: Boolean) {
        this.player = player
        this.hasEverMoved = hasEverMoved
    }

    fun asMoved(): Piece {
        // Return this piece with hasEverMoved = true
        return Piece(player, KingPieceRules(player, hasEverMoved = true))
    }

    override fun isPlayValid(
        from: Position,
        to: Position,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        return moveType
            .getPossiblePositions(board, position)
            .map {
                val pieceNextTurn = Piece(player, KingPieceRules(player, hasEverMoved = true))
                Move(position, it, board, pieceNextTurn).asPlay()
            }
            .filter {
                val futureBoard = it.execute()
                !isChecked(futureBoard, player)
            }
            .plus(
                // Possible castling
                listOf(
                    Position(position.row, C_COLUMN),
                    Position(position.row, G_COLUMN),
                ).mapNotNull {
                    getPlayIfValid(board, position, it)
                },
            )
    }

    override fun getPlayIfValid(
        board: GameBoard,
        from: Position,
        to: Position,
    ): Play? {
        val moveData = MovementData(from, to, board)
        return when {
            moveType.isViolated(moveData) -> {
                Castling(this, hasEverMoved, board, from, to).verify()
            }
// This is actually verified at Game
//            becomesChecked(board, moveData) -> {
//                println("Invalid movement: the king would become checked")
//                null
//            }
            else -> {
                val pieceNextTurn = Piece(player, KingPieceRules(player, hasEverMoved = true))
                Move(from, to, board, pieceNextTurn).asPlay()
            }
        }
    }

    companion object {
        fun isChecked(
            board: GameBoard,
            player: Player,
        ): Boolean {
            val kingPosition = board.getKingPosition(player)

            // Return true if any enemy piece "can capture" the King
            // We don't include the enemy king, since the kings cannot
            // check each other
            return board.getAllPositionsOfPlayer(!player, false).any {
                val enemyPosition: Position = it
                val enemyPiece: Piece = board.getPieceAt(enemyPosition)!!
                val kingCapture: Play? = enemyPiece.rules.getPlayIfValid(board, enemyPosition, kingPosition)

                kingCapture != null
            }
        }

        //        fun willBeChecked(board: GameBoard, player: Player): Boolean {
//            val kingPosition = board.getKingPosition(player)
//
//            return getPossibleFuturePositions(board, player, kingPosition)
//                .all {
//                    val futureBoard = Play(listOf(Move(kingPosition, it, board)), board).execute()
//                    isChecked(futureBoard, player)
//                }
//        }
        private fun willBeChecked(
            board: GameBoard,
            player: Player,
        ): Boolean {
            return board.getAllPositionsOfPlayer(player, true).all {
                val piece = board.getPieceAt(it)!!
                allMovementsEndInCheck(board, piece, it)
            }
        }

        private fun allMovementsEndInCheck(
            board: GameBoard,
            piece: Piece,
            position: Position,
        ): Boolean {
            return piece.rules.getValidPlays(board, position).all {
                val futureBoard = it.execute()
                isChecked(futureBoard, piece.player)
            }
        }

        fun getPlayerState(
            board: GameBoard,
            player: Player,
        ): PlayerState {
            val isChecked: Int = if (isChecked(board, player)) 1 else 0
            val willBeChecked: Int = if (willBeChecked(board, player)) 1 else 0
            val combinedStatus: Int = isChecked * 2 + willBeChecked

            return PlayerState.entries[combinedStatus]
        }
    }
}
