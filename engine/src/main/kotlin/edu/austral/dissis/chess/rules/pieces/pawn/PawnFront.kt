package edu.austral.dissis.chess.rules.pieces.pawn

import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.pieces.Pawn
import edu.austral.dissis.chess.engine.pieces.Pawn.State
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.rules.Rule

class PawnFront(
    private val board: GameBoard,
    private val moveData: MovementData,
    private val player: Player,
) : Rule<Play?> {
    override fun verify(): Play? {
        return if (board.isOccupied(moveData.to)) {
            null
        } else {
            Move(
                moveData.from,
                moveData.to,
                board,
                pieceNextTurn =
                    Piece(
                        player,
                        Pawn(player, State.MOVED),
                    ),
            ).asPlay()
        }
    }
}
