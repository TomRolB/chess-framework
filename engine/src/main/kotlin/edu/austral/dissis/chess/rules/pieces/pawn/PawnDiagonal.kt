package edu.austral.dissis.chess.rules.pieces.pawn

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.not
import edu.austral.dissis.chess.engine.pieces.Pawn
import edu.austral.dissis.chess.engine.pieces.Pawn.PawnState
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.rules.Rule

class PawnDiagonal(
    private val board: ChessBoard,
    private val moveData: MovementData,
    private val player: Player,
) : Rule<Play?> {
    override fun verify(): Play? {
        return if (board.containsPieceOfPlayer(moveData.to, !player)) {
            //TODO: See if this line could be more simple.
            // Or maybe there isn't so much inefficiency in getting the piece again
            val type = board.getPieceAt(moveData.from)!!.type
            Move(
                moveData.from,
                moveData.to,
                board,
                pieceNextTurn = Piece(type, player, Pawn(player, PawnState.MOVED)),
            ).asPlay()
        } else {
            EnPassant(board, moveData, !player).verify()
        }
    }
}
