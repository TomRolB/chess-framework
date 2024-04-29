package edu.austral.dissis.chess.rules.pieces.pawn

import edu.austral.dissis.chess.engine.PawnPieceRules
import edu.austral.dissis.chess.rules.RuleChain

class EnemyMovedTwoPlaces : RuleChain<PawnPieceRules, Boolean> {
    override fun verify(arg: PawnPieceRules): Boolean {
        return arg.hasJustMovedTwoPlaces
    }
}
