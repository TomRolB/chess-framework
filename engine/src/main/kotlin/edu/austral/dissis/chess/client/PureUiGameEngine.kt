//package edu.austral.dissis.chess.client
//
//import edu.austral.dissis.chess.engine.EngineResult
//import edu.austral.dissis.chess.engine.Game
//import edu.austral.dissis.chess.engine.board.RectangularBoardValidator
//import edu.austral.dissis.chess.gui.BoardSize
//import edu.austral.dissis.chess.gui.GameEngine
//import edu.austral.dissis.chess.gui.GameOver
//import edu.austral.dissis.chess.gui.InitialState
//import edu.austral.dissis.chess.gui.InvalidMove
//import edu.austral.dissis.chess.gui.Move
//import edu.austral.dissis.chess.gui.MoveResult
//import edu.austral.dissis.chess.gui.NewGameState
//import edu.austral.dissis.chess.gui.PlayerColor
//import edu.austral.dissis.chess.gui.Position
//import edu.austral.dissis.chess.gui.UndoState
//import edu.austral.dissis.chess.ui.UiActionAdapter
//import edu.austral.dissis.chess.ui.UiBoard
//import edu.austral.dissis.chess.ui.UiPieceAdapter
//import java.util.*
//
//class PureUiGameEngine(
//    private val validator: RectangularBoardValidator,
//    private val pieceAdapter: UiPieceAdapter,
//) : GameEngine {
//    private var uiBoard: UiBoard = emptyMap()
//    private val actionAdapter = UiActionAdapter(pieceAdapter)
//
//    private val undoStack = Stack<Pair<Game, NewGameState>>()
//    private lateinit var currentState: NewGameState
//    private val redoStack = Stack<Pair<Game, NewGameState>>()
//
//    override fun applyMove(move: Move): MoveResult {
//        val (from, to) = move
//        val (ruleResult, newGame) = game.movePiece(adapt(from), adapt(to))
//        val play = ruleResult.play
//        val engineResult = ruleResult.engineResult
//        val message = ruleResult.message
//
//        uiBoard = actionAdapter.applyPlay(uiBoard, play)
//
//        return when (engineResult) {
//            EngineResult.GENERAL_MOVE_VIOLATION, EngineResult.PIECE_VIOLATION, EngineResult.POST_PLAY_VIOLATION -> {
//                InvalidMove(message)
//            }
//            EngineResult.WHITE_WINS, EngineResult.TIE_BY_WHITE -> GameOver(PlayerColor.WHITE)
//            EngineResult.BLACK_WINS, EngineResult.TIE_BY_BLACK -> GameOver(PlayerColor.BLACK)
//            EngineResult.VALID_MOVE -> {
//                undoStack.push(game to currentState)
//                redoStack.clear()
//
//
//                currentState =
//                    NewGameState(
//                        pieces = uiBoard.values.toList(),
//                        currentPlayer = UiPieceAdapter.adapt(game.turnManager.getTurn()),
//                        undoState = UndoState(canUndo = true, canRedo = false),
//                    )
//
//                currentState
//            }
//        }
//    }
//
//    private fun adapt(pos: Position): edu.austral.dissis.chess.engine.board.Position {
//        return edu.austral.dissis.chess.engine.board.Position(pos.row, pos.column)
//    }
//
//    override fun init(): InitialState {
//        uiBoard =
//            game.board
//                .getAllPositions().associate {
//                    val chessPiece = pieceAdapter.adaptNew(game.board.getPieceAt(it)!!, it)
//                    chessPiece.position to chessPiece
//                }
//
//        currentState =
//            NewGameState(
//                pieces = uiBoard.values.toList(),
//                currentPlayer = UiPieceAdapter.adapt(game.turnManager.getTurn()),
//                undoState = UndoState(canUndo = false, canRedo = false),
//            )
//
//        return InitialState(
//            boardSize = BoardSize(validator.numberCols, validator.numberRows),
//            pieces = uiBoard.values.toList(),
//            currentPlayer = UiPieceAdapter.adapt(game.turnManager.getTurn()),
//        )
//    }
//
//    override fun redo(): NewGameState {
//        TODO("Yet to implement")
//    }
//
//    override fun undo(): NewGameState {
//        TODO("Yet to implement")
//    }
//}