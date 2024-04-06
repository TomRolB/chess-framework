package edu.austral.dissis.chess.engine

fun getStringPosition(
    row: Int,
    col: Int,
): String {
    return String(charArrayOf(col.colToChar(), row.digitToChar()))
}

fun Char.charToCol(): Int {
    return this.code - 'a'.code + 1
}

fun Int.colToChar(): Char {
    return (this + 'a'.code - 1).toChar()
}

class MovementData {
    val from: String
    val to: String

    val fromRow: Int
    val fromCol: Int

    val toRow: Int
    val toCol: Int

    val rowDelta: Int
    val colDelta: Int

    constructor(from: String, to: String, board: GameBoard) {
        this.from = from
        this.to = to

        val fromRowAndCol = board.unpackPosition(from)
        this.fromRow = fromRowAndCol.row
        this.fromCol = fromRowAndCol.col

        val toRowAndCol = board.unpackPosition(to)
        this.toRow = toRowAndCol.row
        this.toCol = toRowAndCol.col

        this.rowDelta = toRow - fromRow
        this.colDelta = toCol - fromCol
    }

    constructor(from: String, to: String, board: GameBoard, player: Player) {
        this.from = from
        this.to = to

        val fromRowAndCol = board.unpackPosition(from)
        this.fromRow = fromRowAndCol.row
        this.fromCol = fromRowAndCol.col

        val toRowAndCol = board.unpackPosition(to)
        this.toRow = toRowAndCol.row
        this.toCol = toRowAndCol.col

        val fromRowAsWhite = board.getRowAsWhite(from, player)
        val toRowAsWhite = board.getRowAsWhite(to, player)

        this.rowDelta = toRowAsWhite - fromRowAsWhite
        this.colDelta = toCol - fromCol
    }
}
