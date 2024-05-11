package edu.austral.dissis.chess.engine.rules.gameflow.wincondition

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.board.GameBoard

class CompoundWinCondition(
    val firstValidator: WinCondition,
    vararg val moreValidators: WinCondition
): WinCondition {
    override fun getResult(board: GameBoard, play: Play, player: Player): RuleResult {
        var result = firstValidator.getResult(board, play, player)

        for (validator in moreValidators) {
            if (result.engineResult != EngineResult.VALID_MOVE) break
            result = validator.getResult(board, play, player)
        }

        return result
    }
}