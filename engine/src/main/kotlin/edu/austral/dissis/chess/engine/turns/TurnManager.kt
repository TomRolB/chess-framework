package edu.austral.dissis.chess.engine.turns

import edu.austral.dissis.chess.engine.Player

interface TurnManager {
    fun getTurn(): Player

    fun nextTurn(): TurnManager
}