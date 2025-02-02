package edu.austral.dissis.chess.chess.rules

import edu.austral.dissis.chess.chess.pieces.ChessPieceState.MOVED
import edu.austral.dissis.chess.chess.rules.pawn.PawnPathIsBlocked
import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.InvalidPlay
import edu.austral.dissis.chess.engine.pieces.PlayResult
import edu.austral.dissis.chess.engine.pieces.ValidPlay
import edu.austral.dissis.chess.engine.rules.All
import edu.austral.dissis.chess.engine.rules.Not
import edu.austral.dissis.chess.engine.rules.SimpleRule
import edu.austral.dissis.chess.engine.rules.pieces.IncrementalMovement
import edu.austral.dissis.chess.engine.rules.pieces.PieceRule

class MoveTwoPlaces(
    private val player: Player,
) : PieceRule {
    private val previousRule = IncrementalMovement(2, 0, player)

    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        val destination = Position(position.row + previousRule.rowDelta, position.col + previousRule.colDelta)
        val result = getPlayResult(board, position, destination)

        return if (result !is ValidPlay) {
            emptyList()
        } else {
            listOf(result.play)
        }
    }

    override fun getPlayResult(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val moveData = MovementData(from, to, board, player)
        val hasEverMoved = board.getPieceAt(from)!!.hasState(MOVED)
        val result = previousRule.getPlayResult(board, from, to)

        val conditions =
            All(
                SimpleRule(!hasEverMoved),
                Not(PawnPathIsBlocked(board, moveData)),
            )

        return if (result is ValidPlay && !conditions.verify()) {
            InvalidPlay("Cannot move two places")
        } else {
            result
        }
    }
}
