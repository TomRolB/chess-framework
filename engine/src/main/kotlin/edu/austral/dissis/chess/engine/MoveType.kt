package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position

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
