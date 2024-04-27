package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.*
import edu.austral.dissis.chess.rules.RuleChain

class IsPlayValid(
    val board: GameBoard,
    val from: Position,
    val to: Position,
    val next: RuleChain<Play, Pair<Play?, EngineResult>>
) : RuleChain<Piece, Pair<Play?, EngineResult>> {
    override fun verify(arg: Piece): Pair<Play?, EngineResult> {
        val play = arg.rules.getPlayIfValid(board, from, to)

        return if (play == null) null to EngineResult.PIECE_VIOLATION
        else next.verify(play)
    }
}