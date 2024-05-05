package edu.austral.dissis.chess.engine.pieces

import edu.austral.dissis.chess.engine.ClassicMoveType
import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position

class Bishop(val player: Player) : PieceRule {
    private val moveType = ClassicMoveType.DIAGONAL

    override fun getValidPlays(
        board: ChessBoard,
        position: Position,
    ): Iterable<Play> {
        return getValidPlaysFromMoveType(moveType, board, position, player)
    }

    override fun getPlayIfValid(
        board: ChessBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val moveData = MovementData(from, to)

        return when {
            moveType.isViolated(moveData)
            -> PlayResult(null, "A bishop cannot move this way")
            moveType.isPathBlocked(moveData, board)
            -> PlayResult(null, "Cannot move there: the path is blocked")
            else -> PlayResult(Move(from, to, board).asPlay(), "Valid move")
        }

//        return IndependentRuleChain(
//            !moveType.isViolated(moveData)
//                    to PlayResult(null, "A bishop cannot move this way"),
//            !moveType.isPathBlocked(moveData, board)
//                    to PlayResult(null, "Cannot move there: the path is blocked"),
//            false
//                    to PlayResult(Move(from, to, board).asPlay(), "Valid move")
//        )
//            .onSuccess(PlayResult(Move(from, to, board).asPlay(), "Valid move"))
//            .verify()!!
    }
}
