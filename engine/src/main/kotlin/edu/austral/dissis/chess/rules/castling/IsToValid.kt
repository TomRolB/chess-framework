package edu.austral.dissis.chess.rules.castling

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Position
import edu.austral.dissis.chess.rules.RuleChain

class IsToValid(
    val from: Position,
    val to: Position,
    val listener: RookMoveListener,
    val next: RuleChain<Pair<Position, Position>, Boolean>
) : RuleChain<Any?, Boolean> {
    override fun verify(arg: Any?): Boolean {
        val isToValid: Boolean
        val rookFrom: Position
        val rookTo: Position
        when (to.col) {
            3 -> {
                isToValid = true
                rookFrom = Position(from.row, 1)
                rookTo = Position(from.row, 4)
            }
            7 -> {
                isToValid = true
                rookFrom = Position(from.row, 8)
                rookTo = Position(from.row, 6)
            }
            else -> {
                isToValid = false
                rookFrom = Position(0, 0) // Unreachable
                rookTo = Position(0, 0) // Unreachable
            }
        }

        listener.rookTo = rookTo
        listener.rookFrom = rookFrom

        return if (isToValid) next.verify(rookFrom to rookTo) else false
    }
}