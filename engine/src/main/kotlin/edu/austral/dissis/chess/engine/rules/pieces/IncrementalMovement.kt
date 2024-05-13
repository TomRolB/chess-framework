package edu.austral.dissis.chess.engine.rules.pieces

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult

class IncrementalMovement : PieceRule {
    val rowDelta: Int
    val colDelta: Int

    constructor(rowDelta: Int, colDelta: Int) {
        this.rowDelta = rowDelta
        this.colDelta = colDelta
    }

    constructor(rowDelta: Int, colDelta: Int, mirrorForPlayer: Player) {
        this.rowDelta = if (mirrorForPlayer == Player.WHITE) rowDelta else -rowDelta
        this.colDelta = colDelta
    }

    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        val to = Position(position.row + rowDelta, position.col + colDelta)

        return getPlayResult(board, position, to)
            .play
            ?.let { listOf(it) }
            ?: emptyList()
    }

    override fun getPlayResult(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val moveData = MovementData(from, to)

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
