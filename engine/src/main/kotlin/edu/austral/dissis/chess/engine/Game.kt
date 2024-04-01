package edu.austral.dissis.chess.engine

fun main() {
}

class Game(val gameRules: GameRules, val gameBoard: GameBoard) {
    fun movePiece(from: String, to: String) {

    }
}

interface GameRules {
    fun isPieceMovable(position: String): Boolean
    fun isMoveValid(from: String, to: String): Boolean
    // One of the purposes of isMoveValid() is to check if we have gone off-limits
    fun playerReachedWinCondition(player: Player): Boolean
    fun playerIsChecked(player: Player): Boolean
}


