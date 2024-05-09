package edu.austral.dissis.chess.checkers.rules

import edu.austral.dissis.chess.checkers.rules.CheckersPieceState.HAS_PENDING_MOVE
import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.PrePlayValidator
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position

class PendingMovesValidator : PrePlayValidator {
    override fun getResultOnViolation(
        board: GameBoard,
        from: Position,
        to: Position,
        player: Player,
    ): RuleResult? {
        return when {
            !board.containsPieceOfPlayer(from, player) ->
                getViolationResult(board, "This tile does not contain a piece of yours")
            otherPiecesHavePendingMoves(board, from, player) ->
                getViolationResult(board, "One of your pieces has a pending move")
            (from == to) ->
                getViolationResult(board, "Cannot stay in the same place")
            else -> null
        }
    }

    private fun otherPiecesHavePendingMoves(
        board: GameBoard,
        from: Position,
        player: Player,
    ) = !board.getPieceAt(from)!!.hasState(HAS_PENDING_MOVE) && piecesHavePendingMoves(board, player)

    private fun piecesHavePendingMoves(
        board: GameBoard,
        player: Player,
    ): Boolean {
        return board
            .getAllPositionsOfPlayer(player)
            .any {
                board.getPieceAt(it)!!.hasState(HAS_PENDING_MOVE)
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
