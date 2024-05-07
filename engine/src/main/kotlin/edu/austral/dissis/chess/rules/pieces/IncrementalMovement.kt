package edu.austral.dissis.chess.rules.pieces

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult

// TODO: may be replaced by PathMovementRules limited by 1 space
//  or not really, since it would be inefficient, and knight should jump.
class IncrementalMovement : PieceRule {
    val rowDelta: Int
    val colDelta: Int
    val playerToBeMirrored: Player?

    constructor(rowDelta: Int, colDelta: Int) {
        this.rowDelta = rowDelta
        this.colDelta = colDelta
        this.playerToBeMirrored = null
    }

    constructor(rowDelta: Int, colDelta: Int, player: Player) {
        this.rowDelta = rowDelta
        this.colDelta = colDelta
        this.playerToBeMirrored = player
    }

    override fun getValidPlays(
        board: ChessBoard,
        position: Position,
    ): Iterable<Play> {
        val to = Position(position.row + rowDelta, position.col + colDelta)

        return getPlayResult(board, position, to)
            .play
            ?.let { listOf(it) }
            ?: emptyList()
    }

    override fun getPlayResult(
        board: ChessBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val moveData =
            if (playerToBeMirrored != null) {
                MovementData(from, to, board, playerToBeMirrored)
            } else {
                MovementData(from, to)
            }

        val isRowDeltaValid = moveData.rowDelta == rowDelta
        val isColDeltaValid = moveData.colDelta == colDelta

        return when {
            !isRowDeltaValid || !isColDeltaValid -> PlayResult(null, "Piece cannot move that way")
            !board.positionExists(from) -> PlayResult(null, "Initial position does not exist")
            !board.positionExists(to) -> PlayResult(null, "Final position does not exist")
            else ->
                PlayResult(
                    play = Move(from, to, board).asPlay(),
                    message = "Valid play",
                )
        }
    }
}
