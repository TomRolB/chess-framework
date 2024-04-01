package edu.austral.dissis.chess.engine

interface GameBoard {
    fun isOccupied(position: String): Boolean
    fun getPieceAt(position: String): Piece?
    fun setPieceAt(position: String, piece: Piece)
    fun delPieceAt(position: String)
    fun positionExists(position: String): Boolean
    fun containsPieceOfPlayer(position: String, player: Player): Boolean
    fun getAllPiecesOfPlayer(player: Player): Iterable<Piece>
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
        TODO("Not yet implemented")
    }

    override fun getAllPiecesOfPlayer(player: Player): Iterable<Piece> {
        TODO("Not yet implemented")
    }

}

interface PositionValidator {
    fun positionExists(position: String): Boolean
}