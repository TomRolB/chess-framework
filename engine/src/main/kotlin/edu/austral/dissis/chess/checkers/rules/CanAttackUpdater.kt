package edu.austral.dissis.chess.checkers.rules

import edu.austral.dissis.chess.checkers.rules.CheckersPieceState.HAS_PENDING_JUMP
import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Take
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.rules.pieces.PlayUpdater

// TODO: make clear (this applies to any PlayUpdater, honestly)
class CanAttackUpdater: PlayUpdater {
    //TODO: Idea: in post-play, check if the piece has the state CAN_TAKE_ENEMY.
    // If that's the case, should check there's a Take action.
    override fun update(play: Play, board: GameBoard): Play {
        val boardAfterPlay = play.execute()

        return play
            .actions
            .map {
                if (it is Move) {
                    addStateIfValid(it, boardAfterPlay)
                } else {
                    it
                }
            }
            .let {
                Play(it)
            }
    }

    private fun addStateIfValid(
        it: Move,
        boardAfterPlay: GameBoard,
    ): Move {
        return if (anyAttack(it, boardAfterPlay)) {
            val pieceNextTurn = it.pieceNextTurn.withState(HAS_PENDING_JUMP)
            it.withPiece(pieceNextTurn)
        } else {
            val pieceNextTurn = it.pieceNextTurn.withoutState(HAS_PENDING_JUMP)
            it.withPiece(pieceNextTurn)
        }
    }

    private fun anyAttack(
        move: Move,
        boardAfterPlay: GameBoard,
    ) = move.pieceNextTurn
        .getValidPlays(boardAfterPlay, move.to)
        .flatMap { it.actions }
        .any {
            it is Take
        }
}