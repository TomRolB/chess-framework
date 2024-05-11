package edu.austral.dissis.chess.checkers.rules

import edu.austral.dissis.chess.engine.EngineResult.GENERAL_MOVE_VIOLATION
import edu.austral.dissis.chess.engine.EngineResult.VALID_MOVE
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.rules.gameflow.preplay.PrePlayValidator

class ViolatesCompulsoryJumps: PrePlayValidator {
    override fun getResult(board: GameBoard, from: Position, to: Position, player: Player): RuleResult {
        val currentPiece = board.getPieceAt(from)!!
        val invalid = anotherPieceMustJump(currentPiece, board, from, player)

        return RuleResult(
                board = board,
                play = null,
                engineResult = if (invalid) GENERAL_MOVE_VIOLATION else VALID_MOVE,
                message = if (invalid) "One of your pieces has a compulsory jump" else "Valid play"
            )
    }

    private fun anotherPieceMustJump(
        currentPiece: Piece,
        board: GameBoard,
        from: Position,
        player: Player,
    ) = !HasAvailableJumps(currentPiece, board, from).verify() &&
            piecesHaveAvailableJumps(board, player)

    // TODO: may convert to rule
    private fun piecesHaveAvailableJumps(
        board: GameBoard,
        player: Player,
    ): Boolean {
        return board
            .getAllPositionsOfPlayer(player)
            .any {
                val piece = board.getPieceAt(it)!!
                HasAvailableJumps(piece, board, it).verify()
            }
    }
}