package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.GameResult
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.rules.RuleChain

class IsPlayValid(
    val board: ChessBoard,
    val from: Position,
    val to: Position,
    val next: RuleChain<Play, GameResult>,
) : RuleChain<Piece, GameResult> {
    override fun verify(arg: Piece): GameResult {
        val (play, message) = arg.type.getPlayIfValid(board, from, to)

        return if (play == null) {
            GameResult(board, null, EngineResult.PIECE_VIOLATION, message)
        } else {
            next.verify(play)
        }
    }
}
