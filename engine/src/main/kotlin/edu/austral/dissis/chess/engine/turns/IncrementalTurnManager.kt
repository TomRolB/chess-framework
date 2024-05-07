package edu.austral.dissis.chess.engine.turns

import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.not

class IncrementalTurnManager : TurnManager {
    private val currentPlayer: Player
    private val numberTurns: Int
    private val count: Int

    constructor(initialPlayer: Player, initialNumberTurns: Int) {
        this.currentPlayer = initialPlayer
        this.numberTurns = initialNumberTurns
        this.count = 0
    }

    private constructor(player: Player, numberTurns: Int, count: Int) {
        this.currentPlayer = player
        this.numberTurns = numberTurns
        this.count = count
    }

    override fun getTurn(): Player {
        return currentPlayer
    }

    override fun nextTurn(): TurnManager {
        val currCount = count + 1
        val nextPlayer = if (currCount != numberTurns) currentPlayer else !currentPlayer
        val nextNumberTurns = if (currCount != numberTurns) numberTurns else numberTurns + 1
        val nextCount = if (currCount != numberTurns) currCount else 0

        return IncrementalTurnManager(nextPlayer, nextNumberTurns, nextCount)
    }
}
