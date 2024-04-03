package edu.austral.dissis.chess.engine

import kotlin.math.sign

enum class Heuristics {
    PLUS_SYMBOL;

    fun isViolated(moveData: MovementData): Boolean {
        when(this) {
            PLUS_SYMBOL -> {
                val movedVertically: Boolean = (moveData.rowDelta != 0)
                val movedHorizontally: Boolean = (moveData.colDelta != 0)
                val stayedOnSite = (!movedVertically && !movedHorizontally)
                val movedBothWays = (movedVertically && movedHorizontally)

                return stayedOnSite || movedBothWays
            }
        }
    }

    fun isPathBlocked(moveData: MovementData, board: GameBoard, player: Player): Boolean {
        when(this) {
            PLUS_SYMBOL -> {
                    //TODO("Will probably also hold X_SYMBOL and OCTOGRAM")
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
        }
    }
}

