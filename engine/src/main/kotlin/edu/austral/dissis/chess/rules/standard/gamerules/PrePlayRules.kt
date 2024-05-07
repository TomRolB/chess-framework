package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.PlayResult
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
    val next: RuleChain<Piece, PlayResult>,
) : Rule<PlayResult> {
    override fun verify(): PlayResult {
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

    private fun getViolationResult(message: String): PlayResult {
        return PlayResult(
            board,
            null,
            EngineResult.GENERAL_MOVE_VIOLATION,
            message
        )
    }
}
