package edu.austral.dissis.chess.rules.pieces

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.pieces.ClassicPieceState.MOVED

class HasMovedUpdater : PlayUpdater {
    override fun update(
        play: Play,
        board: ChessBoard,
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
