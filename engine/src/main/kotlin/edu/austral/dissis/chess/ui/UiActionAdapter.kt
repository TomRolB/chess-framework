package edu.austral.dissis.chess.ui

import edu.austral.dissis.chess.engine.Action
import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Take
import edu.austral.dissis.chess.gui.ChessPiece
import edu.austral.dissis.chess.gui.Position

class UiActionAdapter(
    private val pieceAdapter: UiPieceAdapter,
    private val postPlayProcedures: (UiBoard) -> UiBoard,
) {
    fun applyPlay(board: UiBoard, play: Play?): UiBoard {
        return if (play == null) board
        else {
            val boardAfterPlay = adapt(play, board).execute()
            postPlayProcedures(boardAfterPlay) // TODO: Queen image loads after moving promoted pawn
        }
    }

    private fun adapt(action: Action, board: UiBoard): UiAdaptedAction {
        return when (action) {
            is Move -> adapt(move = action, board)
            is Play -> adapt(play = action, board)
            is Take -> adapt(take = action, board)
        }
    }

    private fun adapt(
        move: Move,
        uiBoard: UiBoard,
    ): UiAdaptedMove {
        val from = Position(move.from.row, move.from.col)
        val to = Position(move.to.row, move.to.col)
        val id = uiBoard[from]!!.id
        val pieceNextTurn = pieceAdapter.adapt(move.pieceNextTurn, id, to)

        return UiAdaptedMove(from, to, uiBoard, pieceNextTurn)
    }

    private fun adapt(
        play: Play,
        uiBoard: UiBoard,
    ): UiAdaptedPlay {
        val adaptedActions =
            play.actions
                .map { adapt(it, uiBoard) }

        return UiAdaptedPlay(adaptedActions)
    }

    private fun adapt(
        take: Take,
        uiBoard: UiBoard,
    ): UiAdaptedTake {
        val position = Position(take.position.row, take.position.col)

        return UiAdaptedTake(position, uiBoard)
    }
}