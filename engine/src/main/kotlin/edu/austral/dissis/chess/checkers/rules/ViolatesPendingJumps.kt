package edu.austral.dissis.chess.checkers.rules

import edu.austral.dissis.chess.engine.EngineResult.GENERAL_MOVE_VIOLATION
import edu.austral.dissis.chess.engine.EngineResult.VALID_MOVE
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.rules.gameflow.preplay.PrePlayValidator

class ViolatesPendingJumps: PrePlayValidator {
    override fun getResult(board: GameBoard, from: Position, to: Position, player: Player): RuleResult {
        val invalid = anotherPieceHasPendingJump(board, from, player)

        return RuleResult(
            board = board,
            play = null,
            engineResult = if (invalid) GENERAL_MOVE_VIOLATION else VALID_MOVE,
            message = if (invalid) "There's another piece with a pending jump" else "Valid move"
        )
    }

    private fun anotherPieceHasPendingJump(
        board: GameBoard,
        from: Position,
        player: Player,
    ) = !HasPendingJumps(board, from).verify() &&
            AnyAllyPieceHasPendingJumps(board, player).verify()
}