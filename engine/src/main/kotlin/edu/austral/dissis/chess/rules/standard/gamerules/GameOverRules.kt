package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.GameBoard
import edu.austral.dissis.chess.engine.KingPieceRules
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.PlayerState
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.not
import edu.austral.dissis.chess.rules.RuleChain

class GameOverRules(val player: Player) : RuleChain<Pair<Play, GameBoard>, RuleResult> {
    override fun verify(arg: Pair<Play, GameBoard>): RuleResult {
        val (play, board) = arg

        val enemyState = KingPieceRules.getPlayerState(board, !player)

        val (engineResult, message) =
            when (enemyState) {
                PlayerState.CHECKMATE -> winResult(player)
                PlayerState.STALEMATE -> EngineResult.TIE to "It's a tie!"
                else -> EngineResult.VALID_MOVE to "Successfully moved"
            }

        return RuleResult(board, play, engineResult, message)
    }

    private fun winResult(player: Player): Pair<EngineResult, String> {
        return when (player) {
            Player.WHITE -> EngineResult.WHITE_WINS to "White wins!"
            Player.BLACK -> EngineResult.BLACK_WINS to "Black wins!"
        }
    }
}
