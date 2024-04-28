package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.GameData
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.rules.RuleChain

class StandardGameRules : RuleChain<GameData, RuleResult> {
    override fun verify(arg: GameData): RuleResult {
        val (board, turnManager, from, to) = arg
        val playerOnTurn = turnManager.getTurn()

        val rules =
            PrePlayRules(
                board,
                from,
                to,
                playerOnTurn,
                next =
                    IsPlayValid(
                        board,
                        from,
                        to,
                        next =
                            PostPlayRules(
                                from,
                                to,
                                playerOnTurn,
                                next = GameOverRules(playerOnTurn),
                            ),
                    ),
            )

        return rules.verify(null)
    }
}
