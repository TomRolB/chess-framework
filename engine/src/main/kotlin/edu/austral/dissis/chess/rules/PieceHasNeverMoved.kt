package edu.austral.dissis.chess.rules

import edu.austral.dissis.chess.engine.pieces.MoveDependantPieceType
import edu.austral.dissis.chess.engine.pieces.PieceType

class PieceHasNeverMoved<T : MoveDependantPieceType>(
    val next: RuleChain<PieceType, Boolean>,
) : RuleChain<T, Boolean> {
    override fun verify(arg: T): Boolean {
        return if (
            !arg.hasEverMoved
        ) {
            next.verify(arg)
        } else {
            false
        }

//        return if (
//            pieceType.isInstance(arg.rules) &&
//            !(arg.rules as MoveDependant).hasEverMoved
//        ) {
//            next.verify(arg.rules)
//        } else {
//            false
//        }
    }
}
