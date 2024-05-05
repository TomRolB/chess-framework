package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import kotlin.math.absoluteValue
import kotlin.math.sign

/**
* So, why implementing an interface with an enum?

* Many MoveTypes share the exact same functionality in some cases, but differ in others.

* For instance, see how isViolated() is different for each constant, while
* getPossiblePositions() has overlapping constants in the 'when' statement.

* Besides, the reason for having an interface is that MoveType is not sealed
* (different MoveTypes may be defined apart from the ones below)
*/

enum class ClassicMoveType : MoveType {
    VERTICAL_AND_HORIZONTAL,
    DIAGONAL,
    ANY_STRAIGHT_LINE,
    L_SHAPED,
    ADJACENT_SQUARE,
    ;

    companion object {
        val VH = listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1)
        val D = listOf(1 to 1, -1 to 1, 1 to -1, 1 to -1)
        val L = listOf(2 to 1, 1 to 2, -2 to 1, -1 to 2, 2 to -1, 1 to -2, -2 to -1, -1 to -2)
    }

    override fun isViolated(moveData: MovementData): Boolean {
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
                val noStraightLine =
                    VERTICAL_AND_HORIZONTAL.isViolated(moveData) &&
                        DIAGONAL.isViolated(moveData)

                noStraightLine
            }
            L_SHAPED -> {
                val absRowDelta = moveData.rowDelta.absoluteValue
                val absColDelta = moveData.colDelta.absoluteValue
                val movedInL =
                    (absRowDelta == 1 && absColDelta == 2) ||
                        (absRowDelta == 2 && absColDelta == 1)

                !movedInL
            }
            ADJACENT_SQUARE -> {
                val movedToAdjSquare =
                    moveData.rowDelta.absoluteValue <= 1 &&
                        moveData.colDelta.absoluteValue <= 1

                !movedToAdjSquare
            }
        }
    }

    override fun isPathBlocked(
        moveData: MovementData,
        board: ChessBoard,
    ): Boolean {
        return when (this) {
            VERTICAL_AND_HORIZONTAL, DIAGONAL, ANY_STRAIGHT_LINE -> {
                val rowIncrement = moveData.rowDelta.sign
                val colIncrement = moveData.colDelta.sign

                var row = moveData.fromRow + rowIncrement
                var col = moveData.fromCol + colIncrement

                var anyPieceBlocking = false

                while (didNotReachDestination(row, moveData, col)) {
                    val position = Position(row, col)
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

    private fun didNotReachDestination(
        row: Int,
        moveData: MovementData,
        col: Int,
    ) = !(row == moveData.toRow && col == moveData.toCol)

    private fun getIncrements(): Iterable<Pair<Int, Int>> {
        return when (this) {
            VERTICAL_AND_HORIZONTAL -> VH
            DIAGONAL -> D
            ANY_STRAIGHT_LINE, ADJACENT_SQUARE -> {
                VERTICAL_AND_HORIZONTAL.getIncrements() + DIAGONAL.getIncrements()
            }
            L_SHAPED -> L
        }
    }

    override fun getPossiblePositions(
        board: ChessBoard,
        position: Position,
    ): Iterable<Position> {
        return when (this) {
            VERTICAL_AND_HORIZONTAL, DIAGONAL, ANY_STRAIGHT_LINE -> {
                this.getIncrements()
                    .flatMap { getLineOfPositions(board, position, it) }
            }
            L_SHAPED, ADJACENT_SQUARE -> {
                val (row, col) = position
                val player = board.getPieceAt(position)!!.player

                this.getIncrements()
                    .map { Position(row + it.first, col + it.second) }
                    .filter {
                        board.positionExists(it) &&
                            !board.containsPieceOfPlayer(it, player)
                    }
            }
        }
    }

    private fun getLineOfPositions(
        board: ChessBoard,
        position: Position,
        increments: Pair<Int, Int>,
    ): List<Position> {
        var (row, col) = position
        val player = board.getPieceAt(position)!!.player

        val rowIncrement = increments.first
        val colIncrement = increments.second

        row += rowIncrement
        col += colIncrement

        val result: MutableList<Position> = mutableListOf()

        var blocked = false
        while (!blocked) {
            val reachablePos = Position(row, col)

            // We'll only add the position if it exists, does not hold
            // a piece of the same player and does not imply a move
            // that would leave our king checked
            if (!board.positionExists(reachablePos) ||
                board.containsPieceOfPlayer(reachablePos, player)
            ) {
                blocked = true
            } else {
                result.addLast(reachablePos)

                // We check if there is an enemy piece after adding, since
                // it is possible to take that piece, but we must then
                // break, as the rest of the path is blocked by it
                if (board.containsPieceOfPlayer(reachablePos, !player)) blocked = true

                row += rowIncrement
                col += colIncrement
            }
        }

        return result
    }
}
