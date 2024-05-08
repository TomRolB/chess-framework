package edu.austral.dissis.chess.engine.rules.standard.gamerules

import edu.austral.dissis.chess.engine.GameData
import edu.austral.dissis.chess.engine.PlayResult
import edu.austral.dissis.chess.engine.PostPlayValidator
import edu.austral.dissis.chess.engine.PrePlayValidator
import edu.austral.dissis.chess.engine.WinCondition
import edu.austral.dissis.chess.engine.rules.RuleChain

class StandardGameRules(
    val prePlayValidator: PrePlayValidator,
    val postPlayValidator: PostPlayValidator,
    val winCondition: WinCondition,
) : RuleChain<GameData, PlayResult> {
    override fun verify(arg: GameData): PlayResult {
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
                prePlayValidator,
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
                                postPlayValidator,
                                next =
                                    GameOverRules(
                                        playerOnTurn,
                                        winCondition,
                                    ),
                            ),
                    ),
            )

        return rules.verify()
    }
}
