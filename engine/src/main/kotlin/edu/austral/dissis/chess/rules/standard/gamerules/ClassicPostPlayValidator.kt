package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.PostPlayValidator
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.rules.pieces.king.IsKingChecked

class ClassicPostPlayValidator : PostPlayValidator {
    override fun isStateValid(board: ChessBoard, player: Player): Boolean {
        return IsKingChecked(board, player).verify()
    }
}