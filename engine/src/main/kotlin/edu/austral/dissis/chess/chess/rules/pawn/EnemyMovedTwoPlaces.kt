package edu.austral.dissis.chess.chess.rules.pawn

import edu.austral.dissis.chess.chess.pieces.ClassicPieceState.MOVED_TWO_PLACES
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.rules.RuleChain

class EnemyMovedTwoPlaces : RuleChain<Piece, Boolean> {
    override fun verify(arg: Piece): Boolean {
        return arg.hasState(MOVED_TWO_PLACES)
    }
}
