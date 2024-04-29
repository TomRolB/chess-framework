package edu.austral.dissis.chess.ui

import edu.austral.dissis.chess.gui.ChessPiece
import edu.austral.dissis.chess.gui.Position

sealed interface UiAdaptedAction {
    fun execute(): UiBoard

    fun setBoard(newBoard: UiBoard): UiAdaptedAction
}

class UiAdaptedPlay(private val actions: Iterable<UiAdaptedAction>) : UiAdaptedAction {
    override fun execute(): UiBoard {
        return actions
            .reduce { action, next -> next.setBoard(action.execute()) }
            .execute()
    }

    override fun setBoard(newBoard: UiBoard): UiAdaptedPlay {
        return UiAdaptedPlay(actions)
    }
}

class UiAdaptedMove(
    private val from: Position,
    val to: Position,
    private val board: UiBoard,
    private val pieceNextTurn: ChessPiece,
) : UiAdaptedAction {
    override fun execute(): UiBoard {
        return board
            .plus(to to pieceNextTurn)
            .minus(from)
    }

    override fun setBoard(newBoard: UiBoard): UiAdaptedMove {
        return UiAdaptedMove(from, to, newBoard, pieceNextTurn)
    }
}

class UiAdaptedTake(
    private val position: Position,
    private val board: UiBoard,
) : UiAdaptedAction {
    override fun execute(): UiBoard {
        return board.minus(position)
    }

    override fun setBoard(newBoard: UiBoard): UiAdaptedAction {
        return UiAdaptedTake(position, newBoard)
    }
}
