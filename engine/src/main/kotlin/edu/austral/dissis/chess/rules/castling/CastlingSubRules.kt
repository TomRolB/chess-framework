package edu.austral.dissis.chess.rules.castling

import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.pieces.King
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.rules.All
import edu.austral.dissis.chess.rules.pieces.king.IsKingChecked
import edu.austral.dissis.chess.rules.Not
import edu.austral.dissis.chess.rules.RuleChain
import edu.austral.dissis.chess.rules.SimpleRule

class CastlingSubRules(
    private val kingRules: King,
    private val hasEverMoved: Boolean,
    val board: GameBoard,
    val from: Position,
    val to: Position,
) : RuleChain<Pair<Position, Position>, Boolean> {
    override fun verify(arg: Pair<Position, Position>): Boolean {
        val (rookFrom, _) = arg
        val rules =
            All(
                SimpleRule(!hasEverMoved),
                IsRookAvailable(board, rookFrom, kingRules.player),
                IsKingsPathSafe(kingRules, from, to, board),
                IsbColumnFree(board, rookFrom),
                Not(IsKingChecked(board, kingRules.player)),
            )

        return rules.verify()
    }
}
