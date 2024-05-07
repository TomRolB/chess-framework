package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.GameResult
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.rules.Rule
import edu.austral.dissis.chess.rules.RuleChain

class PrePlayRules(
    val board: ChessBoard,
    val from: Position,
    val to: Position,
    val player: Player,
    val next: RuleChain<Piece, GameResult>,
) : Rule<GameResult> {
    override fun verify(): GameResult {
        return when {
            !board.containsPieceOfPlayer(from, player) -> "This tile does not contain a piece of yours"
            (from == to) -> "Cannot stay in the same place"
            else -> null
        }
            ?.let { GameResult(board, null, EngineResult.GENERAL_MOVE_VIOLATION, it) }
            ?: let {
                val piece = board.getPieceAt(from)!!
                next.verify(piece)
            }
    }
}
