package edu.austral.dissis.chess.engine.rules.pieces

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position

class PositionIterator(
    initPos: Position,
    val rowIncrement: Int,
    val colIncrement: Int,
): Iterator<Position> {
    var currentPos = initPos

    override fun hasNext(): Boolean {
        return true
    }

    override fun next(): Position {
        currentPos = Position(
            row = currentPos.row + rowIncrement,
            col = currentPos.col + colIncrement
        )

        return currentPos
    }
}
