package edu.austral.dissis.chess.checkers.rules

import edu.austral.dissis.chess.checkers.rules.CheckersPieceState.CAN_TAKE_ENEMY
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.includesTake
import edu.austral.dissis.chess.engine.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult

class MovesWhenCompulsory(val subRule: PieceRule): PieceRule {
    override fun getValidPlays(board: GameBoard, position: Position): Iterable<Play> {
        return subRule
            .getValidPlays(board, position)
            .filter {
                val piece = board.getPieceAt(position)!!
                piece.hasState(CAN_TAKE_ENEMY) && it.includesTake()
            }
    }

    override fun getPlayResult(board: GameBoard, from: Position, to: Position): PlayResult {
        val pieceBeforePlay = board.getPieceAt(from)!!
        val result = subRule.getPlayResult(board, from, to)

        return when {
            result.play == null -> result
            pieceBeforePlay.hasState(CAN_TAKE_ENEMY) && !pieceActuallyTookAnEnemy(result.play) -> {
                PlayResult(null, "This piece has a compulsory jump")
            }
            else -> result
        }
    }

    private fun pieceActuallyTookAnEnemy(play: Play) = play.includesTake()
}