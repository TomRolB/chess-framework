package edu.austral.dissis.chess.checkers

import edu.austral.dissis.chess.checkers.rules.AnyAllyPieceHasPendingJumps
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.not
import edu.austral.dissis.chess.engine.turns.TurnManager

class MultiTurnManager(val currentPlayer: Player) : TurnManager {
    override fun getTurn(): Player {
        return currentPlayer
    }

    override fun nextTurn(result: RuleResult): TurnManager {
        return MultiTurnManager(
            currentPlayer = getNextPlayer(result),
        )
    }

    private fun getNextPlayer(result: RuleResult) =
        if (AnyAllyPieceHasPendingJumps(result.board, currentPlayer).verify()) {
            currentPlayer
        } else {
            !currentPlayer
        }
}
