package edu.austral.dissis.chess.engine.rules.gameflow.preplay

import edu.austral.dissis.chess.engine.EngineResult.GENERAL_MOVE_VIOLATION
import edu.austral.dissis.chess.engine.EngineResult.VALID_MOVE
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position

class PieceOfPlayer : PrePlayValidator {
    override fun getResult(
        board: GameBoard,
        from: Position,
        to: Position,
        player: Player,
    ): RuleResult {
        val invalid = from == to

        return RuleResult(
            board = board,
            play = null,
            engineResult = if (invalid) GENERAL_MOVE_VIOLATION else VALID_MOVE,
            message = if (invalid) "This tile does not contain a piece of yours" else "Valid play",
        )
    }
}
