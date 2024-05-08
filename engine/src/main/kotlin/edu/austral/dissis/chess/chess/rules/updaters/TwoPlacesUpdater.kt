package edu.austral.dissis.chess.chess.rules.updaters

import edu.austral.dissis.chess.chess.pieces.ClassicPieceState
import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.rules.pieces.PlayUpdater
import kotlin.math.absoluteValue

class TwoPlacesUpdater : PlayUpdater {
    override fun update(
        play: Play,
        board: GameBoard,
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
        board: GameBoard,
    ): Move {
        return if (movedTwoPlaces(move)) {
            val pieceNextTurn = move.pieceNextTurn.withState(ClassicPieceState.MOVED_TWO_PLACES)
            move.withPiece(pieceNextTurn)
        } else {
            val pieceNextTurn = move.pieceNextTurn.withoutState(ClassicPieceState.MOVED_TWO_PLACES)
            move.withPiece(pieceNextTurn)
        }
    }

    private fun movedTwoPlaces(move: Move): Boolean {
        return (move.to.row - move.from.row).absoluteValue == 2
    }
}