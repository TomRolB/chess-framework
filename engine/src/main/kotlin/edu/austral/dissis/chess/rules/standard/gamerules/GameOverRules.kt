package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.*
import edu.austral.dissis.chess.rules.RuleChain

class GameOverRules(val player: Player) : RuleChain<Pair<Play, GameBoard>, RuleResult> {
    override fun verify(arg: Pair<Play, GameBoard>): RuleResult {
        val (play, board) = arg

        val enemyState = KingPieceRules.getPlayerState(board, !player)

        val engineResult = when (enemyState) {
            PlayerState.CHECKMATE -> winResult(player)
            PlayerState.STALEMATE -> EngineResult.TIE
            else -> EngineResult.VALID_MOVE
        }

        return RuleResult(board, play, engineResult)
    }

    private fun winResult(player: Player): EngineResult {
        when (player) {
            Player.WHITE -> EngineResult.WHITE_WINS
            Player.BLACK -> EngineResult.BLACK_WINS
        }
    }
}