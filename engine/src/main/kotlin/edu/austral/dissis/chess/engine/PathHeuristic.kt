package edu.austral.dissis.chess.engine

import kotlin.math.absoluteValue
import kotlin.math.sign

enum class PathHeuristic {
    VERTICAL_AND_HORIZONTAL,
    DIAGONAL,
    ANY_STRAIGHT,
    L_SHAPE;
    //TODO: Yes, names are horrible. Change them.

    fun isViolated(moveData: MovementData): Boolean {
        when(this) {
            VERTICAL_AND_HORIZONTAL -> {
                val movedVertically: Boolean = (moveData.rowDelta != 0)
                val movedHorizontally: Boolean = (moveData.colDelta != 0)
                val movedBothWays = (movedVertically && movedHorizontally)

                return movedBothWays
            }
            DIAGONAL -> {
                val movedDiagonally: Boolean = moveData.rowDelta.absoluteValue == moveData.colDelta.absoluteValue

                return !movedDiagonally
            }
            ANY_STRAIGHT -> return VERTICAL_AND_HORIZONTAL.isViolated(moveData) && DIAGONAL.isViolated(moveData)
            L_SHAPE -> {
                val absRowDelta = moveData.rowDelta.absoluteValue
                val absColDelta = moveData.colDelta.absoluteValue
                val movedInL = (absRowDelta == 1 && absColDelta == 2) || (absRowDelta == 2 && absColDelta == 1)
                return !movedInL
            }
        }
    }

    fun isPathBlocked(moveData: MovementData, board: GameBoard): Boolean {
        when(this) {
            VERTICAL_AND_HORIZONTAL, DIAGONAL, ANY_STRAIGHT -> {
                val rowIncrement = moveData.rowDelta.sign
                val colIncrement = moveData.colDelta.sign

                var row = moveData.fromRow + rowIncrement
                var col = moveData.fromCol + colIncrement

                while (!(row == moveData.toRow && col == moveData.toCol)) {
                    val position: String = getStringPosition(row, col)
                    if (board.isOccupied(position)) {
                        return true
                    }

                    row += rowIncrement
                    col += colIncrement
                }

                return false
            }

            L_SHAPE -> return false
        }
    }
}