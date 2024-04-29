package edu.austral.dissis.chess.rules.pieces.pawn

import edu.austral.dissis.chess.engine.GameBoard
import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.PawnPieceRules
import edu.austral.dissis.chess.engine.PawnPieceRules.State
import edu.austral.dissis.chess.engine.Piece
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
                        PawnPieceRules(player, State.MOVED),
                    ),
            ).asPlay()
        }
    }
}
