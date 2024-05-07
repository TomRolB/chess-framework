package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position

interface PrePlayValidator {
    fun getResultOnViolation(
        board: ChessBoard,
        from: Position,
        to: Position,
        player: Player
    ): PlayResult?
}