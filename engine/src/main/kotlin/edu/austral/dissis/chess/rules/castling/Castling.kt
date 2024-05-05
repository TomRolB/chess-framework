package edu.austral.dissis.chess.rules.castling

import edu.austral.dissis.chess.engine.ClassicMoveType
import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.King
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.pieces.getRook
import edu.austral.dissis.chess.rules.Rule
import edu.austral.dissis.chess.rules.pieces.MovedUpdater
import edu.austral.dissis.chess.rules.pieces.PathMovementRules

class Castling(
    private val kingRules: King,
    private val hasEverMoved: Boolean,
    val board: ChessBoard,
    val from: Position,
    val to: Position,
) : Rule<Play?> {
    override fun verify(): Play? {
        val listener = RookMoveListener()

        val rules =
            IsToValid(
                from,
                to,
                listener,
                next = CastlingSubRules(kingRules, hasEverMoved, board, from, to),
            )

        return if (rules.verify()) {
            val (rookFrom, rookTo) = listener
            val movedRook = getRook(kingRules.player).withState("moved")

            Play(
                listOf(
                    Move(from, to, board, pieceNextTurn = kingRules.asMoved()),
                    Move(rookFrom!!, rookTo!!, board, pieceNextTurn = movedRook),
                ),
            )
        } else {
            null
        }
    }
}

data class RookMoveListener(var rookFrom: Position? = null, var rookTo: Position? = null)
