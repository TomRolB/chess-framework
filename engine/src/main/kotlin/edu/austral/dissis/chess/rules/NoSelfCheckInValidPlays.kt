package edu.austral.dissis.chess.rules

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult
import edu.austral.dissis.chess.rules.pieces.king.IsKingChecked

class NoSelfCheckInValidPlays(val player: Player, val subRule: PieceRule) : PieceRule {
    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        return subRule
            .getValidPlays(board, position)
            .filter {
                !playLeavesKingChecked(it)
            }
    }

    override fun getPlayResult(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        return subRule.getPlayResult(board, from, to)
    }

    private fun playLeavesKingChecked(play: Play): Boolean {
        val futureBoard = play.execute()
        return IsKingChecked(futureBoard, player).verify()
    }
}
