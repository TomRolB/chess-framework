package edu.austral.dissis.chess.ui

import edu.austral.dissis.chess.engine.Game
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.board.Position

interface GameConnection {
    fun movePiece(
        from: Position,
        to: Position,
    ): Pair<RuleResult, Game>
}
