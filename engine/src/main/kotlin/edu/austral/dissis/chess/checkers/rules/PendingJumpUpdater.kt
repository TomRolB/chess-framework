package edu.austral.dissis.chess.checkers.rules

import edu.austral.dissis.chess.checkers.rules.CheckersPieceState.HAS_PENDING_JUMP
import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.includesTakeAction
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.rules.pieces.PlayUpdater

// TODO: make readable. Could probably create helper classes for Move, Play, etc.

class PendingJumpUpdater: PlayUpdater {
    override fun update(play: Play, board: GameBoard): Play {
//        val (pieceNextTurn, to) =
//            play
//                .extractMoveAction()
//                .let { it.pieceNextTurn to it.to }
//
//        return if (performingMultipleJump(play, pieceNextTurn, board, to)) {
//            play.actions.map {  }
//        }

        // TODO: this is repeated in all updaters. Will probably make a function
        //  updateMoveInPlay() or sth of the sort.
        return play
            .actions
            .map {
                if (it is Move) {
                    replaceByPieceIfValid(play, it, board)
                } else {
                    it
                }
            }
            .let {
                Play(it)
            }
    }

    private fun replaceByPieceIfValid(
        play: Play,
        move: Move,
        board: GameBoard,
    ): Move {
        val (pieceNextTurn, to) = move.let { it.pieceNextTurn to it.to }

        return if (performingMultipleJump(play, pieceNextTurn, board, to)) {
            move.withPiece(pieceNextTurn.withState(HAS_PENDING_JUMP))
        } else {
            move.withPiece(pieceNextTurn.withoutState(HAS_PENDING_JUMP))
        }
    }

    private fun performingMultipleJump(
        play: Play,
        pieceNextTurn: Piece,
        board: GameBoard,
        to: Position,
    ) = play.includesTakeAction() && HasAvailableJumps(pieceNextTurn, board, to).verify()
}