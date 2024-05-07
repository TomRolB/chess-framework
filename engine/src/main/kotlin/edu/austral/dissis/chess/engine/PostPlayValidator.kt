package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.engine.board.ChessBoard

interface PostPlayValidator {
    fun isStateInvalid(
        board: ChessBoard,
        player: Player,
    ): Boolean
}
