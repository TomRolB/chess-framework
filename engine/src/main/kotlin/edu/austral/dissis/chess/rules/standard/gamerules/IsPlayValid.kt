package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.*
import edu.austral.dissis.chess.rules.RuleChain

class IsPlayValid(
    val board: GameBoard,
    val from: Position,
    val to: Position,
    val next: RuleChain<Play, RuleResult>
) : RuleChain<Piece, RuleResult> {
    override fun verify(arg: Piece): RuleResult {
        val play = arg.rules.getPlayIfValid(board, from, to)

        return if (play == null) RuleResult(board, null, EngineResult.PIECE_VIOLATION)
        else next.verify(play)
    }
}