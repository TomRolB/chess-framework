package edu.austral.dissis.chess.checkers.rules

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.PrePlayValidator
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position

class CompulsoryJumpsValidator : PrePlayValidator {
    override fun getResultOnViolation(
        board: GameBoard,
        from: Position,
        to: Position,
        player: Player,
    ): RuleResult? {
        return when {
            // TODO: could compose these rules
            !board.containsPieceOfPlayer(from, player) ->
                getViolationResult(board, "This tile does not contain a piece of yours")
            violatesCompulsoryJumps(board, from, player) ->
                getViolationResult(board, "One of your pieces has a pending move")
            (from == to) ->
                getViolationResult(board, "Cannot stay in the same place")
            else -> null
        }
    }

    private fun violatesCompulsoryJumps(
        board: GameBoard,
        from: Position,
        player: Player,
    ): Boolean {
        val currentPiece = board.getPieceAt(from)!!
        return !HasAvailableJumps(currentPiece, board, from).verify()
                && piecesHaveAvailableJumps(board, player, from)
    }

    // TODO: may convert to Rule
    private fun piecesHaveAvailableJumps(
        board: GameBoard,
        player: Player,
        position: Position
    ): Boolean {
        return board
            .getAllPositionsOfPlayer(player)
            .any {
                val piece = board.getPieceAt(it)!!
                HasAvailableJumps(piece, board, it).verify()
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
