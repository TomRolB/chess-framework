package edu.austral.dissis.chess.engine.pieces

interface PieceState

enum class ClassicPieceState : PieceState {
    MOVED,
    MOVED_TWO_PLACES
}