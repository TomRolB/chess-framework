package edu.austral.dissis.chess.rules.castling

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult
import edu.austral.dissis.chess.engine.pieces.getRook

private const val C_COLUMN = 3

private const val G_COLUMN = 7

// TODO: shouldn't be tied to rook
class Castling : PieceRule {
    override fun getValidPlays(
        board: ChessBoard,
        position: Position,
    ): Iterable<Play> {
        return listOfNotNull(
            getPlayResult(board, position, Position(position.row, C_COLUMN)).play,
            getPlayResult(board, position, Position(position.row, G_COLUMN)).play,
        )
    }

    override fun getPlayResult(
        board: ChessBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val listener = RookMoveListener()
        val king = board.getPieceAt(from)!!

        val rules =
            IsToValid(
                from,
                to,
                listener,
                next = CastlingSubRules(king, board, from, to),
            )

        return if (rules.verify()) {
            val (rookFrom, rookTo) = listener
            val movedRook = getRook(king.player).withState("moved")

            PlayResult(
                Play(
                    listOf(
                        Move(from, to, board, pieceNextTurn = king.withState("moved")),
                        Move(rookFrom!!, rookTo!!, board, pieceNextTurn = movedRook),
                    ),
                ),
                "Valid play",
            )
        } else {
            PlayResult(null, "Cannot perform castling")
        }
    }
}

data class RookMoveListener(var rookFrom: Position? = null, var rookTo: Position? = null)
