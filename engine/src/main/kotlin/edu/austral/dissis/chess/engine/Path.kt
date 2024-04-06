package edu.austral.dissis.chess.engine

import kotlin.math.absoluteValue
import kotlin.math.sign

enum class Path {
    VERTICAL_AND_HORIZONTAL,
    DIAGONAL,
    ANY_STRAIGHT,
    L_SHAPE,
    ;

    // TODO: Yes, names are horrible. Change them.

    fun isViolated(moveData: MovementData): Boolean {
        return when (this) {
            VERTICAL_AND_HORIZONTAL -> {
                val movedVertically: Boolean = (moveData.rowDelta != 0)
                val movedHorizontally: Boolean = (moveData.colDelta != 0)
                val movedBothWays = (movedVertically && movedHorizontally)

                movedBothWays
            }
            DIAGONAL -> {
                val movedDiagonally: Boolean = moveData.rowDelta.absoluteValue == moveData.colDelta.absoluteValue

                !movedDiagonally
            }
            ANY_STRAIGHT -> {
                val noStraightLine = VERTICAL_AND_HORIZONTAL.isViolated(moveData) && DIAGONAL.isViolated(moveData)

                noStraightLine
            }
            L_SHAPE -> {
                val absRowDelta = moveData.rowDelta.absoluteValue
                val absColDelta = moveData.colDelta.absoluteValue
                val movedInL = (absRowDelta == 1 && absColDelta == 2) || (absRowDelta == 2 && absColDelta == 1)

                !movedInL
            }
        }
    }

    fun isPathBlocked(
        moveData: MovementData,
        board: GameBoard,
    ): Boolean {
        return when (this) {
            VERTICAL_AND_HORIZONTAL, DIAGONAL, ANY_STRAIGHT -> {
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

            L_SHAPE -> false
        }
    }
}
