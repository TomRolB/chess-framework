package edu.austral.dissis.chess.rules.pieces

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult

class Combined(val first: PieceRule, val second: PieceRule): PieceRule {
    override fun getValidPlays(board: ChessBoard, position: Position): Iterable<Play> {
        return first.getValidPlays(board, position) + second.getValidPlays(board, position)
    }

    override fun getPlayIfValid(board: ChessBoard, from: Position, to: Position): PlayResult {
        val firstResult = first.getPlayIfValid(board, from, to)

        return if (firstResult.play != null) firstResult
        // TODO: consider actually returning a combination of failure messages
        else second.getPlayIfValid(board, from, to)
    }
}