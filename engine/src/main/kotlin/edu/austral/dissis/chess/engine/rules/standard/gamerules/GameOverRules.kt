package edu.austral.dissis.chess.engine.rules.standard.gamerules

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.WinCondition
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.rules.RuleChain

class GameOverRules(
    val player: Player,
    val winCondition: WinCondition,
) : RuleChain<Pair<Play, GameBoard>, RuleResult> {
    override fun verify(arg: Pair<Play, GameBoard>): RuleResult {
        val (play, board) = arg

        val finalResult = winCondition.getGameResult(board, play, player)
        return finalResult
    }
}
