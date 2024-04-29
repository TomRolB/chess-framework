package edu.austral.dissis.chess.rules.pieces.pawn

import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.pieces.Pawn
import edu.austral.dissis.chess.engine.pieces.Pawn.State
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.rules.All
import edu.austral.dissis.chess.rules.Not
import edu.austral.dissis.chess.rules.Rule
import edu.austral.dissis.chess.rules.SimpleRule

class PawnTwoPlaces(
    private val board: GameBoard,
    private val moveData: MovementData,
    private val player: Player,
    private val hasEverMoved: Boolean,
) : Rule<Play?> {
    override fun verify(): Play? {
        val subRules =
            All(
                SimpleRule(!hasEverMoved),
                Not(PawnPathIsBlocked(board, moveData)),
            )

        return if (!subRules.verify()) {
            null
        } else {
            Move(
                moveData.from,
                moveData.to,
                board,
                pieceNextTurn = Piece(player, type = Pawn(player, State.MOVED_TWO_PLACES)),
            ).asPlay()
        }
    }
}
