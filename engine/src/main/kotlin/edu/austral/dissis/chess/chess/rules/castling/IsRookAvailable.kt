package edu.austral.dissis.chess.chess.rules.castling

import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.rules.ContainsPieceOfPlayer
import edu.austral.dissis.chess.chess.rules.PieceHasNeverMoved
import edu.austral.dissis.chess.rules.Rule
import edu.austral.dissis.chess.rules.Succeed

class IsRookAvailable(
    private val board: GameBoard,
    private val rookPos: Position,
    private val player: Player,
) : Rule<Boolean> {
    override fun verify(): Boolean {
        return ContainsPieceOfPlayer(
            board,
            rookPos,
            player,
            next =
                PieceHasNeverMoved(
                    next = Succeed(),
                ),
        ).verify()
    }
}
