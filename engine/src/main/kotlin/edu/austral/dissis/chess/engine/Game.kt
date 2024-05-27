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

class Game private constructor(
    val gameRules: RuleChain<GameData, RuleResult>,
    val board: GameBoard,
    val turnManager: TurnManager,
    private val previousState: Game?,
    private val nextState: Game?,
) {
    constructor(gameRules: RuleChain<GameData, RuleResult>, board: GameBoard, turnManager: TurnManager) : this(
        gameRules,
        board,
        turnManager,
        null,
        null,
    )

    fun movePiece(
        from: Position,
        to: Position,
    ): Pair<RuleResult, Game> {
        val gameData = GameData(board, turnManager, from, to)
        val ruleResult = gameRules.verify(gameData)
        val newGame = Game(gameRules, ruleResult.board, turnManager.nextTurn(ruleResult), this, null)

        return ruleResult to newGame
    }

    fun undo(): Game {
        checkNotNull(previousState) { "There is no movement to undo" }

        return Game(
            previousState.gameRules,
            previousState.board,
            previousState.turnManager,
            previousState.previousState,
            this,
        )
    }

    fun redo(): Game {
        checkNotNull(nextState) { "There is no movement to redo" }

        return nextState
    }

    fun canUndo(): Boolean {
        return previousState != null
    }

    fun canRedo(): Boolean {
        return nextState != null
    }
}

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
