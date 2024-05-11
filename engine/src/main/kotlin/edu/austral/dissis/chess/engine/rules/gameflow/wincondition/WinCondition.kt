package edu.austral.dissis.chess.engine.rules.gameflow.wincondition

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.board.GameBoard

interface WinCondition {
    fun getResult(
        board: GameBoard,
        play: Play,
        player: Player,
    ): RuleResult
}
