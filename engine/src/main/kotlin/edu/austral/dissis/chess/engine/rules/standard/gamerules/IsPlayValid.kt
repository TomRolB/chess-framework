package edu.austral.dissis.chess.engine.rules.standard.gamerules

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.PlayResult
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.rules.RuleChain

class IsPlayValid(
    val board: GameBoard,
    val from: Position,
    val to: Position,
    val next: RuleChain<Play, PlayResult>,
) : RuleChain<Piece, PlayResult> {
    override fun verify(arg: Piece): PlayResult {
        val (play, message) = arg.getPlayResult(board, from, to)

        return if (play == null) {
            PlayResult(board, null, EngineResult.PIECE_VIOLATION, message)
        } else {
            next.verify(play)
        }
    }
}
