package edu.austral.dissis.chess.engine.exam

import edu.austral.dissis.chess.engine.EngineResult.*
import edu.austral.dissis.chess.engine.Game
import edu.austral.dissis.chess.engine.GameBoard
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Position
import edu.austral.dissis.chess.test.TestBoard
import edu.austral.dissis.chess.test.TestPosition
import edu.austral.dissis.chess.test.game.*

class AdapterTestGameRunner(val game: Game, val testBoard: TestBoard) : TestGameRunner {
    override fun executeMove(from: TestPosition, to: TestPosition): TestMoveResult {
        val (play, engineResult) = game.movePiece(adapt(from), adapt(to))

        val boardAfterMove : TestBoard = ActionAdapter.getInstance().applyPlay(testBoard, play)

        return when (engineResult) {
            GENERAL_MOVE_VIOLATION, PIECE_VIOLATION, POST_PLAY_VIOLATION -> {
                TestMoveFailure(boardAfterMove)
            }
            WHITE_WINS -> WhiteCheckMate(boardAfterMove)
            BLACK_WINS -> BlackCheckMate(boardAfterMove)
            TIE -> TestMoveDraw(boardAfterMove)
            VALID_MOVE -> TODO()
        }
    }

    private fun applyPlay(boardNow: TestBoard, play: Play?): TestBoard {
        return
    }

    private fun adapt(testPos: TestPosition): Position {
        return Position(testPos.row, testPos.col)
    }

    override fun getBoard(): TestBoard {
        return testBoard
    }

    override fun withBoard(board: TestBoard): TestGameRunner {

    }

}
