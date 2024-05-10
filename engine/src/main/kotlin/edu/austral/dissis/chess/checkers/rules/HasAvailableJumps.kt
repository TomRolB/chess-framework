package edu.austral.dissis.chess.checkers.rules

import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.includesTakeAction
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.rules.Rule

class HasAvailableJumps(val piece: Piece, val  board: GameBoard, val position: Position): Rule<Boolean> {
    override fun verify(): Boolean {
        //TODO: if we don't have a CAN_TAKE_ENEMY in the end, then to check a piece has actually
        // taken an enemy's we'll have to call this function again. Thus, it will probably be
        // converted to a Rule.
        return piece
            .getValidPlays(board, position)
            .any { it.includesTakeAction() }
    }
}