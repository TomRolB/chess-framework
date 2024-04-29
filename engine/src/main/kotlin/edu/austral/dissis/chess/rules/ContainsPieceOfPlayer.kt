package edu.austral.dissis.chess.rules

import edu.austral.dissis.chess.engine.GameBoard
import edu.austral.dissis.chess.engine.Piece
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.Position

class ContainsPieceOfPlayer(
    val board: GameBoard,
    val pos: Position,
    val player: Player,
    val next: RuleChain<Piece, Boolean>,
) : RuleChain<Any?, Boolean> {
    override fun verify(arg: Any?): Boolean {
        return if (!board.containsPieceOfPlayer(pos, player)) false
        else next.verify(board.getPieceAt(pos)!!)
    }
}
