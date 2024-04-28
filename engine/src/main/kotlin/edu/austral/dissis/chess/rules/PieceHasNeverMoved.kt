package edu.austral.dissis.chess.rules

import edu.austral.dissis.chess.engine.MoveDependant
import edu.austral.dissis.chess.engine.Piece
import edu.austral.dissis.chess.engine.PieceRules

class PieceHasNeverMoved<T : MoveDependant>(
    val pieceType: Class<T>,
    val next: RuleChain<PieceRules, Boolean>,
) : RuleChain<Piece, Boolean> {
    override fun verify(arg: Piece): Boolean {
        return if (
            pieceType.isInstance(arg.rules) &&
            !(arg.rules as MoveDependant).hasEverMoved
        ) {
            next.verify(arg.rules)
        } else {
            false
        }

//        return piece.rules
//            .takeIf {
//                pieceType.isInstance(piece.rules)
//                && !(piece.rules as MoveDependant).hasEverMoved
//            }
//            ?.let { next.verify(piece.rules) }
    }
}
