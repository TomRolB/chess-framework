package edu.austral.dissis.chess.checkers.rules

import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.rules.Rule

// TODO: should compose
class AnyAllyPieceHasPendingJumps(val board: GameBoard, val player: Player) : Rule<Boolean> {
    override fun verify(): Boolean {
        return board
            .getAllPositionsOfPlayer(player)
            .any {
                HasPendingJumps(board, it).verify()
            }
    }
}
