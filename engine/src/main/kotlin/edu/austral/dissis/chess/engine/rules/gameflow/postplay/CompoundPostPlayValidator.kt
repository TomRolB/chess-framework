package edu.austral.dissis.chess.engine.rules.gameflow.postplay

import edu.austral.dissis.chess.engine.EngineResult.VALID_MOVE
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.pieces.PlayResult

class CompoundPostPlayValidator(
    val firstValidator: PostPlayValidator,
    vararg val moreValidators: PostPlayValidator
): PostPlayValidator {
    override fun getResult(play: Play, board: GameBoard, player: Player): RuleResult {
        var result = firstValidator.getResult(play, board, player)

        for (validator in moreValidators) {
            if (result.engineResult != VALID_MOVE) break
            result = validator.getResult(play, board, player)
        }

        return result
    }
}