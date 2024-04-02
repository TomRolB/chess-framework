package edu.austral.dissis.chess.engine

class Piece(val player: Player, val board: GameBoard, val rules: PieceRules) {
    fun isPlayValid(from: String, to: String): Boolean {
        return rules.isPlayValid(from, to)
    }
    fun getValidPlays(): Set<Play> {
        return rules.getValidPlays()
    }

    fun getPlayIfValid(from: String, to: String): Play? {
        return rules.getPlayIfValid(from, to)
    }
}

interface PieceRules {
    fun isPlayValid(from: String, to: String): Boolean
    fun getValidPlays(): Set<Play>
    fun getPlayIfValid(from: String, to: String): Play?
}

class PathValidator {
    fun isPathClear(player: Player, from: String, to: String) {
        // Checks whether something is blocking the path between a piece and a position.
        // Will probably hold all of the standard paths (for instance, up, down, left, right, each diagonal,etc.), since many are repeated across pieces.
        // TODO: Should this actually be an interface? A singleton class?
    }
}

class PawnPieceRules(val board: GameBoard, val player: Player) : PieceRules {
    var hasEverMoved: Boolean = false
    override fun isPlayValid(from: String, to: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValidPlays(): Set<Play> {
        TODO("Not yet implemented")
    }

    override fun getPlayIfValid(from: String, to: String): Play? {
        val (fromRow, fromCol) = board.unpackPosition(from)
        val (toRow, toCol) = board.unpackPosition(to)
        val fromRowAsWhite = board.getRowAsWhite(from)
        val toRowAsWhite = board.getRowAsWhite(to)
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
                        return Play(listOf(Move(from, to, board)))
                    }
                    1, -1 -> {
                        if (!board.containsPieceOfPlayer(to, !player)) {
                            return null
                        }
                        return Play(listOf(Move(from, to, board)))
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
                    return null;
                }
                return Play(listOf(Move(from, to, board)))
            }
        }

        return null
    }

    private fun isPlayWithinPawnRange(rowDelta: Int, colDelta: Int): Boolean {
        val movedFrontOrDiagonally = (rowDelta == 1) && (-1 < colDelta) && (colDelta < 1)
        val movedTwoSpaces = (rowDelta == 2 && colDelta == 0)
        return movedFrontOrDiagonally || movedTwoSpaces
    }


}
