package edu.austral.dissis.chess.engine.rules.gameflow.preplay

import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position

interface PrePlayValidator {
    fun getResult(
        board: GameBoard,
        from: Position,
        to: Position,
        player: Player,
    ): RuleResult
}
