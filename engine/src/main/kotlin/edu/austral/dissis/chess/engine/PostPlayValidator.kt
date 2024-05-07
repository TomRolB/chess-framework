package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.engine.board.GameBoard

interface PostPlayValidator {
    fun isStateInvalid(
        board: GameBoard,
        player: Player,
    ): Boolean
}
