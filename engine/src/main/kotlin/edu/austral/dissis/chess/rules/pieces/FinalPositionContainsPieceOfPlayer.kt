package edu.austral.dissis.chess.rules.pieces

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult

class FinalPositionContainsPieceOfPlayer(val player: Player, val subRule: PieceRule): PieceRule {
    override fun getValidPlays(board: ChessBoard, position: Position): Iterable<Play> {
        return subRule
            .getValidPlays(board, position)
            .filter {
                val finalPosition = extractMove(it).to
                !board.containsPieceOfPlayer(finalPosition, player)
            }
    }

    private fun extractMove(play: Play): Move {
        return play.actions
            .find { it is Move }
            ?.let { it as Move }
            ?: throw IllegalArgumentException(
                "ContainsPieceOfPlayer must be used with a Play containing a Move"
            )
    }

    override fun getPlayIfValid(board: ChessBoard, from: Position, to: Position): PlayResult {
        val playResult = subRule.getPlayIfValid(board, from, to)

        // TODO: Could this be more readable?
        return if (playResult.play != null && !board.containsPieceOfPlayer(to, player)) playResult
        else PlayResult(null, "There must be a $player piece at destination")
    }
}