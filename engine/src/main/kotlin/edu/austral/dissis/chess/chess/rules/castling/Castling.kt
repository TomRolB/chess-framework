package edu.austral.dissis.chess.chess.rules.castling

import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getRook
import edu.austral.dissis.chess.chess.pieces.ChessPieceState.MOVED
import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.rules.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult

private const val C_COLUMN = 3

private const val G_COLUMN = 7

// TODO: shouldn't be tied to rook
class Castling : PieceRule {
    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        return listOfNotNull(
            getPlayResult(board, position, Position(position.row, C_COLUMN)).play,
            getPlayResult(board, position, Position(position.row, G_COLUMN)).play,
        )
    }

    override fun getPlayResult(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val listener = RookMoveListener()
        val king = board.getPieceAt(from)!!

        return if (castlingRulesVerify(from, to, listener, king, board)) {
            val (rookFrom, rookTo) = listener
            val movedRook = getRook(king.player).withState(MOVED)

            getCastling(from, to, board, king, rookFrom, rookTo, movedRook)
        } else {
            PlayResult(null, "Cannot perform castling")
        }
    }

    private fun castlingRulesVerify(
        from: Position,
        to: Position,
        listener: RookMoveListener,
        king: Piece,
        board: GameBoard,
    ): Boolean {
        return IsToValid(
            from,
            to,
            listener,
            next = CastlingSubRules(king, board, from, to),
        ).verify()
    }

    private fun getCastling(
        from: Position,
        to: Position,
        board: GameBoard,
        king: Piece,
        rookFrom: Position?,
        rookTo: Position?,
        movedRook: Piece,
    ) = PlayResult(
        Play(
            listOf(
                Move(from, to, board, pieceNextTurn = king.withState(MOVED)),
                Move(rookFrom!!, rookTo!!, board, pieceNextTurn = movedRook),
            ),
        ),
        message = "Valid play",
    )
}

data class RookMoveListener(var rookFrom: Position? = null, var rookTo: Position? = null)
