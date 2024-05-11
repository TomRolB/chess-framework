package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.rules.RuleChain
import edu.austral.dissis.chess.engine.turns.TurnManager

data class GameData(
    val board: GameBoard,
    val turnManager: TurnManager,
    val from: Position,
    val to: Position,
)

data class RuleResult(
    val board: GameBoard,
    val play: Play?,
    val engineResult: EngineResult,
    val message: String,
)

class Game(
    val gameRules: RuleChain<GameData, RuleResult>,
    val board: GameBoard,
    val turnManager: TurnManager,
) {
    fun movePiece(
        from: Position,
        to: Position,
    ): Pair<RuleResult, Game> {
        val gameData = GameData(board, turnManager, from, to)

        val ruleResult = gameRules.verify(gameData)

        val newGame = Game(gameRules, ruleResult.board, turnManager.nextTurn(ruleResult))

        return ruleResult to newGame
    }
}

// TODO: consider implementing an interface
enum class EngineResult {
    GENERAL_MOVE_VIOLATION,
    PIECE_VIOLATION,
    POST_PLAY_VIOLATION,
    WHITE_WINS,
    BLACK_WINS,
    TIE_BY_WHITE,
    TIE_BY_BLACK,
    VALID_MOVE,
}
