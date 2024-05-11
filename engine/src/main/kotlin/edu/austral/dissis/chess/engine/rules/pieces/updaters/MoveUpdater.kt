package edu.austral.dissis.chess.engine.rules.pieces.updaters

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.GameBoard

interface MoveUpdater {
    fun update(
        board: GameBoard,
        play: Play,
        move: Move,
    ): Move
}
