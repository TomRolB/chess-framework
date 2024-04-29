package edu.austral.dissis.chess.rules.pieces.pawn

import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.rules.Rule

class PawnValidMove(
    private val board: ChessBoard,
    private val from: Position,
    private val to: Position,
    private val player: Player,
    private val hasEverMoved: Boolean,
) : Rule<Play?> {
    override fun verify(): Play? {
        // In this case, moveData is always created from
        // WHITE's point of view, to avoid splitting rules
        // based on the player

        val moveData = MovementData(from, to, board, player)

        val pawnDiagonal = PawnDiagonal(board, moveData, player)
        val subRules =
            mapOf(
                (0 to 1) to PawnFront(board, moveData, player),
                (1 to 1) to pawnDiagonal,
                (-1 to 1) to pawnDiagonal,
                (0 to 2) to PawnTwoPlaces(board, moveData, player, hasEverMoved),
            )

        return subRules[ moveData.colDelta to moveData.rowDelta ]?.verify()
    }
}
