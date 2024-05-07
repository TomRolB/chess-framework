package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position

class MovementData {
    val from: Position
    val to: Position

    val fromRow: Int
    val fromCol: Int

    val toRow: Int
    val toCol: Int

    val rowDelta: Int
    val colDelta: Int

    constructor(from: Position, to: Position) {
        this.from = from
        this.to = to

        val fromRowAndCol = from
        this.fromRow = fromRowAndCol.row
        this.fromCol = fromRowAndCol.col

        val toRowAndCol = to
        this.toRow = toRowAndCol.row
        this.toCol = toRowAndCol.col

        this.rowDelta = toRow - fromRow
        this.colDelta = toCol - fromCol
    }

    constructor(from: Position, to: Position, board: GameBoard, player: Player) {
        this.from = from
        this.to = to

        val fromRowAndCol = from
        this.fromRow = fromRowAndCol.row
        this.fromCol = fromRowAndCol.col

        val toRowAndCol = to
        this.toRow = toRowAndCol.row
        this.toCol = toRowAndCol.col

        val fromRowAsWhite = board.getRowAsWhite(from, player)
        val toRowAsWhite = board.getRowAsWhite(to, player)

        this.rowDelta = toRowAsWhite - fromRowAsWhite
        this.colDelta = toCol - fromCol
    }
}
