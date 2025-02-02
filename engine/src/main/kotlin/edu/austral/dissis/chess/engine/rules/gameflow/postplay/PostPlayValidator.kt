package edu.austral.dissis.chess.engine.rules.gameflow.postplay

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.board.GameBoard

interface PostPlayValidator {
    fun getResult(
        play: Play,
        board: GameBoard,
        player: Player,
    ): RuleResult
}
