package edu.austral.dissis.chess.engine.board

import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.pieces.Piece

data class Position(val row: Int, val col: Int)

interface GameBoard {
    fun isOccupied(position: Position): Boolean

    fun getPieceAt(position: Position): Piece?

    fun setPieceAt(
        position: Position,
        piece: Piece,
    ): GameBoard

    fun delPieceAt(position: Position): GameBoard

    fun positionExists(position: Position): Boolean

    fun isPositionOnUpperLimit(
        position: Position,
        player: Player,
    ): Boolean

    fun containsPieceOfPlayer(
        position: Position,
        player: Player,
    ): Boolean

    fun getAllPositions(): Iterable<Position>

    fun getAllPositionsOfPlayer(
        player: Player,
        includeKing: Boolean,
    ): Iterable<Position>

    fun getRowAsWhite(
        position: Position,
        player: Player,
    ): Int
}
