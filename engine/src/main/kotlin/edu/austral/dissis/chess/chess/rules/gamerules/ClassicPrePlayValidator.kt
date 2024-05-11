package edu.austral.dissis.chess.chess.rules.gamerules

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.rules.gameflow.preplay.PrePlayValidator
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position

// TODO: replace by two atomic validators
class ClassicPrePlayValidator : PrePlayValidator {
    override fun getResult(
        board: GameBoard,
        from: Position,
        to: Position,
        player: Player,
    ): RuleResult {
        return when {
            !board.containsPieceOfPlayer(from, player) ->
                getViolationResult(board, "This tile does not contain a piece of yours")
            (from == to) ->
                getViolationResult(board, "Cannot stay in the same place")
            else -> RuleResult(
                board,
                null,
                EngineResult.VALID_MOVE,
                "valid move",
            )
        }
    }

    private fun getViolationResult(
        board: GameBoard,
        message: String,
    ): RuleResult {
        return RuleResult(
            board,
            null,
            EngineResult.GENERAL_MOVE_VIOLATION,
            message,
        )
    }
}
