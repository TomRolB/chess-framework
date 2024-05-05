package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position

// TODO: would be better to have more atomic rules. For instance,
//  composing the knight's movement of a rule of moving 2 times in
//  one direction, and another of moving one time in other direction.

interface MoveType {
    fun isViolated(moveData: MovementData): Boolean

    fun isPathBlocked(
        moveData: MovementData,
        board: ChessBoard,
    ): Boolean

    fun getPossiblePositions(
        board: ChessBoard,
        position: Position,
    ): Iterable<Position>
}
