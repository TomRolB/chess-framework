package edu.austral.dissis.chess.engine.rules.pieces

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.InvalidPlay
import edu.austral.dissis.chess.engine.pieces.PlayResult
import edu.austral.dissis.chess.engine.pieces.ValidPlay

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

        return listOf(getPlayResult(board, position, to))
            .filterIsInstance<ValidPlay>()
            .map{ it.play }
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
            !isRowDeltaValid || !isColDeltaValid -> InvalidPlay("Piece cannot move that way")
            !board.positionExists(from) -> InvalidPlay("Initial position does not exist")
            !board.positionExists(to) -> InvalidPlay("Final position does not exist")
            else -> ValidPlay(Move(from, to, board).asPlay())
        }
    }
}
