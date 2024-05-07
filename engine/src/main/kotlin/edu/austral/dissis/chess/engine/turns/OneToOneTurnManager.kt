package edu.austral.dissis.chess.engine.turns

import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.not

class OneToOneTurnManager : TurnManager {
    private val currentTurn: Player

    constructor () {
        this.currentTurn = Player.WHITE
    }

    private constructor(player: Player) {
        this.currentTurn = player
    }

    override fun getTurn(): Player {
        return currentTurn
    }

    override fun nextTurn(): TurnManager {
        return OneToOneTurnManager(!currentTurn)
    }
}