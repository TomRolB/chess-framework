package edu.austral.dissis.chess.chess.rules.updaters

import edu.austral.dissis.chess.chess.pieces.ChessPieceState.MOVED_TWO_PLACES
import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.rules.pieces.updaters.MoveUpdater
import kotlin.math.absoluteValue

class MovedTwoPlacesUpdater : MoveUpdater {
    override fun update(
        board: GameBoard,
        play: Play,
        move: Move,
    ): Move {
        return if (movedTwoPlaces(move)) {
            val pieceNextTurn = move.pieceNextTurn.withState(MOVED_TWO_PLACES)
            move.withPiece(pieceNextTurn)
        } else {
            val pieceNextTurn = move.pieceNextTurn.withoutState(MOVED_TWO_PLACES)
            move.withPiece(pieceNextTurn)
        }
    }

    private fun movedTwoPlaces(move: Move): Boolean {
        return (move.to.row - move.from.row).absoluteValue == 2
    }
}
