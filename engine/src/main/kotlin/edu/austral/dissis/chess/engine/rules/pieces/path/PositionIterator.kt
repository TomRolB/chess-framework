package edu.austral.dissis.chess.engine.rules.pieces.path

import edu.austral.dissis.chess.engine.board.Position

class PositionIterator(
    initPos: Position,
    private val rowIncrement: Int,
    private val colIncrement: Int,
) : Iterator<Position> {
    private var currentPos = initPos

    override fun hasNext(): Boolean {
        return true
    }

    override fun next(): Position {
        if (!hasNext()) throw NoSuchElementException("There is no position next")

        currentPos =
            Position(
                row = currentPos.row + rowIncrement,
                col = currentPos.col + colIncrement,
            )

        return currentPos
    }
}
