package edu.austral.dissis.chess.engine.rules.gameflow.postplay

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.pieces.PlayResult

interface PostPlayValidator {
    fun getPostPlayResult(
        play: Play,
        board: GameBoard,
        player: Player,
    ): PlayResult
}
