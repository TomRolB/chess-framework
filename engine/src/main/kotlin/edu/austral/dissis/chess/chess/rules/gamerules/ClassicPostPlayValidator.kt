package edu.austral.dissis.chess.chess.rules.gamerules

import edu.austral.dissis.chess.chess.rules.king.IsKingChecked
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.PostPlayValidator
import edu.austral.dissis.chess.engine.board.GameBoard

class ClassicPostPlayValidator : PostPlayValidator {
    override fun isStateInvalid(
        board: GameBoard,
        player: Player,
    ): Boolean {
        return IsKingChecked(board, player).verify()
    }
}
