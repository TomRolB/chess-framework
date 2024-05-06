package edu.austral.dissis.chess.rules.pieces

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.pieces.getQueen

class PromotionUpdater : PlayUpdater {
    override fun update(
        play: Play,
        board: ChessBoard,
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
        board: ChessBoard,
    ): Move {
        val player = move.pieceNextTurn.player

        return if (board.isPositionOnUpperLimit(move.to, move.pieceNextTurn.player)) {
            val pieceNextTurn = getQueen(player)
            move.withPiece(pieceNextTurn)
        } else {
            move
        }
    }
}
