package edu.austral.dissis.chess.engine.exam

import edu.austral.dissis.chess.engine.Piece
import edu.austral.dissis.chess.test.TestPiece

class PieceAdapter(private val map: Map<Piece, TestPiece>) {
    private val reverseMap: Map<TestPiece, Piece> = map.map{ (k, v) -> v to k}.toMap()

    fun adapt(piece: Piece): TestPiece {
        return map[piece] ?: throw IllegalArgumentException(
            "The game board contains a piece which was not defined as a piece type"
        )
    }

    fun adapt(testPiece: TestPiece): Piece {
        return reverseMap[testPiece] ?: throw IllegalArgumentException(
            "The game board contains a piece which was not defined as a piece type"
        )
    }
}

//data class PieceCategory(val player: Player, val pieceType: KClass<out PieceRules>)
