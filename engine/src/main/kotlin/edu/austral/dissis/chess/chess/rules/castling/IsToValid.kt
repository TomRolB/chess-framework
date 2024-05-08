package edu.austral.dissis.chess.chess.rules.castling

import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.rules.Rule
import edu.austral.dissis.chess.engine.rules.RuleChain

private const val A_COLUMN = 1
private const val C_COLUMN = 3
private const val D_COLUMN = 4
private const val F_COLUMN = 6
private const val G_COLUMN = 7
private const val H_COLUMN = 8

class IsToValid(
    private val from: Position,
    private val to: Position,
    private val listener: RookMoveListener,
    private val next: RuleChain<Pair<Position, Position>, Boolean>,
) : Rule<Boolean> {
    override fun verify(): Boolean {
        val isToValid: Boolean
        val rookFrom: Position
        val rookTo: Position
        when (to.col) {
            C_COLUMN -> {
                isToValid = true
                rookFrom = Position(from.row, A_COLUMN)
                rookTo = Position(from.row, D_COLUMN)
            }
            G_COLUMN -> {
                isToValid = true
                rookFrom = Position(from.row, H_COLUMN)
                rookTo = Position(from.row, F_COLUMN)
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
