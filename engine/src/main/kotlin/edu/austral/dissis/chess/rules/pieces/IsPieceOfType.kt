package edu.austral.dissis.chess.rules.pieces

import edu.austral.dissis.chess.engine.Piece
import edu.austral.dissis.chess.engine.PieceRules
import edu.austral.dissis.chess.rules.RuleChain

class IsPieceOfType<T: PieceRules>(
    val pieceType: Class<T>,
    val next: RuleChain<T, Boolean>
): RuleChain<Piece, Boolean> {
    override fun verify(arg: Piece): Boolean {
        return if (
            pieceType.isInstance(arg.rules)
        ) {
            next.verify(pieceType.cast(arg.rules))
        } else {
            false
        }
    }
}