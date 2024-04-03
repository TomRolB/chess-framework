package edu.austral.dissis.chess.engine

interface GameBoard {
    fun isOccupied(position: String): Boolean
    fun getPieceAt(position: String): Piece?
    fun setPieceAt(position: String, piece: Piece)
    fun delPieceAt(position: String)
    fun positionExists(position: String): Boolean
    fun containsPieceOfPlayer(position: String, player: Player): Boolean
    fun getAllPiecesOfPlayer(player: Player): Iterable<Piece>
    fun unpackPosition(position: String): RowAndCol
    fun getRowAsWhite(position: String, player: Player): Int
}

class HashGameBoard(val validator: PositionValidator): GameBoard {
    val boardMap: HashMap<String, Piece> = HashMap()

    override fun isOccupied(position: String): Boolean {
        return boardMap[position] != null
    }

    override fun getPieceAt(position: String): Piece? {
        return boardMap[position]
    }

    override fun setPieceAt(position: String, piece: Piece) {
        boardMap[position] = piece
    }

    override fun delPieceAt(position: String) {
        boardMap.remove(position)
    }

    override fun positionExists(position: String): Boolean {
        return validator.positionExists(position)
    }

    override fun containsPieceOfPlayer(position: String, player: Player): Boolean {
        val piece = getPieceAt(position)
        if (piece == null) return false
        return piece.player == player
    }

    override fun getAllPiecesOfPlayer(player: Player): Iterable<Piece> {
        TODO("Not yet implemented")
    }

    override fun unpackPosition(position: String): RowAndCol {
        return validator.unpackPosition(position)
    }

    override fun getRowAsWhite (position: String, player: Player): Int {
        return validator.getRowAsWhite(position, player)
    }
}

data class RowAndCol(val row: Int, val col: Int)

interface PositionValidator {
    fun positionExists(position: String): Boolean
    fun unpackPosition(position: String): RowAndCol
    fun getRowAsWhite(position: String, player: Player): Int
}

class RectangleBoardValidator(val numberRows: Int, val numberCols: Int) : PositionValidator {

    override fun positionExists(position: String): Boolean {
        val (row, col) = unpackPosition(position)
        return (row <= numberRows) && (col <= numberCols)
    }


    override fun getRowAsWhite(position: String, player: Player): Int {
        if (player == Player.WHITE) return position[1].charToCol()

        return numberRows - position[1].charToCol() + 1
    }

    override fun unpackPosition(position: String): RowAndCol {
        val col: Int = position[0].charToCol()
        val row: Int = position[1].digitToInt()
        return RowAndCol(row, col)
    }

}