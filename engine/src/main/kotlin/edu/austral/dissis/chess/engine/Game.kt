package edu.austral.dissis.chess.engine

fun main() {
}

class Game(val gameRules: GameRules, val gameBoard: GameBoard) {
    fun movePiece(from: String, to: String) {
        if (!gameBoard.positionExists(from)) {
            println("Invalid board position")
            return
        }

        val piece = gameBoard.getPieceAt(from)
        if (piece == null) {
            println("There is no piece at this position")
            return
        }
        if (!gameRules.isPieceMovable(from)) {
            println("This piece is not movable")
            return
        }
        val play = piece.getPlayIfValid(from, to)
        if (play == null) {
            println("This movement is not valid")
            return
        }
        play.execute()
    }
}

interface GameRules {
    fun isPieceMovable(position: String): Boolean
    fun isMoveValid(from: String, to: String): Boolean
    // One of the purposes of isMoveValid() is to check if we have gone off-limits
    fun playerReachedWinCondition(player: Player): Boolean
    fun playerIsChecked(player: Player): Boolean
}




