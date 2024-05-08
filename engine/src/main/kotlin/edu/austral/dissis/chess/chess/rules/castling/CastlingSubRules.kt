package edu.austral.dissis.chess.chess.rules.castling

import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.chess.pieces.ClassicPieceState.MOVED
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.rules.All
import edu.austral.dissis.chess.rules.Not
import edu.austral.dissis.chess.rules.RuleChain
import edu.austral.dissis.chess.rules.SimpleRule
import edu.austral.dissis.chess.chess.rules.king.IsKingChecked

class CastlingSubRules(
    private val king: Piece,
    val board: GameBoard,
    val from: Position,
    val to: Position,
) : RuleChain<Pair<Position, Position>, Boolean> {
    override fun verify(arg: Pair<Position, Position>): Boolean {
        val (rookFrom, _) = arg
        val rules =
            All(
                SimpleRule(!king.hasState(MOVED)),
                IsRookAvailable(board, rookFrom, king.player),
                IsKingsPathSafe(king, from, to, board),
                IsbColumnFree(board, rookFrom),
                Not(IsKingChecked(board, king.player)),
            )

        return rules.verify()
    }
}
