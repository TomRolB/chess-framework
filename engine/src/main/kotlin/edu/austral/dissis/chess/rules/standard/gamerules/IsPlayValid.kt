package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.rules.RuleChain

class IsPlayValid(
    val board: GameBoard,
    val from: Position,
    val to: Position,
    val next: RuleChain<Play, RuleResult>,
) : RuleChain<Piece, RuleResult> {
    override fun verify(arg: Piece): RuleResult {
        val (play, message) = arg.type.getPlayIfValid(board, from, to)

        return if (play == null) {
            RuleResult(board, null, EngineResult.PIECE_VIOLATION, message)
        } else {
            next.verify(play)
        }
    }
}
