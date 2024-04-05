package edu.austral.dissis.chess.engine

class Piece(val player: Player, val rules: PieceRules) {
    fun isPlayValid(from: String, to: String): Boolean {
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
    fun isPlayValid(from: String, to: String): Boolean
    fun getValidPlays(): Set<Play>
    fun getPlayIfValid(board: GameBoard, from: String, to: String): Play?
}

class PathValidator {
    fun isPathClear(player: Player, from: String, to: String) {
        // Checks whether something is blocking the path between a piece and a position.
        // Will probably hold all of the standard paths (for instance, up, down, left, right, each diagonal,etc.), since many are repeated across pieces.
        // TODO: Should this actually be an interface? A singleton class?
    }
}

class PawnPieceRules : PieceRules {
    val player: Player
    val hasEverMoved: Boolean
    val hasJustMovedTwoPlaces: Boolean

    enum class State {
        MOVED,
        MOVED_TWO_PLACES
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


    override fun isPlayValid(from: String, to: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValidPlays(): Set<Play> {
        TODO("Not yet implemented")
    }

    override fun getPlayIfValid(board: GameBoard, from: String, to: String): Play? {
        val (fromRow, fromCol) = board.unpackPosition(from)
        val (toRow, toCol) = board.unpackPosition(to)
        val fromRowAsWhite = board.getRowAsWhite(from, player)
        val toRowAsWhite = board.getRowAsWhite(to, player)
        val rowDelta = toRowAsWhite - fromRowAsWhite
        val colDelta = toCol - fromCol

        if (!isPlayWithinPawnRange(rowDelta, colDelta)) {
            println("A pawn cannot move this way")
            return null
        }

        // TODO: en passant

        when (rowDelta) {
            1 -> {
                when (colDelta) {
                    0 -> {
                        if (board.isOccupied(to)) {
                            return null;
                        }

                        return Play(listOf(Move(from, to, board)), board)
                    }
                    1, -1 -> {
                        if (!board.containsPieceOfPlayer(to, !player)) {
                            return null
                        }

                        return Play(listOf(Move(from, to, board)), board)
                    }
                }
            }
            2 -> {
                if (hasEverMoved) {
                    println("Pawn already moved. Cannot move two places")
                    return null;
                }
                val front: String = getStringPosition(fromCol, fromRow + 1)
                if (board.isOccupied(to) || board.isOccupied(front)) {
                    return null
                }

                if (!hasEverMoved) {
                    val rulesNextTurn = PawnPieceRules(player, State.MOVED)
                    val pieceNextTurn = Piece(player, rulesNextTurn)
                    return Play(listOf(Move(from, to, board, pieceNextTurn)), board)
                }
                else return Play(listOf(Move(from, to, board)), board)
            }
        }

        return null
    }

    private fun isPlayWithinPawnRange(rowDelta: Int, colDelta: Int): Boolean {
        val movedFrontOrDiagonally = (rowDelta == 1) && (-1 <= colDelta) && (colDelta <= 1)
        val movedTwoSpaces = (rowDelta == 2 && colDelta == 0)
        return movedFrontOrDiagonally || movedTwoSpaces
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

    override fun isPlayValid(from: String, to: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValidPlays(): Set<Play> {
        TODO("Not yet implemented")
    }

    override fun getPlayIfValid(board: GameBoard, from: String, to: String): Play? {
        val moveData = MovementData(from, to, board)


        if (PathHeuristic.VERTICAL_AND_HORIZONTAL.isViolated(moveData)) {
            println("A tower cannot move this way")
            return null
        }

        if (PathHeuristic.VERTICAL_AND_HORIZONTAL.isPathBlocked(moveData, board)) {
            println("Cannot move there: the path is blocked")
            return null
        }


        if (!hasEverMoved) {
            val rulesNextTurn = RookPieceRules(player, true)
            val pieceNextTurn = Piece(player, rulesNextTurn)
            return Play(listOf(Move(from, to, board, pieceNextTurn)), board)
        }
        else return Play(listOf(Move(from, to, board)), board)
    }
}

class BishopPieceRules : PieceRules {

    override fun isPlayValid(from: String, to: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValidPlays(): Set<Play> {
        TODO("Not yet implemented")
    }

    override fun getPlayIfValid(board: GameBoard, from: String, to: String): Play? {
        val moveData = MovementData(from, to, board)


        if (PathHeuristic.DIAGONAL.isViolated(moveData)) {
            println("A bishop cannot move this way")
            return null
        }

        if (PathHeuristic.DIAGONAL.isPathBlocked(moveData, board)) {
            println("Cannot move there: the path is blocked")
            return null
        }

        return Play(listOf(Move(from, to, board)), board)
    }
}

class QueenPieceRules : PieceRules {
    override fun isPlayValid(from: String, to: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValidPlays(): Set<Play> {
        TODO("Not yet implemented")
    }

    override fun getPlayIfValid(board: GameBoard, from: String, to: String): Play? {
        val moveData = MovementData(from, to, board)


        if (PathHeuristic.ANY_STRAIGHT.isViolated(moveData)) {
            println("A bishop cannot move this way")
            return null
        }

        if (PathHeuristic.ANY_STRAIGHT.isPathBlocked(moveData, board)) {
            println("Cannot move there: the path is blocked")
            return null
        }

        return Play(listOf(Move(from, to, board)), board)
    }
}

class KnightPieceRules : PieceRules {
    override fun isPlayValid(from: String, to: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValidPlays(): Set<Play> {
        TODO("Not yet implemented")
    }

    override fun getPlayIfValid(board: GameBoard, from: String, to: String): Play? {
        val moveData = MovementData(from, to, board)


        if (PathHeuristic.L_SHAPE.isViolated(moveData)) {
            println("A bishop cannot move this way")
            return null
        }

        return Play(listOf(Move(from, to, board)), board)
    }
}