package edu.austral.dissis.chess.rules.castling

import edu.austral.dissis.chess.engine.*
import edu.austral.dissis.chess.engine.KingPieceRules.Companion.isChecked
import edu.austral.dissis.chess.rules.Rule

class IsKingsPathSafe (
    val kingRules: KingPieceRules,
    val from: Position,
    val to: Position,
    val board: GameBoard): Rule<Boolean>
{
    override fun verify(): Boolean {
        val positions =
            if (to.col == 3) listOf(Position(from.row, 4), to)
            else listOf(Position(from.row, 6, ), to)

        return positions
            .map { Move(from, it, board, pieceNextTurn = kingRules.asMoved()).execute() }
            .all { futureBoard -> !isChecked(futureBoard, kingRules.player) }
    }
}
