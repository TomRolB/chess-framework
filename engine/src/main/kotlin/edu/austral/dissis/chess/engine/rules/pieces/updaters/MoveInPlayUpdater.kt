package edu.austral.dissis.chess.engine.rules.pieces.updaters

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.GameBoard

class MoveInPlayUpdater(val moveUpdater: MoveUpdater) : PlayUpdater {
    override fun update(
        play: Play,
        board: GameBoard,
    ): Play {
        return play
            .actions
            .map {
                if (it is Move) {
                    moveUpdater.update(board, play, it)
                } else {
                    it
                }
            }
            .let {
                Play(it)
            }
    }
}
