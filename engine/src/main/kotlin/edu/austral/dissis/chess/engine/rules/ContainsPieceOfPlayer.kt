package edu.austral.dissis.chess.engine.rules

import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.Piece

class ContainsPieceOfPlayer(
    val board: GameBoard,
    val pos: Position,
    val player: Player,
    val next: RuleChain<Piece, Boolean>,
) : Rule<Boolean> {
    override fun verify(): Boolean {
        return if (!board.containsPieceOfPlayer(pos, player)) {
            false
        } else {
            next.verify(board.getPieceAt(pos)!!)
        }
    }
}
