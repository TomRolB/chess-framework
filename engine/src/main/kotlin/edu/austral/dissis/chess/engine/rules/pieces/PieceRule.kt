package edu.austral.dissis.chess.engine.rules.pieces

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.PlayResult

interface PieceRule {
    fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play>

    fun getPlayResult(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult
}