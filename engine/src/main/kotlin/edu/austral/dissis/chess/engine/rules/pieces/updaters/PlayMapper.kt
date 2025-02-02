package edu.austral.dissis.chess.engine.rules.pieces.updaters

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.GameBoard

interface PlayMapper {
    fun update(
        play: Play,
        board: GameBoard,
    ): Play
}
