package edu.austral.dissis.chess.rules.castling

import edu.austral.dissis.chess.engine.*
import edu.austral.dissis.chess.rules.*

class IsRookAvailable(
    val board: GameBoard,
    val rookPos: Position,
    val player: Player) : Rule<Boolean> {
    override fun verify(): Boolean {
        return PieceOfPlayer(board, rookPos, player,
            next = PieceHasNeverMoved( RookPieceRules::class.java,
                next = Succeed())
        ).verify(null)
    }
}
