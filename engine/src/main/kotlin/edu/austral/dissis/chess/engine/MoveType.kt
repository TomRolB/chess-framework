package edu.austral.dissis.chess.engine

import kotlin.math.absoluteValue
import kotlin.math.sign

enum class MoveType {
    VERTICAL_AND_HORIZONTAL,
    DIAGONAL,
    ANY_STRAIGHT_LINE,
    L_SHAPED,
    ADJACENT_SQUARE
    ;

    fun isViolated(moveData: MovementData): Boolean {
        return when (this) {
            VERTICAL_AND_HORIZONTAL -> {
                val movedVertically = (moveData.rowDelta != 0)
                val movedHorizontally = (moveData.colDelta != 0)
                val movedBothWays = (movedVertically && movedHorizontally)

                movedBothWays
            }
            DIAGONAL -> {
                val movedDiagonally = moveData.rowDelta.absoluteValue == moveData.colDelta.absoluteValue

                !movedDiagonally
            }
            ANY_STRAIGHT_LINE -> {
                val noStraightLine = VERTICAL_AND_HORIZONTAL.isViolated(moveData)
                                      && DIAGONAL.isViolated(moveData)

                noStraightLine
            }
            L_SHAPED -> {
                val absRowDelta = moveData.rowDelta.absoluteValue
                val absColDelta = moveData.colDelta.absoluteValue
                val movedInL = (absRowDelta == 1 && absColDelta == 2)
                                || (absRowDelta == 2 && absColDelta == 1)

                !movedInL
            }
            ADJACENT_SQUARE -> {
                val movedToAdjSquare =  moveData.rowDelta.absoluteValue <= 1
                                        || moveData.colDelta.absoluteValue <= 1

                !movedToAdjSquare
            }
        }
    }

    fun isPathBlocked(
        moveData: MovementData,
        board: GameBoard,
    ): Boolean {
        return when (this) {
            VERTICAL_AND_HORIZONTAL, DIAGONAL, ANY_STRAIGHT_LINE -> {
                val rowIncrement = moveData.rowDelta.sign
                val colIncrement = moveData.colDelta.sign

                var row = moveData.fromRow + rowIncrement
                var col = moveData.fromCol + colIncrement

                var anyPieceBlocking = false

                while (!(row == moveData.toRow && col == moveData.toCol)) {
                    val position: String = getStringPosition(row, col)
                    if (board.isOccupied(position)) {
                        anyPieceBlocking = true
                        break
                    }

                    row += rowIncrement
                    col += colIncrement
                }

                anyPieceBlocking
            }

            L_SHAPED, ADJACENT_SQUARE -> {
                throw UnsupportedOperationException("There's no path calculation for this")
            }
        }
    }

    fun getIncrements(): Iterable<Pair<Int, Int>> {
        return when (this) {
            VERTICAL_AND_HORIZONTAL -> listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1)
            DIAGONAL -> listOf(1 to 1, -1 to 1, -1 to -1, 1 to -1)
            ANY_STRAIGHT_LINE -> {
                getIncrements() + getIncrements()
            }
            L_SHAPED -> listOf(
                2 to 1, 1 to 2, -2 to 1, -1 to 2,
                2 to -1, 1 to -2, -2 to -1, -1 to -2,
            )

            ADJACENT_SQUARE -> listOf(
                1 to 0, 1 to 1, 0 to 1, -1 to 1,
                -1 to 0, -1 to -1, 0 to -1, -1 to 1
            )
        }
    }

    fun getPossiblePositions(board: GameBoard, position: String): Iterable<String> {
        return when (this) {
            VERTICAL_AND_HORIZONTAL, DIAGONAL, ANY_STRAIGHT_LINE -> {
                this.getIncrements()
                    .flatMap { getLineOfPositions(board, position, it) }
            }
            L_SHAPED, ADJACENT_SQUARE -> {
                val (row, col) = board.unpackPosition(position)
                val player = board.getPieceAt(position)!!.player

                this.getIncrements()
                    .map { getStringPosition(row + it.first, col + it.second) }
                    .filter{
                        board.positionExists(it)
                        && !board.containsPieceOfPlayer(it, player)
                    }
            }
        }
    }

    private fun getLineOfPositions(board: GameBoard, position: String, increments: Pair<Int, Int>): List<String> {
        var (row, col) = board.unpackPosition(position)
        val player = board.getPieceAt(position)!!.player

        val rowIncrement = increments.first
        val colIncrement = increments.second

        var result: MutableList<String> = mutableListOf()

        while (true) {
            val reachablePos: String = getStringPosition(row, col)

            // We'll only add the position if it exists and does not hold
            // a piece of the same player
            if (!board.positionExists(reachablePos)
                || board.containsPieceOfPlayer(reachablePos, player)) break

            result.addLast(reachablePos)

            // We check if there is an enemy piece after adding, since
            // it is possible to eat that piece, but we must then
            // break, since the rest of the path is blocked by it
            if (board.containsPieceOfPlayer(reachablePos, !player)) break

            row += rowIncrement
            col += colIncrement
        }

        return result
    }
}
