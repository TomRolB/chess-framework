package edu.austral.dissis.chess.rules.castling

import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.Rook
import edu.austral.dissis.chess.rules.ContainsPieceOfPlayer
import edu.austral.dissis.chess.rules.PieceHasNeverMoved
import edu.austral.dissis.chess.rules.Rule
import edu.austral.dissis.chess.rules.Succeed
import edu.austral.dissis.chess.rules.pieces.IsPieceOfType

class IsRookAvailable(
    private val board: ChessBoard,
    private val rookPos: Position,
    private val player: Player,
) : Rule<Boolean> {
    override fun verify(): Boolean {
        return ContainsPieceOfPlayer(
            board,
            rookPos,
            player,
            next =
                PieceHasNeverMoved(
                    next = Succeed(),
                ),
        ).verify()
    }
}
