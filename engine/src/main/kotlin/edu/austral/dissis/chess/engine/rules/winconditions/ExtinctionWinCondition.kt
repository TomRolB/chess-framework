package edu.austral.dissis.chess.engine.rules.winconditions

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.WinCondition
import edu.austral.dissis.chess.engine.board.GameBoard

class ExtinctionWinCondition : WinCondition {
    override fun getGameResult(
        board: GameBoard,
        play: Play,
        player: Player,
    ): RuleResult {
        return when {
            playerWentExtinct(board, Player.WHITE) ->
                RuleResult(board, null, EngineResult.BLACK_WINS, "Black wins!")
            playerWentExtinct(board, Player.BLACK) ->
                RuleResult(board, null, EngineResult.WHITE_WINS, "White wins!")
            else ->
                RuleResult(board, play, EngineResult.VALID_MOVE, "Valid move")
        }
    }

    private fun playerWentExtinct(
        board: GameBoard,
        player: Player,
    ) = board.getAllPositionsOfPlayer(player).none()
}