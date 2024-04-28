package edu.austral.dissis.chess.engine.exam

import edu.austral.dissis.chess.test.TestBoard
import edu.austral.dissis.chess.test.TestPiece
import edu.austral.dissis.chess.test.TestPosition

// This interface and all its implementations mirror
// the engine's Action classes
sealed interface AdaptedAction {
    fun execute(): TestBoard

    fun setBoard(newBoard: TestBoard): AdaptedAction
}

class AdaptedPlay(private val actions: Iterable<AdaptedAction>) : AdaptedAction {
    override fun execute(): TestBoard {
        return actions
            .reduce { action, next -> next.setBoard(action.execute()) }
            .execute()
    }

    override fun setBoard(newBoard: TestBoard): AdaptedPlay {
        return AdaptedPlay(actions)
    }
}

class AdaptedMove(
    private val from: TestPosition,
    val to: TestPosition,
    private val board: TestBoard,
    private val pieceNextTurn: TestPiece,
) : AdaptedAction {
    override fun execute(): TestBoard {
        val piecesAfter =
            board.pieces
                .plus(to to pieceNextTurn)
                .minus(from)

        return TestBoard(board.size, piecesAfter)
    }

    override fun setBoard(newBoard: TestBoard): AdaptedMove {
        return AdaptedMove(from, to, newBoard, pieceNextTurn)
    }
}

class AdaptedTake(private val position: TestPosition, private val board: TestBoard) : AdaptedAction {
    override fun execute(): TestBoard {
        val piecesAfter = board.pieces.minus(position)
        return TestBoard(board.size, piecesAfter)
    }

    override fun setBoard(newBoard: TestBoard): AdaptedAction {
        return AdaptedTake(position, newBoard)
    }
}
