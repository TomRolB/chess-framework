package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position

interface PrePlayValidator {
    fun getResultOnViolation(
        board: GameBoard,
        from: Position,
        to: Position,
        player: Player,
    ): PlayResult?
}
