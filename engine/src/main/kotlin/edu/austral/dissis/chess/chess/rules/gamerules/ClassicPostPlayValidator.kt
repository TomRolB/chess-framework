package edu.austral.dissis.chess.chess.rules.gamerules

import edu.austral.dissis.chess.chess.rules.king.IsKingChecked
import edu.austral.dissis.chess.engine.EngineResult.POST_PLAY_VIOLATION
import edu.austral.dissis.chess.engine.EngineResult.VALID_MOVE
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.rules.gameflow.postplay.PostPlayValidator

class ClassicPostPlayValidator : PostPlayValidator {
    override fun getResult(
        play: Play,
        board: GameBoard,
        player: Player,
    ): RuleResult {
        val invalid = IsKingChecked(board, player).verify()
        return RuleResult(
            board = board,
            play = if (invalid) null else play,
            engineResult = if (invalid) POST_PLAY_VIOLATION else VALID_MOVE,
            message = if (invalid) "That movement would leave your king checked" else "Valid move"
        )
    }
}
