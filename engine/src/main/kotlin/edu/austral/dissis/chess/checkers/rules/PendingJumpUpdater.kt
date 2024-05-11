package edu.austral.dissis.chess.checkers.rules

import edu.austral.dissis.chess.checkers.rules.CheckersPieceState.HAS_PENDING_JUMP
import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.includesTakeAction
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.rules.pieces.updaters.MoveUpdater

// TODO: make readable. Could probably create helper classes for Move, Play, etc.
class PendingJumpUpdater : MoveUpdater {
    override fun update(board: GameBoard, play: Play, move: Move): Move {
        val (pieceNextTurn, to) = move.let { it.pieceNextTurn to it.to }

        return if (isPerformingMultipleJump(play, pieceNextTurn, to)) {
            move.withPiece(pieceNextTurn.withState(HAS_PENDING_JUMP))
        } else {
            move.withPiece(pieceNextTurn.withoutState(HAS_PENDING_JUMP))
        }
    }

    private fun isPerformingMultipleJump(
        play: Play,
        pieceNextTurn: Piece,
        to: Position,
    ): Boolean {
        val futureBoard = play.execute()
        return play.includesTakeAction() && HasAvailableJumps(pieceNextTurn, futureBoard, to).verify()
    }
}
