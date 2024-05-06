package edu.austral.dissis.chess.rules

import edu.austral.dissis.chess.engine.pieces.Piece

class PieceHasNeverMoved(
    val next: RuleChain<Piece, Boolean>,
) : RuleChain<Piece, Boolean> {
    override fun verify(arg: Piece): Boolean {
        return if (!arg.hasState("moved")) {
            next.verify(arg)
        } else {
            false
        }
    }
}
