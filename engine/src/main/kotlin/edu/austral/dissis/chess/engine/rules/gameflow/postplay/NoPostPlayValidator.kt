package edu.austral.dissis.chess.engine.rules.gameflow.postplay

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.board.GameBoard

class NoPostPlayValidator : PostPlayValidator {
    override fun getResult(
        play: Play,
        board: GameBoard,
        player: Player,
    ): RuleResult {
        return RuleResult(
            board = board,
            play = play,
            engineResult = EngineResult.VALID_MOVE,
            message = "Valid play"
        )
    }
}