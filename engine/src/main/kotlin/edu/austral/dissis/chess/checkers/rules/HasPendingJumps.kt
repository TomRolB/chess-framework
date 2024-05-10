package edu.austral.dissis.chess.checkers.rules

import edu.austral.dissis.chess.checkers.rules.CheckersPieceState.HAS_PENDING_JUMP
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.rules.Rule

class HasPendingJumps(val board: GameBoard, val from: Position): Rule<Boolean> {
    override fun verify(): Boolean {
        return board
            .getPieceAt(from)!!
            .hasState(HAS_PENDING_JUMP)
    }
}