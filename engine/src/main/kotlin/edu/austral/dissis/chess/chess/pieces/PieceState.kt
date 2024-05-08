package edu.austral.dissis.chess.chess.pieces

interface PieceState

enum class ClassicPieceState : PieceState {
    MOVED,
    MOVED_TWO_PLACES,
}
