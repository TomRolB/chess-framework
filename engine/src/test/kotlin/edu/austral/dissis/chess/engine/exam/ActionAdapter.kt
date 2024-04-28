package edu.austral.dissis.chess.engine.exam

import edu.austral.dissis.chess.engine.*
import edu.austral.dissis.chess.test.TestBoard
import edu.austral.dissis.chess.test.TestPosition

class ActionAdapter(
    private val pieceAdapter: PieceAdapter,
    private val postPlayProcedures: (TestBoard) -> TestBoard
) {
    fun applyPlay(testBoard: TestBoard, play: Play?): TestBoard {
        return if (play == null) testBoard
        else {
            val boardAfterPlay = adapt(play, testBoard).execute()
            postPlayProcedures(boardAfterPlay)
        }
    }

    private fun adapt(action: Action, testBoard: TestBoard) : AdaptedAction {
        return when (action) {
            is Move -> adapt(move = action, testBoard)
            is Play -> adapt(play = action, testBoard)
            is Take -> adapt(take = action, testBoard)
        }
    }

    private fun adapt(move: Move, testBoard: TestBoard) : AdaptedMove {
        val from = TestPosition(move.from.row, move.from.col)
        val to = TestPosition(move.to.row, move.to.col)
        val pieceNextTurn = pieceAdapter.adapt(move.pieceNextTurn)

        return AdaptedMove(from, to, testBoard, pieceNextTurn)
    }

    private fun adapt(play: Play, testBoard: TestBoard): AdaptedPlay {
        val adaptedActions = play.actions
            .map { adapt(it, testBoard) }

        return AdaptedPlay(adaptedActions)
    }

    private fun adapt(take: Take, testBoard: TestBoard): AdaptedTake {
        val position = TestPosition(take.position.row, take.position.col)

        return AdaptedTake(position, testBoard)
    }
}

