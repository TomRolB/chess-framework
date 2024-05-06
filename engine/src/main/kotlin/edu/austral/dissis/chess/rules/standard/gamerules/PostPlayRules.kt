package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.GameResult
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.rules.RuleChain
import edu.austral.dissis.chess.rules.pieces.king.IsKingChecked

class PostPlayRules(
    val from: Position,
    val to: Position,
    val player: Player,
    val next: RuleChain<Pair<Play, ChessBoard>, GameResult>,
) : RuleChain<Play, GameResult> {
    override fun verify(arg: Play): GameResult {
        val board = arg.execute()

        // King should not be checked
        return if (IsKingChecked(board, player).verify()) {
            GameResult(board, null, EngineResult.POST_PLAY_VIOLATION, "That movement would leave your king checked")
        } else {
            next.verify(arg to board)
        }
    }
}
