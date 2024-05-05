package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.GameData
import edu.austral.dissis.chess.engine.GameResult
import edu.austral.dissis.chess.rules.RuleChain

class StandardGameRules : RuleChain<GameData, GameResult> {
    override fun verify(arg: GameData): GameResult {
        val board = arg.board
        val turnManager = arg.turnManager
        val from = arg.from
        val to = arg.to
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



        return rules.verify()
    }
}
