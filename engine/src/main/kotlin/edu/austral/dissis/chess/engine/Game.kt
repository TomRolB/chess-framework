package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.rules.RuleChain

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
    var board: GameBoard,
    var turnManager: TurnManager,
) {
    fun movePiece(
        from: Position,
        to: Position,
    ): RuleResult {
        val gameData = GameData(board, turnManager, from, to)
        val ruleResult = gameRules.verify(gameData)

        if (ruleResult.engineResult == EngineResult.VALID_MOVE) {
            board = ruleResult.board
            turnManager = turnManager.nextTurn()
        }

        return ruleResult
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

interface TurnManager {
    fun getTurn(): Player

    fun nextTurn(): TurnManager
}

class OneToOneTurnManager : TurnManager {
    private val currentTurn: Player

    constructor () {
        this.currentTurn = Player.WHITE
    }

    private constructor(player: Player) {
        this.currentTurn = player
    }

    override fun getTurn(): Player {
        return currentTurn
    }

    override fun nextTurn(): TurnManager {
        return OneToOneTurnManager(!currentTurn)
    }
}
