package edu.austral.dissis.chess.engine.pieces

import edu.austral.dissis.chess.engine.ClassicMoveType
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.Position

class Queen(val player: Player) : PieceType {
    private val moveType = ClassicMoveType.ANY_STRAIGHT_LINE

    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        return getValidPlaysFromMoveType(moveType, board, position, player)
    }

    override fun getPlayIfValid(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val moveData = MovementData(from, to)

        return when {
            moveType.isViolated(moveData)
            -> PlayResult(null, "A queen cannot move this way")
            moveType.isPathBlocked(moveData, board)
            -> PlayResult(null, "Cannot move there: the path is blocked")
            else -> PlayResult(Move(from, to, board).asPlay(), "Valid move")
        }
    }
}