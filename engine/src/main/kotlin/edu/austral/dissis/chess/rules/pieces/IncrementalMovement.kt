package edu.austral.dissis.chess.rules.pieces

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult

class IncrementalMovement : PieceRule {
    val rowDelta: Int
    val colDelta: Int
    val player: Player?

    constructor(rowDelta: Int, colDelta: Int) {
        this.rowDelta = rowDelta
        this.colDelta = colDelta
        this.player = null
    }

    constructor(rowDelta: Int, colDelta: Int, player: Player) {
        this.rowDelta = rowDelta
        this.colDelta = colDelta
        this.player = player
    }

    override fun getValidPlays(board: ChessBoard, position: Position): Iterable<Play> {
        val to = Position(position.row + rowDelta, position.col + colDelta)

        return getPlayIfValid(board, position, to)
            .play
            ?.let { listOf(it) }
            ?: emptyList()
    }

    override fun getPlayIfValid(
        board: ChessBoard,
        from: Position,
        to: Position
    ): PlayResult {
        val moveData =
            if (player != null) MovementData(from, to, board, player)
            else MovementData(from, to)

        val isRowDeltaValid = moveData.rowDelta == rowDelta
        val isColDeltaValid = moveData.colDelta == colDelta
        val isPlayValid = isRowDeltaValid && isColDeltaValid

        return PlayResult(
            play = Move(from, to, board).asPlay().takeIf{ isPlayValid },
            message = if (isPlayValid) "Valid play" else "Piece cannot move this way"
        )
    }
}