package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.turns.TurnManager
import edu.austral.dissis.chess.rules.RuleChain

data class GameData(
    val board: ChessBoard,
    val turnManager: TurnManager,
    val from: Position,
    val to: Position,
)

data class GameResult(
    val board: ChessBoard,
    val play: Play?,
    val engineResult: EngineResult,
    val message: String,
)

// TODO: Should be immutable
class Game(
    val gameRules: RuleChain<GameData, GameResult>,
    var board: ChessBoard,
    var turnManager: TurnManager,
) {
    fun movePiece(
        from: Position,
        to: Position,
    ): GameResult {
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

