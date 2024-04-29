package edu.austral.dissis.chess.rules

import edu.austral.dissis.chess.engine.MoveDependant
import edu.austral.dissis.chess.engine.PieceRules

class PieceHasNeverMoved<T : MoveDependant>(
    val next: RuleChain<PieceRules, Boolean>,
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
