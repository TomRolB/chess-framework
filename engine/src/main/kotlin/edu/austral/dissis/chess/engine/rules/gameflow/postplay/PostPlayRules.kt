package edu.austral.dissis.chess.engine.rules.gameflow.postplay

import edu.austral.dissis.chess.engine.EngineResult.VALID_MOVE
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.rules.RuleChain

class PostPlayRules(
    val from: Position,
    val to: Position,
    val player: Player,
    val validator: PostPlayValidator,
    val next: RuleChain<Pair<Play, GameBoard>, RuleResult>,
) : RuleChain<Play, RuleResult> {
    override fun verify(arg: Play): RuleResult {
        val board = arg.execute()

        val result = validator.getResult(arg, board, player)
        return if (result.engineResult != VALID_MOVE) result
        else {
            next.verify(arg to board)
        }
    }

}
