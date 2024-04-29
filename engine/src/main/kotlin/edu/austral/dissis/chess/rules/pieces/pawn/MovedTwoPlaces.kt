package edu.austral.dissis.chess.rules.pieces.pawn

import edu.austral.dissis.chess.engine.PawnPieceRules
import edu.austral.dissis.chess.engine.PieceRules
import edu.austral.dissis.chess.rules.RuleChain

class MovedTwoPlaces: RuleChain<PawnPieceRules, Boolean> {
    override fun verify(arg: PawnPieceRules): Boolean {
        return arg.hasJustMovedTwoPlaces
    }
}