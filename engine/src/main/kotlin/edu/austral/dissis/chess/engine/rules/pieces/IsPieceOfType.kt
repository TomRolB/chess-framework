package edu.austral.dissis.chess.engine.rules.pieces

import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.pieces.PieceType
import edu.austral.dissis.chess.engine.rules.RuleChain

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
