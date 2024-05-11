package edu.austral.dissis.chess.engine.rules.gameflow.preplay

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position

class CompoundPrePlayValidator(
    val firstValidator: PrePlayValidator,
    vararg val moreValidators: PrePlayValidator
): PrePlayValidator {
    override fun getResult(board: GameBoard, from: Position, to: Position, player: Player): RuleResult {
        var result = firstValidator.getResult(board, from, to, player)

        for (validator in moreValidators) {
            if (result.engineResult != EngineResult.VALID_MOVE) break
            result = validator.getResult(board, from, to, player)
        }

        return result
    }
}