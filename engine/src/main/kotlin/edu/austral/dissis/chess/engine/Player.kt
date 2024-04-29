package edu.austral.dissis.chess.engine

enum class Player {
    BLACK,
    WHITE,
    ;

    override fun toString(): String {
        return when (this) {
            BLACK -> "black"
            WHITE -> "white"
        }
    }
}

enum class PlayerState {
    NORMAL,
    STALEMATE,
    CHECKED,
    CHECKMATE,
}

operator fun Player.not(): Player {
    return when (this) {
        Player.BLACK -> Player.WHITE
        Player.WHITE -> Player.BLACK
    }
}
