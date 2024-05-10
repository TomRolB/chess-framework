package edu.austral.dissis.chess.engine.turns

import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.RuleResult

interface TurnManager {
    fun getTurn(): Player

    fun nextTurn(result: RuleResult): TurnManager
}
