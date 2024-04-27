package edu.austral.dissis.chess.rules.castling

import edu.austral.dissis.chess.engine.GameBoard
import edu.austral.dissis.chess.engine.KingPieceRules
import edu.austral.dissis.chess.engine.Position
import edu.austral.dissis.chess.rules.*

class CastlingSubRules(
    val kingRules: KingPieceRules,
    val hasEverMoved: Boolean,
    val board: GameBoard,
    val from: Position,
    val to: Position,
): RuleChain<Pair<Position, Position>, Boolean> {
    override fun verify(arg: Pair<Position, Position>): Boolean {
        val (rookFrom, rookTo) = arg
        val rules = All(
            SimpleRule(!hasEverMoved),
            IsRookAvailable(board, rookFrom, kingRules.player),
            IsKingsPathSafe(kingRules, from, to, board),
            IsbColumnFree(board, rookFrom),
            Not(IsKingChecked(board, kingRules.player))
        )

        return rules.verify()
    }
}