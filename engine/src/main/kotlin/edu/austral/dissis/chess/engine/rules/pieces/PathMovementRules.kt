package edu.austral.dissis.chess.engine.rules.pieces

import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult
import kotlin.math.sign

// TODO: fix messages
class PathMovementRules(private val increments: Pair<Int, Int>, private val manager: PathManager) : PieceRule {
    // TODO: this method is basically a nonsensical wrapper of another
    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        return getAllPlaysInPath(board, position)
    }

    override fun getPlayResult(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val moveData = MovementData(from, to)

        return if (isViolated(moveData)) {
            PlayResult(null, "Piece cannot move this way")
        } else {
            val play = getPlayIfValid(moveData, board)
            if (play == null) {
                PlayResult(null, "Cannot move there: the path is blocked")
            } else {
                PlayResult(play, "Valid move")
            }
        }
    }

    private fun isViolated(moveData: MovementData): Boolean {
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

    private fun getPlayIfValid(
        moveData: MovementData,
        board: GameBoard,
    ): Play? {
        val rowIncrement = moveData.rowDelta.sign
        val colIncrement = moveData.colDelta.sign

        var row = moveData.fromRow + rowIncrement
        var col = moveData.fromCol + colIncrement

        var currentManager = manager
        var play: Play? = null

        while (!currentManager.isBlocked) {
            val to = Position(row, col)
            val player = board.getPieceAt(moveData.from)!!.player

            val (newManager, newPlay) = currentManager.processPosition(board, moveData.from, to, player)
            currentManager = newManager

            if (!reachedFinalPosition(row, moveData, col)) {
                play = newPlay
                break
            }

            row += rowIncrement
            col += colIncrement
        }

        return play
    }

    private fun reachedFinalPosition(
        row: Int,
        moveData: MovementData,
        col: Int,
    ) = !(row == moveData.toRow && col == moveData.toCol)

    private fun getAllPlaysInPath(
        board: GameBoard,
        position: Position,
    ): List<Play> {
        var (row, col) = position
        val player = board.getPieceAt(position)!!.player

        val (rowIncrement, colIncrement) = increments

        row += rowIncrement
        col += colIncrement

        val result: MutableList<Play> = mutableListOf()

        var currentManager = manager

        while (true) {
            val possibleTo = Position(row, col)

            val (newManager, play) = currentManager.processPosition(board, position, possibleTo, player)

            currentManager = newManager

            if (currentManager.isBlocked) break
            if (play != null) result.add(play)

            row += rowIncrement
            col += colIncrement
        }

        return result
    }
}
