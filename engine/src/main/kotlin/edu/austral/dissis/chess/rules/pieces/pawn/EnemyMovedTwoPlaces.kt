package edu.austral.dissis.chess.rules.pieces.pawn

import edu.austral.dissis.chess.engine.pieces.Pawn
import edu.austral.dissis.chess.rules.RuleChain

class EnemyMovedTwoPlaces : RuleChain<Pawn, Boolean> {
    override fun verify(arg: Pawn): Boolean {
        return arg.hasJustMovedTwoPlaces
    }
}
