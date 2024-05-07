package edu.austral.dissis.chess.engine.pieces

interface PieceType

enum class ClassicPieceType : PieceType {
    PAWN,
    ROOK,
    BISHOP,
    QUEEN,
    KNIGHT,
    KING,
}

enum class CapablancaPieceTypes : PieceType {
    ARCHBISHOP,
    CHANCELLOR,
}
