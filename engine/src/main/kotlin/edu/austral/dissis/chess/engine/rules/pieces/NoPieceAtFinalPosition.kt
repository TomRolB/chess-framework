package edu.austral.dissis.chess.engine.rules.pieces

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.extractMoveAction
import edu.austral.dissis.chess.engine.pieces.InvalidPlay
import edu.austral.dissis.chess.engine.pieces.PlayResult

class NoPieceAtFinalPosition(val subRule: PieceRule) : PieceRule {
    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        return subRule
            .getValidPlays(board, position)
            .filter {
                val finalPosition = it.extractMoveAction().to
                !board.isOccupied(finalPosition)
            }
    }

    override fun getPlayResult(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val playResult = subRule.getPlayResult(board, from, to)

        return if (playResult is InvalidPlay || !board.isOccupied(to)) {
            playResult
        } else {
            InvalidPlay("There is a piece blocking that position")
        }
    }
}
