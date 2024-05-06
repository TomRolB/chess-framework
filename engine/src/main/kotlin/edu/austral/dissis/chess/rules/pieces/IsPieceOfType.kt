package edu.austral.dissis.chess.rules.pieces

import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.rules.RuleChain

class IsPieceOfType(
    private val pieceType: String,
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
