package edu.austral.dissis.chess.rules.castling

import edu.austral.dissis.chess.engine.GameBoard
import edu.austral.dissis.chess.engine.KingPieceRules
import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Piece
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Position
import edu.austral.dissis.chess.engine.RookPieceRules
import edu.austral.dissis.chess.rules.Rule

class Castling(
    private val kingRules: KingPieceRules,
    private val hasEverMoved: Boolean,
    val board: GameBoard,
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

        return if (rules.verify(null)) {
            val (rookFrom, rookTo) = listener
            val movedRook = Piece(kingRules.player, RookPieceRules(kingRules.player, hasEverMoved = true))
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
