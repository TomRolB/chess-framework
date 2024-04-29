package edu.austral.dissis.chess.engine.board

import edu.austral.dissis.chess.engine.Player

interface PositionValidator {
    fun positionExists(position: Position): Boolean

    fun getRowAsWhite(
        position: Position,
        player: Player,
    ): Int

    fun isPositionOnLastRow(position: Position): Boolean
}