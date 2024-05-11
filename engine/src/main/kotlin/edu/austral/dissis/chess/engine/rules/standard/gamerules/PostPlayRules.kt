package edu.austral.dissis.chess.engine.rules.standard.gamerules

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.PostPlayValidator
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.PlayResult
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

        val result = validator.getPostPlayResult(arg, board, player)
        return if (result.play == null) {
            getViolationResult(board, result)
        } else {
            next.verify(arg to board)
        }
    }

    private fun getViolationResult(
        board: GameBoard,
        result: PlayResult,
    ) = RuleResult(board, null, EngineResult.POST_PLAY_VIOLATION, result.message)
}
