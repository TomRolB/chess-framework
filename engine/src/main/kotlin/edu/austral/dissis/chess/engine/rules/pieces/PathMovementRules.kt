package edu.austral.dissis.chess.engine.rules.pieces

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.not
import edu.austral.dissis.chess.engine.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult
import kotlin.math.sign

// TODO: Would jumping work?
class PathMovementRules(val increments: Pair<Int, Int>) : PieceRule {
    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        return getLineOfPositions(board, position)
            .map {
                Move(position, it, board).asPlay()
            }
    }

    override fun getPlayResult(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val moveData = MovementData(from, to)
        return when {
            isViolated(moveData) -> PlayResult(null, "Piece cannot move this way")
            isPathBlocked(moveData, board) -> PlayResult(null, "Cannot move there: the path is blocked")
            else -> PlayResult(Move(from, to, board).asPlay(), "Valid move")
        }
    }

    fun isViolated(moveData: MovementData): Boolean {
        return !(
            areVectorsParallel(moveData, increments) &&
                doVectorsShareOrientation(moveData, increments)
        )
    }

    private fun areVectorsParallel(
        moveData: MovementData,
        it: Pair<Int, Int>,
    ) = moveData.colDelta * it.first == moveData.rowDelta * it.second

    private fun doVectorsShareOrientation(
        moveData: MovementData,
        increment: Pair<Int, Int>,
    ): Boolean {
        return moveData.rowDelta.sign == increment.first.sign
    }

    fun isPathBlocked(
        moveData: MovementData,
        board: GameBoard,
    ): Boolean {
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

        return anyPieceBlocking
    }

    private fun didNotReachDestination(
        row: Int,
        moveData: MovementData,
        col: Int,
    ) = !(row == moveData.toRow && col == moveData.toCol)

    private fun getLineOfPositions(
        board: GameBoard,
        position: Position,
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
