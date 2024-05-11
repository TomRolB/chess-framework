package edu.austral.dissis.chess.chess.rules.castling

import edu.austral.dissis.chess.chess.pieces.ChessPieceState.MOVED
import edu.austral.dissis.chess.chess.rules.king.IsKingChecked
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.rules.Not
import edu.austral.dissis.chess.engine.rules.RuleChain
import edu.austral.dissis.chess.engine.rules.SimpleRule

class CastlingSubRules(
    private val king: Piece,
    val board: GameBoard,
    val from: Position,
    val to: Position,
) : RuleChain<Pair<Position, Position>, Boolean> {
    override fun verify(arg: Pair<Position, Position>): Boolean {
        val (rookFrom, _) = arg
        val rules =
            edu.austral.dissis.chess.engine.rules.All(
                SimpleRule(!king.hasState(MOVED)),
                IsRookAvailable(board, rookFrom, king.player),
                IsKingsPathSafe(king, from, to, board),
                IsbColumnFree(board, rookFrom),
                Not(IsKingChecked(board, king.player)),
            )

        return rules.verify()
    }
}
