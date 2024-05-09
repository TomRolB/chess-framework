package edu.austral.dissis.chess.chess.rules.updaters

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.rules.pieces.PlayUpdater

class PromotionUpdater(val pieceNextTurn: Piece) : PlayUpdater {
    override fun update(
        play: Play,
        board: GameBoard,
    ): Play {
        // TODO: make clear

        return play
            .actions
            .map {
                if (it is Move) {
                    replaceByQueenIfValid(it, board)
                } else {
                    it
                }
            }
            .let {
                Play(it)
            }
    }

    private fun replaceByQueenIfValid(
        move: Move,
        board: GameBoard,
    ): Move {
        val player = move.pieceNextTurn.player

        return if (board.isPositionOnUpperLimit(move.to, move.pieceNextTurn.player)) {
            move.withPiece(pieceNextTurn)
        } else {
            move
        }
    }
}
