package edu.austral.dissis.chess.engine

fun getStringPosition(row: Int, col: Int): String {
    return String(charArrayOf(col.colToChar(), row.digitToChar()))
}

fun Char.charToCol(): Int {
    return this.code - 'a'.code + 1
}

fun Int.colToChar(): Char {
    return (this + 'a'.code - 1).toChar()
}

class MovementData(from: String, to: String, board: GameBoard) {
    private val fromRowAndCol = board.unpackPosition(from)
    val fromRow = fromRowAndCol.row
    val fromCol = fromRowAndCol.col

    private val toRowAndCol = board.unpackPosition(to)
    val toRow = toRowAndCol.row
    val toCol = toRowAndCol.col

    val rowDelta = toRow - fromRow
    val colDelta = toCol - fromCol
}