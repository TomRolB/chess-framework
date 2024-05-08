package edu.austral.dissis.chess.chess.rules

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.chess.pieces.ClassicPieceState.MOVED
import edu.austral.dissis.chess.rules.pieces.PlayUpdater

class HasMovedUpdater : PlayUpdater {
    override fun update(
        play: Play,
        board: GameBoard,
    ): Play {
        // TODO: make clear

        return play
            .actions
            .map {
                if (it is Move) {
                    val pieceNextTurn = it.pieceNextTurn.withState(MOVED)
                    it.withPiece(pieceNextTurn)
                } else {
                    it
                }
            }
            .let {
                Play(it)
            }
    }
}
