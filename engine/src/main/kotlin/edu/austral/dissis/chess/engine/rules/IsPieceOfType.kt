package edu.austral.dissis.chess.engine.rules

import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.pieces.PieceType

class IsPieceOfType(
    private val pieceType: PieceType,
    val next: RuleChain<Piece, Boolean>,
) : RuleChain<Piece, Boolean> {
    override fun verify(arg: Piece): Boolean {
        return if (arg.type == pieceType) {
            next.verify(arg)
        } else {
            false
        }
    }
}
