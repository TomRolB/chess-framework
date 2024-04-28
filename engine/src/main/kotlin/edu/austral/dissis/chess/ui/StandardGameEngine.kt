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
    val game: Game,
    val validator: RectangleBoardValidator,
    val pieceAdapter: UiPieceAdapter,
    val postPlayProcedures: (UiBoard) -> UiBoard
) : GameEngine {
    var board: UiBoard = mapOf()
    val actionAdapter = UiActionAdapter(pieceAdapter, postPlayProcedures)


    override fun applyMove(move: Move): MoveResult {
        val (from, to) = move

        val (play, engineResult) = game.movePiece(adapt(from), adapt(to))

        board = actionAdapter.applyPlay(board, play)

        return when (engineResult) {
            EngineResult.GENERAL_MOVE_VIOLATION, EngineResult.PIECE_VIOLATION, EngineResult.POST_PLAY_VIOLATION -> {
                //TODO: reason = [message from the engine]
                InvalidMove("Invalid move")
            }
            EngineResult.WHITE_WINS -> GameOver(PlayerColor.WHITE)
            EngineResult.BLACK_WINS -> GameOver(PlayerColor.BLACK)
            // TODO: may have to make a TIE_BY_WHITE and TIE_BY_BLACK
            EngineResult.TIE -> GameOver(PlayerColor.BLACK)
            EngineResult.VALID_MOVE -> {
                val pieces = board.values.toList()
                val currentPlayer = UiPieceAdapter.adapt(game.turnManager.getTurn())

                NewGameState(pieces, currentPlayer)
            }
        }
    }

    private fun adapt(pos: Position): edu.austral.dissis.chess.engine.Position {
        return edu.austral.dissis.chess.engine.Position(pos.row, pos.column)
    }

    override fun init(): InitialState {
        board =
            game.board
                .getAllPositions()
                .map {
                    val chessPiece = pieceAdapter.adaptNew(game.board.getPieceAt(it)!!, it)
                    chessPiece.position to chessPiece
                }
                .toMap()

        return InitialState(
            boardSize = BoardSize(validator.numberRows, validator.numberCols),
            pieces = board.values.toList(),
            currentPlayer = UiPieceAdapter.adapt(game.turnManager.getTurn())
        )
    }
}
