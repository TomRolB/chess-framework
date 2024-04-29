package edu.austral.dissis.chess.rules.castling

import edu.austral.dissis.chess.engine.GameBoard
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.Position
import edu.austral.dissis.chess.engine.RookPieceRules
import edu.austral.dissis.chess.rules.PieceHasNeverMoved
import edu.austral.dissis.chess.rules.PieceOfPlayer
import edu.austral.dissis.chess.rules.Rule
import edu.austral.dissis.chess.rules.Succeed
import edu.austral.dissis.chess.rules.pieces.PieceOfType

class IsRookAvailable(
    val board: GameBoard,
    val rookPos: Position,
    val player: Player,
) : Rule<Boolean> {
    override fun verify(): Boolean {
        return PieceOfPlayer(
            board,
            rookPos,
            player,
            next = PieceOfType<RookPieceRules>(
                RookPieceRules::class.java,
                next = PieceHasNeverMoved(
                    next = Succeed(),
                )
            )
        ).verify(null)
    }
}
