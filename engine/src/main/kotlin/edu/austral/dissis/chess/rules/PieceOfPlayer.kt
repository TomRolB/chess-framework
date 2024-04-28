package edu.austral.dissis.chess.rules

import edu.austral.dissis.chess.engine.GameBoard
import edu.austral.dissis.chess.engine.Piece
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.Position

class PieceOfPlayer(
    val board: GameBoard,
    val pos: Position,
    val player: Player,
    val next: RuleChain<Piece, Boolean>,
) : RuleChain<Any?, Boolean> {
    override fun verify(arg: Any?): Boolean {
        return board
            .getPieceAt(pos).takeIf { board.containsPieceOfPlayer(pos, player) }
            ?.let { next.verify(it) }
            ?: false
    }
}
