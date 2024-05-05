package edu.austral.dissis.chess.rules.pieces

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult

class IncrementalMovement(val rowDelta: Int, val colDelta: Int): PieceRule {
    override fun getValidPlays(board: ChessBoard, position: Position): Iterable<Play> {
        val to = Position(position.row + rowDelta, position.col + colDelta)

        return getPlayIfValid(board, position, to)
            .play
            ?.let { listOf(it) }
            ?: emptyList()
    }

    override fun getPlayIfValid(
        board: ChessBoard,
        from: Position,
        to: Position
    ): PlayResult {
        val isRowDeltaValid = (from.row - to.row) == rowDelta
        val isColDeltaValid = (from.col - to.col) == colDelta
        val isPlayValid = isRowDeltaValid && isColDeltaValid

        return PlayResult(
            play = Move(from, to, board).asPlay().takeIf{ isPlayValid },
            message = if (isPlayValid) "Valid play" else "Invalid play"
        )
    }
}