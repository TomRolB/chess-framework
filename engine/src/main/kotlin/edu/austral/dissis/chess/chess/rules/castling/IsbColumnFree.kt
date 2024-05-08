package edu.austral.dissis.chess.chess.rules.castling

import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.rules.Rule

class IsbColumnFree(val board: GameBoard, private val rookFrom: Position) : Rule<Boolean> {
    override fun verify(): Boolean {
        // When performing long castling, the king does not pass over
        // the position in column b. However, it mustn't be blocked

        return rookFrom.col != 1 || !board.isOccupied(Position(rookFrom.row, 2))
    }
}
