package edu.austral.dissis.chess.engine.turns

import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.not

class OneToOneTurnManager(val currentPlayer: Player) : TurnManager {
    override fun getTurn(): Player {
        return currentPlayer
    }

    override fun nextTurn(result: RuleResult): TurnManager {
        return OneToOneTurnManager(!currentPlayer)
    }
}
