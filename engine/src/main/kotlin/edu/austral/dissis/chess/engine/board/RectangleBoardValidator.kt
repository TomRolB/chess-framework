package edu.austral.dissis.chess.engine.board

import edu.austral.dissis.chess.engine.Player

class RectangleBoardValidator(val numberRows: Int, val numberCols: Int) : PositionValidator {
    override fun positionExists(position: Position): Boolean {
        val (row, col) = position
        return (0 < row) && (row <= numberRows) &&
            (0 < col) && (col <= numberCols)
    }

    override fun getRowAsWhite(
        position: Position,
        player: Player,
    ): Int {
        if (player == Player.WHITE) return position.row

        return numberRows - position.row + 1
    }

    override fun isPositionOnLastRow(
        position: Position,
        player: Player,
    ): Boolean {
        return getRowAsWhite(position, player) == numberRows
    }
}
