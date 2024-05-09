package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.pieces.PlayResult

interface PostPlayValidator {
    fun getPostPlayResult(
        play: Play,
        board: GameBoard,
        player: Player,
    ): PlayResult
}
