package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.GameResult
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.PrePlayValidator
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position

class ClassicPrePlayValidator : PrePlayValidator {
    override fun getResultOnViolation(
        board: ChessBoard,
        from: Position,
        to: Position,
        player: Player
    ): GameResult? {
        return when {
            !board.containsPieceOfPlayer(from, player) ->
                getViolationResult(board, "This tile does not contain a piece of yours")
            (from == to) ->
                getViolationResult(board, "Cannot stay in the same place")
            else -> null
        }
    }

    private fun getViolationResult(board: ChessBoard, message: String): GameResult {
        return GameResult(
            board,
            null,
            EngineResult.GENERAL_MOVE_VIOLATION,
            message
        )
    }
}
