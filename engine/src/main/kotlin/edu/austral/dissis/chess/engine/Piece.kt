package edu.austral.dissis.chess.engine

import kotlin.math.absoluteValue

class Piece(val player: Player, val rules: PieceRules) {
    fun isPlayValid(
        from: String,
        to: String,
    ): Boolean {
        return rules.isPlayValid(from, to)
    }

    fun getValidPlays(): Set<Play> {
        return rules.getValidPlays()
    }

//    fun getPlayIfValid(from: String, to: String): Play? {
//        return rules.getPlayIfValid(from, to)
//    }
}

interface PieceRules {
    fun isPlayValid(
        from: String,
        to: String,
    ): Boolean

    fun getValidPlays(): Set<Play>

    fun getPlayIfValid(
        board: GameBoard,
        from: String,
        to: String,
    ): Play?
}

class PawnPieceRules : PieceRules {
    private val player: Player
    private val hasEverMoved: Boolean
    private val hasJustMovedTwoPlaces: Boolean

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
                this.hasJustMovedTwoPlaces = false
            }
        }
    }

    override fun isPlayValid(
        from: String,
        to: String,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValidPlays(): Set<Play> {
        TODO("Not yet implemented")
    }

    override fun getPlayIfValid(
        board: GameBoard,
        from: String,
        to: String,
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
        if (hasEverMoved || pathIsBlocked(board, moveData)) {
            return null
        }

        val rulesNextTurn = PawnPieceRules(player, State.MOVED)
        val pieceNextTurn = Piece(player, rulesNextTurn)
        return Play(listOf(Move(moveData.from, moveData.to, board, pieceNextTurn)), board)
    }

    private fun pathIsBlocked(
        board: GameBoard,
        moveData: MovementData,
    ): Boolean {
        val front: String = getStringPosition(moveData.fromCol, moveData.fromRow + 1)
        return board.isOccupied(moveData.to) || board.isOccupied(front)
    }

    private fun moveAheadOrDiagonallyIfValid(
        board: GameBoard,
        moveData: MovementData,
    ): Play? {
        return when (moveData.colDelta) {
            0 -> {
                if (board.isOccupied(moveData.to)) {
                    null
                } else {
                    Play(listOf(Move(moveData.from, moveData.to, board)), board)
                }
            }
            1, -1 -> {
                if (!board.containsPieceOfPlayer(moveData.to, !player)) {
                    null
                } else {
                    Play(listOf(Move(moveData.from, moveData.to, board)), board)
                }
            }

            else -> {
                println("A pawn cannot move this way")
                null
            }
        }
    }
}

class RookPieceRules : PieceRules {
    private val player: Player
    private val hasEverMoved: Boolean

    constructor(player: Player) {
        this.player = player
        this.hasEverMoved = false
    }

    constructor(player: Player, hasEverMoved: Boolean) {
        this.player = player
        this.hasEverMoved = true
    }

    override fun isPlayValid(
        from: String,
        to: String,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValidPlays(): Set<Play> {
        TODO("Not yet implemented")
    }

    override fun getPlayIfValid(
        board: GameBoard,
        from: String,
        to: String,
    ): Play? {
        val moveData = MovementData(from, to, board)

        return when {
            Path.VERTICAL_AND_HORIZONTAL.isViolated(moveData) -> {
                println("A tower cannot move this way")
                null
            }
            Path.VERTICAL_AND_HORIZONTAL.isPathBlocked(moveData, board) -> {
                println("Cannot move there: the path is blocked")
                null
            }
            !hasEverMoved -> {
                val rulesNextTurn = RookPieceRules(player, true)
                val pieceNextTurn = Piece(player, rulesNextTurn)
                Play(listOf(Move(from, to, board, pieceNextTurn)), board)
            }
            else -> {
                Play(listOf(Move(from, to, board)), board)
            }
        }
    }
}

class BishopPieceRules : PieceRules {
    override fun isPlayValid(
        from: String,
        to: String,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValidPlays(): Set<Play> {
        TODO("Not yet implemented")
    }

    override fun getPlayIfValid(
        board: GameBoard,
        from: String,
        to: String,
    ): Play? {
        val moveData = MovementData(from, to, board)

        return when {
            Path.DIAGONAL.isViolated(moveData) -> {
                println("A bishop cannot move this way")
                null
            }
            Path.DIAGONAL.isPathBlocked(moveData, board) -> {
                println("Cannot move there: the path is blocked")
                null
            }
            else -> {
                Play(listOf(Move(from, to, board)), board)
            }
        }
    }
}

class QueenPieceRules : PieceRules {
    override fun isPlayValid(
        from: String,
        to: String,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValidPlays(): Set<Play> {
        TODO("Not yet implemented")
    }

    override fun getPlayIfValid(
        board: GameBoard,
        from: String,
        to: String,
    ): Play? {
        val moveData = MovementData(from, to, board)

        return when {
            Path.ANY_STRAIGHT_LINE.isViolated(moveData) -> {
                println("A queen cannot move this way")
                null
            }
            Path.ANY_STRAIGHT_LINE.isPathBlocked(moveData, board) -> {
                println("Cannot move there: the path is blocked")
                null
            }
            else -> {
                Play(listOf(Move(from, to, board)), board)
            }
        }
    }
}

class KnightPieceRules : PieceRules {
    override fun isPlayValid(
        from: String,
        to: String,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValidPlays(): Set<Play> {
        TODO("Not yet implemented")
    }

    override fun getPlayIfValid(
        board: GameBoard,
        from: String,
        to: String,
    ): Play? {
        val moveData = MovementData(from, to, board)

        return when {
            Path.L_SHAPED.isViolated(moveData) -> {
                println("A bishop cannot move this way")
                null
            }
            else -> Play(listOf(Move(from, to, board)), board)
        }
    }
}

class KingPieceRules(val player: Player) : PieceRules {
    override fun isPlayValid(
        from: String,
        to: String,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValidPlays(): Set<Play> {
        TODO("Not yet implemented")
    }

    override fun getPlayIfValid(
        board: GameBoard,
        from: String,
        to: String,
    ): Play? {
        val moveData = MovementData(from, to, board)
        return when {
            !isMovementLogical(moveData) -> {
                println("The king cannot move this way")
                null
            }
            becomesChecked(board, moveData) -> {
                println("Invalid movement: the king would become checked")
                null
            }
            else -> Play(listOf(Move(from, to, board)), board)
        }
    }

    fun isChecked(board: GameBoard): Boolean {
        val kingPosition = board.getKingPosition(player)

        // Return true if any enemy piece can perform a Take action on the King
        return board.getAllPositionsOfPlayer(!player).any {
            val enemyPosition: String = it
            val enemyPiece: Piece = board.getPieceAt(enemyPosition)!!
            val kingCapture: Play? = enemyPiece.rules.getPlayIfValid(board, enemyPosition, kingPosition)

            kingCapture != null
        }
    }

    private fun becomesChecked(
        board: GameBoard,
        moveData: MovementData,
    ): Boolean {
        val futureBoard: GameBoard = Move(moveData.from, moveData.to, board).execute()
        return this.isChecked(futureBoard)
    }

    private fun isMovementLogical(moveData: MovementData): Boolean {
        return moveData.rowDelta.absoluteValue <= 1 || moveData.colDelta.absoluteValue <= 1
    }
}
