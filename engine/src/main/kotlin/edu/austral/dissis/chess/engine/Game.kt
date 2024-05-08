package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.turns.TurnManager
import edu.austral.dissis.chess.rules.RuleChain

data class GameData(
    val board: GameBoard,
    val turnManager: TurnManager,
    val from: Position,
    val to: Position,
)

data class PlayResult(
    val board: GameBoard,
    val play: Play?,
    val engineResult: EngineResult,
    val message: String,
)

// TODO: Should be immutable
class Game(
    val gameRules: RuleChain<GameData, PlayResult>,
    val board: GameBoard,
    val turnManager: TurnManager,
) {
    fun movePiece(
        from: Position,
        to: Position,
    ): Pair<PlayResult, Game> {
        val gameData = GameData(board, turnManager, from, to)

        val playResult = gameRules.verify(gameData)

        val newGame = Game(gameRules, playResult.board, turnManager.nextTurn())

        return playResult to newGame
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
