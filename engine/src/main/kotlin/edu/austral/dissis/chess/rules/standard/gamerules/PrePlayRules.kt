package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.GameResult
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.PrePlayValidator
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
    val validator: PrePlayValidator,
    val next: RuleChain<Piece, GameResult>,
) : Rule<GameResult> {
    override fun verify(): GameResult {
        val resultOnViolation = validator.getResultOnViolation(board, from, to, player)

        return resultOnViolation ?: let {
                val piece = board.getPieceAt(from)!!
                next.verify(piece)
        }

//        return when {
//            !board.containsPieceOfPlayer(from, player) ->
//                getViolationResult("This tile does not contain a piece of yours")
//            (from == to) ->
//                getViolationResult("Cannot stay in the same place")
//            else -> {
//                val piece = board.getPieceAt(from)!!
//                next.verify(piece)
//            }
//        }
    }

    private fun getViolationResult(message: String): GameResult {
        return GameResult(
            board,
            null,
            EngineResult.GENERAL_MOVE_VIOLATION,
            message
        )
    }
}
