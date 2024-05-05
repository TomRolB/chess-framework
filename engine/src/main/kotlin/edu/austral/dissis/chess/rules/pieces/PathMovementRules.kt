package edu.austral.dissis.chess.rules.pieces

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.MoveType
import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult

class PathMovementRules(val moveType: MoveType) : PieceRule {
    override fun getValidPlays(board: ChessBoard, position: Position): Iterable<Play> {
        return moveType
            .getPossiblePositions(board, position)
            .map {
                Move(position, it, board).asPlay()
            }
    }

    override fun getPlayIfValid(board: ChessBoard, from: Position, to: Position): PlayResult {
        val moveData = MovementData(from, to)
        return when {
            moveType.isViolated(moveData) -> PlayResult(null, "Piece cannot move this way")
            moveType.isPathBlocked(moveData, board) -> PlayResult(null, "Cannot move there: the path is blocked")
            else -> PlayResult(Move(from, to, board).asPlay(), "Valid move")
        }

        //TODO: problem is now how to maintain state for rook, for instance.
        // Set of attributes?
    }
}