package edu.austral.dissis.chess.engine.pieces

import edu.austral.dissis.chess.engine.ClassicMoveType
import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position

class Knight(val player: Player) : PieceRule {
    private val moveType = ClassicMoveType.L_SHAPED

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
            moveType.isViolated(moveData) ->
                PlayResult(null, "A knight cannot move this way")
            else -> PlayResult(Move(from, to, board).asPlay(), "Valid move")
        }
    }
}
