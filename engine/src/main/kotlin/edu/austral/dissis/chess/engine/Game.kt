package edu.austral.dissis.chess.engine

fun main() {
    TODO("Main game declaration (although this may be simulated in tests, actually)")
}

class Game(private val gameRules: GameRules, var gameBoard: GameBoard) {
    fun movePiece(
        from: String,
        to: String,
    ) {
        require(gameBoard.positionExists(from)) { "Invalid board position" }

        val piece = gameBoard.getPieceAt(from)
        require(piece != null) { "There is no piece at this position" }

        require(gameRules.isPieceMovable(from)) { "This piece is not movable" }

        val play = piece.rules.getPlayIfValid(gameBoard, from, to)
        require(play != null) { "This movement is not valid" }

        val gameBoardAfterPlay = play.execute()
        gameBoard = gameBoardAfterPlay
    }
}

interface GameRules {
    fun isPieceMovable(position: String): Boolean

    fun isMoveValid(
        from: String,
        to: String,
    ): Boolean {
        TODO(
            //  One of the purposes of isMoveValid() is to check if we have gone off-limits
            //  We must check as well the piece isn't staying on site
        )
    }

    fun playerReachedWinCondition(player: Player): Boolean

    fun playerIsChecked(player: Player): Boolean
}
