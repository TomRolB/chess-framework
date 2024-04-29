package edu.austral.dissis.chess.rules

import edu.austral.dissis.chess.engine.GameBoard
import edu.austral.dissis.chess.engine.Piece
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.Position

class WillKingBeChecked(
    val board: GameBoard,
    val player: Player,
): Rule<Boolean> {
    override fun verify(): Boolean {
        return board.getAllPositionsOfPlayer(player, true).all {
            val piece = board.getPieceAt(it)!!
            allMovementsEndInCheck(board, piece, it)
        }
    }

    private fun allMovementsEndInCheck(
        board: GameBoard,
        piece: Piece,
        position: Position,
    ): Boolean {
        return piece.rules.getValidPlays(board, position).all {
            val futureBoard = it.execute()
            IsKingChecked(futureBoard, piece.player).verify()
        }
    }
}