package edu.austral.dissis.chess.rules

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult
import edu.austral.dissis.chess.rules.pieces.king.IsKingChecked

class NoSelfCheck(val player: Player, val subRule: PieceRule): PieceRule {
    override fun getValidPlays(board: ChessBoard, position: Position): Iterable<Play> {
        return subRule
            .getValidPlays(board, position)
            .filter {
                !playLeavesKingInChecked(it)
            }
    }

    override fun getPlayIfValid(board: ChessBoard, from: Position, to: Position): PlayResult {
        val result = subRule.getPlayIfValid(board, from, to)
        val play = result.play

        return if (play != null && playLeavesKingInChecked(play)) {
            PlayResult(null, "This movement would leave your king checked")
        }
        else result
    }

    private fun playLeavesKingInChecked(play: Play): Boolean {
        val futureBoard = play.execute()
        return IsKingChecked(futureBoard, player).verify()
    }
}