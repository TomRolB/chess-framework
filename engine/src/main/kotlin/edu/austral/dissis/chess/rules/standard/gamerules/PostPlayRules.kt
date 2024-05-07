package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.PlayResult
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.PostPlayValidator
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.rules.RuleChain

class PostPlayRules(
    val from: Position,
    val to: Position,
    val player: Player,
    val validator: PostPlayValidator,
    val next: RuleChain<Pair<Play, ChessBoard>, PlayResult>,
) : RuleChain<Play, PlayResult> {
    override fun verify(arg: Play): PlayResult {
        val board = arg.execute()

        // King should not be checked
        return if (validator.isStateInvalid(board, player)) {
            PlayResult(board, null, EngineResult.POST_PLAY_VIOLATION, "That movement would leave your king checked")
        } else {
            next.verify(arg to board)
        }
    }
}
