package edu.austral.dissis.chess.engine

// TODO: Consider implementing an interface
enum class Player {
    BLACK,
    WHITE,
}

operator fun Player.not(): Player {
    return when (this) {
        Player.BLACK -> Player.WHITE
        Player.WHITE -> Player.BLACK
    }
}
