package edu.austral.dissis.chess.rules.pieces

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.ChessBoard
import kotlin.math.absoluteValue

class TwoPlacesUpdater : PlayUpdater {
    override fun update(
        play: Play,
        board: ChessBoard,
    ): Play {
        // TODO: make clear

        return play
            .actions
            .map {
                if (it is Move) {
                    removeOrAddState(it, board)
                } else {
                    it
                }
            }
            .let {
                Play(it)
            }
    }

    private fun removeOrAddState(
        move: Move,
        board: ChessBoard,
    ): Move {
        return if (movedTwoPlaces(move)) {
            val pieceNextTurn = move.pieceNextTurn.withState("moved two places")
            move.withPiece(pieceNextTurn)
        } else {
            val pieceNextTurn = move.pieceNextTurn.withoutState("moved two places")
            move.withPiece(pieceNextTurn)
        }
    }

    private fun movedTwoPlaces(move: Move): Boolean {
        return (move.to.row - move.from.row).absoluteValue == 2
    }
}
