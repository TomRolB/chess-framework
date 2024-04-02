package edu.austral.dissis.chess.engine

fun getStringPosition(row: Int, col: Int): String {
    return String(charArrayOf(col.intToChar(), row.toChar()))
}

fun Char.charToInt(): Int {
    return this.code - 'a'.code + 1
}

fun Int.intToChar(): Char {
    return (this + 'a'.code - 1).toChar()
}