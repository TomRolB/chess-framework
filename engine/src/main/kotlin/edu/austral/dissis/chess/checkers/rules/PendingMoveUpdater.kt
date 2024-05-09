package edu.austral.dissis.chess.checkers.rules

import edu.austral.dissis.chess.checkers.rules.CheckersPieceState.HAS_PENDING_MOVE
import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Take
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.rules.pieces.PlayUpdater

// TODO: make clear (this applies to any PlayUpdater, honestly)
class PendingMoveUpdater: PlayUpdater {
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
            val pieceNextTurn = it.pieceNextTurn.withState(HAS_PENDING_MOVE)
            it.withPiece(pieceNextTurn)
        } else it
    }

    private fun anyAttack(
        it: Move,
        boardAfterPlay: GameBoard,
    ) = it.pieceNextTurn
        .getValidPlays(boardAfterPlay, it.to)
        .flatMap { it.actions }
        .any {
            it is Take
        }
}