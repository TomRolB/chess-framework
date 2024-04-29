package edu.austral.dissis.chess.ui

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.Game
import edu.austral.dissis.chess.engine.RectangleBoardValidator
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
    private val game: Game,
    private val validator: RectangleBoardValidator,
    private val pieceAdapter: UiPieceAdapter,
    postPlayProcedures: (UiBoard) -> UiBoard,
) : GameEngine {
    var board: UiBoard = mapOf()
    private val actionAdapter = UiActionAdapter(pieceAdapter, postPlayProcedures)

    override fun applyMove(move: Move): MoveResult {
        val (from, to) = move
        val ruleResult = game.movePiece(adapt(from), adapt(to))
        val play = ruleResult.play
        val engineResult = ruleResult.engineResult
        val message = ruleResult.message

        board = actionAdapter.applyPlay(board, play)

        return when (engineResult) {
            EngineResult.GENERAL_MOVE_VIOLATION, EngineResult.PIECE_VIOLATION, EngineResult.POST_PLAY_VIOLATION -> {
                // TODO: reason = [message from the engine]
                InvalidMove(message)
            }
            EngineResult.WHITE_WINS -> GameOver(PlayerColor.WHITE)
            EngineResult.BLACK_WINS -> GameOver(PlayerColor.BLACK)
            // TODO: may have to make a TIE_BY_WHITE and TIE_BY_BLACK
            EngineResult.TIE -> GameOver(PlayerColor.BLACK)
            EngineResult.VALID_MOVE -> {
                NewGameState(
                    pieces = board.values.toList(),
                    currentPlayer = UiPieceAdapter.adapt(game.turnManager.getTurn()),
                )
            }
        }
    }

    private fun adapt(pos: Position): edu.austral.dissis.chess.engine.Position {
        return edu.austral.dissis.chess.engine.Position(pos.row, pos.column)
    }

    override fun init(): InitialState {
        board =
            game.board
                .getAllPositions().associate {
                    val chessPiece = pieceAdapter.adaptNew(game.board.getPieceAt(it)!!, it)
                    chessPiece.position to chessPiece
                }

        return InitialState(
            boardSize = BoardSize(validator.numberRows, validator.numberCols),
            pieces = board.values.toList(),
            currentPlayer = UiPieceAdapter.adapt(game.turnManager.getTurn()),
        )
    }
}
