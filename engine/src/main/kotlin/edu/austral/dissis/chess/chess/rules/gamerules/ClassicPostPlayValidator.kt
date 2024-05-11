package edu.austral.dissis.chess.chess.rules.gamerules

import edu.austral.dissis.chess.chess.rules.king.IsKingChecked
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.PostPlayValidator
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.pieces.PlayResult

class ClassicPostPlayValidator : PostPlayValidator {
    override fun getPostPlayResult(
        play: Play,
        board: GameBoard,
        player: Player,
    ): PlayResult {
        return if (IsKingChecked(board, player).verify()) {
            PlayResult(null, "That movement would leave your king checked")
        } else {
            PlayResult(play, "Valid play")
        }
    }
}
