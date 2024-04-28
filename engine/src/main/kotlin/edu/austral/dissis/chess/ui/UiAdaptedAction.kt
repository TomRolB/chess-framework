package edu.austral.dissis.chess.ui

import edu.austral.dissis.chess.gui.ChessPiece
import edu.austral.dissis.chess.gui.Position

sealed interface UiAdaptedAction {
    fun execute(): Map<Position, ChessPiece>
    fun setBoard(newBoard: Map<Position, ChessPiece>): UiAdaptedAction
}

class UiAdaptedPlay(private val actions: Iterable<UiAdaptedAction>) : UiAdaptedAction {
    override fun execute(): Map<Position, ChessPiece> {
        return actions
            .reduce { action, next -> next.setBoard(action.execute()) }
            .execute()
    }

    override fun setBoard(newBoard: Map<Position, ChessPiece>): UiAdaptedPlay {
        return UiAdaptedPlay(actions)
    }
}

class UiAdaptedMove(
    private val from: Position,
    val to: Position,
    private val board: Map<Position, ChessPiece>,
    private val pieceNextTurn: ChessPiece,
) : UiAdaptedAction {
    override fun execute(): Map<Position, ChessPiece> {
        return board
                .plus(to to pieceNextTurn)
                .minus(from)
    }

    override fun setBoard(newBoard: Map<Position, ChessPiece>): UiAdaptedMove {
        return UiAdaptedMove(from, to, newBoard, pieceNextTurn)
    }
}

class UiAdaptedTake(
    private val position: Position,
    private val board: Map<Position, ChessPiece>
) : UiAdaptedAction {
    override fun execute(): Map<Position, ChessPiece> {
        return board.minus(position)
    }

    override fun setBoard(newBoard: Map<Position, ChessPiece>): UiAdaptedAction {
        return UiAdaptedTake(position, newBoard)
    }
}
