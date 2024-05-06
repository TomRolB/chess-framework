package edu.austral.dissis.chess.rules

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult

class Promote(val subRule: PieceRule) : PieceRule {
    override fun getValidPlays(
        board: ChessBoard,
        position: Position,
    ): Iterable<Play> {
        TODO("Not yet implemented")
    }

    override fun getPlayIfValid(
        board: ChessBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        TODO("Not yet implemented")
    }
}
