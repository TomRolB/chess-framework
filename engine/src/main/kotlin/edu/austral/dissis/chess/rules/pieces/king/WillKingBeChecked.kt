package edu.austral.dissis.chess.rules.pieces.king

import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.rules.Rule

class WillKingBeChecked(
    val board: ChessBoard,
    val player: Player,
) : Rule<Boolean> {
    override fun verify(): Boolean {
        return board.getAllPositionsOfPlayer(player, true).all {
            val piece = board.getPieceAt(it)!!
            allMovementsEndInCheck(board, piece, it)
        }
    }

    private fun allMovementsEndInCheck(
        board: ChessBoard,
        piece: Piece,
        position: Position,
    ): Boolean {
        return piece.getValidPlays(board, position).all {
            val futureBoard = it.execute()
            IsKingChecked(futureBoard, piece.player).verify()
        }
    }
}
