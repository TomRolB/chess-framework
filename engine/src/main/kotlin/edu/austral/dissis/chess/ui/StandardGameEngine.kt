package edu.austral.dissis.chess.ui

import edu.austral.dissis.chess.engine.EngineResult.BLACK_WINS
import edu.austral.dissis.chess.engine.EngineResult.GENERAL_MOVE_VIOLATION
import edu.austral.dissis.chess.engine.EngineResult.PIECE_VIOLATION
import edu.austral.dissis.chess.engine.EngineResult.POST_PLAY_VIOLATION
import edu.austral.dissis.chess.engine.EngineResult.TIE_BY_BLACK
import edu.austral.dissis.chess.engine.EngineResult.TIE_BY_WHITE
import edu.austral.dissis.chess.engine.EngineResult.VALID_MOVE
import edu.austral.dissis.chess.engine.EngineResult.WHITE_WINS
import edu.austral.dissis.chess.engine.Game
import edu.austral.dissis.chess.engine.board.RectangularBoardValidator
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
import edu.austral.dissis.chess.gui.UndoState
import java.util.Stack

typealias UiBoard = Map<Position, ChessPiece>

class StandardGameEngine(
    private var game: Game,
    private val validator: RectangularBoardValidator,
    private val pieceAdapter: UiPieceAdapter,
) : GameEngine {
    private var uiBoard: UiBoard = emptyMap()
    private val actionAdapter = UiActionAdapter(pieceAdapter)

    private val undoStack = Stack<Pair<Game, NewGameState>>()
    private lateinit var currentState: NewGameState
    private val redoStack = Stack<Pair<Game, NewGameState>>()

    override fun applyMove(move: Move): MoveResult {
        val (from, to) = move
        val (ruleResult, newGame) = game.movePiece(adapt(from), adapt(to))
        val play = ruleResult.play
        val engineResult = ruleResult.engineResult
        val message = ruleResult.message

        uiBoard = actionAdapter.applyPlay(uiBoard, play)

        return when (engineResult) {
            GENERAL_MOVE_VIOLATION, PIECE_VIOLATION, POST_PLAY_VIOLATION -> {
                InvalidMove(message)
            }
            WHITE_WINS, TIE_BY_WHITE -> GameOver(PlayerColor.WHITE)
            BLACK_WINS, TIE_BY_BLACK -> GameOver(PlayerColor.BLACK)
            VALID_MOVE -> {
                undoStack.push(game to currentState)
                redoStack.clear()

                this.game = newGame

                currentState =
                    NewGameState(
                        pieces = uiBoard.values.toList(),
                        currentPlayer = UiPieceAdapter.adapt(game.turnManager.getTurn()),
                        undoState = UndoState(canUndo = true, canRedo = false),
                    )

                currentState
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

        currentState =
            NewGameState(
                pieces = uiBoard.values.toList(),
                currentPlayer = UiPieceAdapter.adapt(game.turnManager.getTurn()),
                undoState = UndoState(canUndo = false, canRedo = false),
            )

        return InitialState(
            boardSize = BoardSize(validator.numberCols, validator.numberRows),
            pieces = uiBoard.values.toList(),
            currentPlayer = UiPieceAdapter.adapt(game.turnManager.getTurn()),
        )
    }

    override fun redo(): NewGameState {
        undoStack.push(game to currentState)
        val (redoGame, redoState) = redoStack.pop()
        game = redoGame
        uiBoard = redoState.pieces.associateBy { it.position }

        val undoState =
            UndoState(
                canRedo = !redoStack.isEmpty(),
                canUndo = true,
            )

        currentState =
            NewGameState(
                pieces = redoState.pieces,
                currentPlayer = redoState.currentPlayer,
                undoState,
            )

        return currentState
    }

    override fun undo(): NewGameState {
        redoStack.push(game to currentState)
        val (undoGame, stackState) = undoStack.pop()
        game = undoGame
        uiBoard = stackState.pieces.associateBy { it.position }

        val undoState =
            UndoState(
                canRedo = true,
                canUndo = !undoStack.isEmpty(),
            )

        currentState =
            NewGameState(
                pieces = stackState.pieces,
                currentPlayer = stackState.currentPlayer,
                undoState,
            )

        return currentState
    }
}
