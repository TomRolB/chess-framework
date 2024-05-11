package edu.austral.dissis.chess.chess.rules.updaters

import edu.austral.dissis.chess.chess.pieces.ChessPieceState.MOVED
import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.rules.pieces.updaters.MoveUpdater

class HasMovedUpdater : MoveUpdater {
    override fun update(board: GameBoard, play: Play, move: Move): Move {
        val pieceNextTurn = move.pieceNextTurn.withState(MOVED)
        return move.withPiece(pieceNextTurn)
    }
}
