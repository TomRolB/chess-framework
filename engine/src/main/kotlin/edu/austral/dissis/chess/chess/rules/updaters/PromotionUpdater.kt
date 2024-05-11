package edu.austral.dissis.chess.chess.rules.updaters

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.rules.pieces.updaters.MoveUpdater

// TODO: Passing a piece instance would make all piece promote to
//  the same 'pieceNextTurn'. Should copy the piece or create it
//  in the moment.
class PromotionUpdater(val pieceNextTurn: Piece) : MoveUpdater {
    override fun update(board: GameBoard, play: Play, move: Move): Move {
        return if (board.isPositionOnUpperLimit(move.to, move.pieceNextTurn.player)) {
            move.withPiece(pieceNextTurn.clone())
        } else {
            move
        }
    }
}
