package edu.austral.dissis.chess.engine.rules.gameflow.wincondition

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.rules.RuleChain

class GameOverRules(
    val player: Player,
    val winCondition: WinCondition,
) : RuleChain<Pair<Play, GameBoard>, RuleResult> {
    override fun verify(arg: Pair<Play, GameBoard>): RuleResult {
        val (play, board) = arg

        return winCondition.getResult(board, play, player)
    }
}
