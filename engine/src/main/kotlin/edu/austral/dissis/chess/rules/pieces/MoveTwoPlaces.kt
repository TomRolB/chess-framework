package edu.austral.dissis.chess.rules.pieces

import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.ClassicPieceState.MOVED
import edu.austral.dissis.chess.engine.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult
import edu.austral.dissis.chess.rules.All
import edu.austral.dissis.chess.rules.Not
import edu.austral.dissis.chess.rules.SimpleRule
import edu.austral.dissis.chess.rules.pieces.pawn.PawnPathIsBlocked

// TODO: could be replaced if MoveType can be passed a limit in a future
class MoveTwoPlaces(
    private val player: Player,
) : PieceRule {
    private val subRule = IncrementalMovement(2, 0, player)

    override fun getValidPlays(
        board: ChessBoard,
        position: Position,
    ): Iterable<Play> {
        val destination = Position(position.row + subRule.rowDelta, position.col + subRule.colDelta)
        val play = getPlayResult(board, position, destination).play

        return if (play == null) {
            emptyList()
        } else {
            listOf(play)
        }
    }

    override fun getPlayResult(
        board: ChessBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val moveData = MovementData(from, to, board, player)
        val hasEverMoved = board.getPieceAt(from)!!.hasState(MOVED)
        val result = subRule.getPlayResult(board, from, to)

        // TODO: turn into normal boolean conditions?
        val conditions =
            All(
                SimpleRule(!hasEverMoved),
                Not(PawnPathIsBlocked(board, moveData)),
            )

        return if (result.play != null && !conditions.verify()) {
            PlayResult(null, "Cannot move two places")
        } else {
            result
        }
    }
}
