package edu.austral.dissis.chess.engine.rules.gameflow.play

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.InvalidPlay
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.pieces.ValidPlay
import edu.austral.dissis.chess.engine.rules.RuleChain

class IsPlayValid(
    val board: GameBoard,
    val from: Position,
    val to: Position,
    val next: RuleChain<Play, RuleResult>,
) : RuleChain<Piece, RuleResult> {
    override fun verify(arg: Piece): RuleResult {
        val result = arg.getPlayResult(board, from, to)

        return if (result is InvalidPlay) {
            RuleResult(board, null, EngineResult.PIECE_VIOLATION, result.message)
        } else {
            next.verify((result as ValidPlay).play)
        }
    }
}
