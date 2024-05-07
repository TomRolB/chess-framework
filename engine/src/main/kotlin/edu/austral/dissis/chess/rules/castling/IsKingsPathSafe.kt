package edu.austral.dissis.chess.rules.castling

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.ClassicPieceState.MOVED
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.rules.Rule
import edu.austral.dissis.chess.rules.pieces.king.IsKingChecked

private const val C_COLUMN = 3

private const val D_COLUMN = 4

private const val F_COLUMN = 6

class IsKingsPathSafe(
    private val king: Piece,
    val from: Position,
    val to: Position,
    val board: ChessBoard,
) : Rule<Boolean> {
    override fun verify(): Boolean {
        val positions =
            if (to.col == C_COLUMN) {
                listOf(Position(from.row, D_COLUMN), to)
            } else {
                listOf(Position(from.row, F_COLUMN), to)
            }

        return positions
            .map { Move(from, it, board, pieceNextTurn = king.withState(MOVED)).execute() }
            .all { futureBoard -> !IsKingChecked(futureBoard, king.player).verify() }
    }
}
