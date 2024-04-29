package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.GameBoard
import edu.austral.dissis.chess.engine.Piece
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Position
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.rules.RuleChain

class IsPlayValid(
    val board: GameBoard,
    val from: Position,
    val to: Position,
    val next: RuleChain<Play, RuleResult>,
) : RuleChain<Piece, RuleResult> {
    override fun verify(arg: Piece): RuleResult {
        val play = arg.rules.getPlayIfValid(board, from, to)

        return if (play == null) {
            RuleResult(board, null, EngineResult.PIECE_VIOLATION, "Cannot move piece that way")
        } else {
            next.verify(play)
        }
    }
}
