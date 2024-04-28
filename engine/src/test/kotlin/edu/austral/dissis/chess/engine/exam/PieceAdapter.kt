package edu.austral.dissis.chess.engine.exam

import edu.austral.dissis.chess.engine.Piece
import edu.austral.dissis.chess.test.TestPiece

class PieceAdapter(constructorToTestPiece: Map<() -> Piece, TestPiece>) {
    private val pieceToTestPiece: Map<Piece, TestPiece> =
        constructorToTestPiece.map { (k, v) -> k.invoke() to v }.toMap()

    private val testPieceToConstructor: Map<TestPiece, () -> Piece> =
        constructorToTestPiece.map { (k, v) -> v to k }.toMap()

    fun adapt(piece: Piece): TestPiece {
        return pieceToTestPiece[piece] ?: throw IllegalArgumentException(
            "The game board contains a piece which was not defined as a piece type",
        )
    }

    fun adapt(testPiece: TestPiece): Piece {
        return testPieceToConstructor[testPiece]
            ?.invoke() // get a different instance, to avoid joint state between pieces
            ?: throw IllegalArgumentException(
                "The test board contains a piece ${testPiece.pieceTypeSymbol} which was not defined as a piece type",
            )
    }
}
