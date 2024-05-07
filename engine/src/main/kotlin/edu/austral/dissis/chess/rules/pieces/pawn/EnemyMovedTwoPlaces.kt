package edu.austral.dissis.chess.rules.pieces.pawn

import edu.austral.dissis.chess.engine.pieces.ClassicPieceState.MOVED_TWO_PLACES
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.rules.RuleChain

class EnemyMovedTwoPlaces : RuleChain<Piece, Boolean> {
    override fun verify(arg: Piece): Boolean {
        return arg.hasState(MOVED_TWO_PLACES)
    }
}
