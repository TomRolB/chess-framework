package edu.austral.dissis.chess.rules

import edu.austral.dissis.chess.engine.pieces.ClassicPieceState.MOVED
import edu.austral.dissis.chess.engine.pieces.Piece

class PieceHasNeverMoved(
    val next: RuleChain<Piece, Boolean>,
) : RuleChain<Piece, Boolean> {
    override fun verify(arg: Piece): Boolean {
        return if (!arg.hasState(MOVED)) {
            next.verify(arg)
        } else {
            false
        }
    }
}
