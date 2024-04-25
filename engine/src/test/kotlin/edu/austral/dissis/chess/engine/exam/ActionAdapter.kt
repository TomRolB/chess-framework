package edu.austral.dissis.chess.engine.exam;

import edu.austral.dissis.chess.engine.Action
import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play;
import edu.austral.dissis.chess.engine.Take
import edu.austral.dissis.chess.test.TestBoard
import edu.austral.dissis.chess.test.TestPosition

class ActionAdapter private constructor() {
    // Make it singleton, since this class hold no state at all
    companion object {
        var instance: ActionAdapter? = null
        fun getInstance(): ActionAdapter {
            return instance ?: ActionAdapter().also { instance = it }
        }
    }

    private fun adapt(action: Action, testBoard: TestBoard) : AdaptedAction {
        return when (action) {
            is Move -> adapt(move = action, testBoard)
            is Play -> adapt(play = action, testBoard)
            is Take -> adapt(take = action, testBoard)
        }
    }

    fun adapt(move: Move, testBoard: TestBoard) : AdaptedMove {
        val from = TestPosition(move.from.row, move.from.col)
        val to = TestPosition(move.to.row, move.to.col)

        return AdaptedMove(from, to, testBoard, /* adapted move.pieceNextTurn */)
    }

    fun adapt(play: Play, testBoard: TestBoard): AdaptedPlay {
        val adaptedActions = play.actions
            .map { adapt(it, testBoard) }

        return AdaptedPlay(adaptedActions, testBoard)
    }



    fun applyPlay(testBoard: TestBoard, play: Play?): TestBoard {
        return if (play == null) testBoard else adapt(play, testBoard).execute()
    }
}

