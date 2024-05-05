package edu.austral.dissis.chess.rules.pieces

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult

class NoPieceAtFinalPosition(val subRule: PieceRule): PieceRule {
    override fun getValidPlays(board: ChessBoard, position: Position): Iterable<Play> {
        return subRule
            .getValidPlays(board, position)
            .filter {
                val finalPosition = extractMove(it).to
                !board.isOccupied(finalPosition)
            }
    }

    private fun extractMove(play: Play): Move {
        return play.actions
            .find { it is Move }
            ?.let { it as Move }
            ?: throw IllegalArgumentException(
                "NoPieceAtDestination must be used with a Play containing a Move"
            )
    }

    override fun getPlayIfValid(board: ChessBoard, from: Position, to: Position): PlayResult {
        val playResult = subRule.getPlayIfValid(board, from, to)

        // TODO: Could this be more readable?
        return if (playResult.play != null && !board.isOccupied(to)) playResult
        else PlayResult(null, "There is a piece blocking that position")
    }
}