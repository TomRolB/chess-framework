package edu.austral.dissis.chess.checkers.rules

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.PrePlayValidator
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.includesTake
import edu.austral.dissis.chess.engine.pieces.Piece

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
        return !hasAvailableJumps(currentPiece, board, from) && piecesHaveAvailableJumps(board, player, from)
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
                hasAvailableJumps(piece, board, it)
            }
    }

    private fun hasAvailableJumps(piece: Piece, board: GameBoard, position: Position): Boolean {
        //TODO: if we don't have a CAN_TAKE_ENEMY in the end, then to check a piece has actually
        // taken an enemy's we'll have to call this function again. Thus, it will probably be
        // converted to a Rule.
        return piece
            .getValidPlays(board, position)
            .any { it.includesTake() }
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
