package edu.austral.dissis.chess.ui

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.Game
import edu.austral.dissis.chess.engine.board.RectangleBoardValidator
import edu.austral.dissis.chess.gui.BoardSize
import edu.austral.dissis.chess.gui.ChessPiece
import edu.austral.dissis.chess.gui.GameEngine
import edu.austral.dissis.chess.gui.GameOver
import edu.austral.dissis.chess.gui.InitialState
import edu.austral.dissis.chess.gui.InvalidMove
import edu.austral.dissis.chess.gui.Move
import edu.austral.dissis.chess.gui.MoveResult
import edu.austral.dissis.chess.gui.NewGameState
import edu.austral.dissis.chess.gui.PlayerColor
import edu.austral.dissis.chess.gui.Position

typealias UiBoard = Map<Position, ChessPiece>

class StandardGameEngine(
    private var game: Game,
    private val validator: RectangleBoardValidator,
    private val pieceAdapter: UiPieceAdapter,
) : GameEngine {
    var uiBoard: UiBoard = mapOf()
    private val actionAdapter = UiActionAdapter(pieceAdapter)

    override fun applyMove(move: Move): MoveResult {
        val (from, to) = move
        val (ruleResult, newGame) = game.movePiece(adapt(from), adapt(to))
        val play = ruleResult.play
        val engineResult = ruleResult.engineResult
        val message = ruleResult.message

        uiBoard = actionAdapter.applyPlay(uiBoard, play)

        return when (engineResult) {
            EngineResult.GENERAL_MOVE_VIOLATION, EngineResult.PIECE_VIOLATION, EngineResult.POST_PLAY_VIOLATION -> {
                InvalidMove(message)
            }
            EngineResult.WHITE_WINS, EngineResult.TIE_BY_WHITE -> GameOver(PlayerColor.WHITE)
            EngineResult.BLACK_WINS, EngineResult.TIE_BY_BLACK -> GameOver(PlayerColor.BLACK)
            EngineResult.VALID_MOVE -> {
                this.game = newGame

                NewGameState(
                    pieces = uiBoard.values.toList(),
                    currentPlayer = UiPieceAdapter.adapt(game.turnManager.getTurn()),
                )
            }
        }
    }

    private fun adapt(pos: Position): edu.austral.dissis.chess.engine.board.Position {
        return edu.austral.dissis.chess.engine.board.Position(pos.row, pos.column)
    }

    override fun init(): InitialState {
        uiBoard =
            game.board
                .getAllPositions().associate {
                    val chessPiece = pieceAdapter.adaptNew(game.board.getPieceAt(it)!!, it)
                    chessPiece.position to chessPiece
                }

        return InitialState(
            boardSize = BoardSize(validator.numberRows, validator.numberCols),
            pieces = uiBoard.values.toList(),
            currentPlayer = UiPieceAdapter.adapt(game.turnManager.getTurn()),
        )
    }
}
