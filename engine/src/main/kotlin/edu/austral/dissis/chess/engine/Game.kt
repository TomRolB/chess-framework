package edu.austral.dissis.chess.engine

class Game(private val gameRules: GameRules,
           var gameBoard: GameBoard,
           private val turnManager: TurnManager,
           private val inputProvider: PlayerInputProvider) {
    fun run() {
        while (true) {
            val playerOnTurn: Player = turnManager.getTurn()

            //TODO: is player checked? On stalemate? On checkmate? Win condition?
            // Problem: I actually cannot call isChecked() at WinCondition.
            // I would like to have the game state (normal, check, stalemate, checkmate)
            // here, since there are some operations which need it.

            val playerState: PlayerState = KingPieceRules.getPlayerState(gameBoard, playerOnTurn)

            when (playerState) {
                PlayerState.NORMAL -> TODO()
                PlayerState.CHECKED -> TODO()
                PlayerState.STALEMATE -> {
                    println()
                }
                PlayerState.CHECKMATE -> TODO()
            }

            while (true) {
                val (from, to) = inputProvider.requestPlayerMove(playerOnTurn)
                try {
                    movePiece(from, to)
                } catch (e: IllegalArgumentException) {
                    continue
                }
                break
            }
        }
    }

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
    fun isPieceMovable(position: String): Boolean {
        TODO(
            // Determines if, given the circumstances, the piece can be moved.
            // Typically, a piece cannot be moved when the king is checked,
            // unless its movements cancels the check.
        )
    }

    fun isMoveValid(
        from: String,
        to: String,
    ): Boolean {
        TODO(
            //  One of the purposes of isMoveValid() is to check if we have gone off-limits
            //  We must check as well the piece isn't staying on site
            //  And if there is a piece belonging to the same player at "to"
        )
    }

    fun playerReachedWinCondition(player: Player): Boolean

    fun playerIsChecked(player: Player): Boolean
}

interface TurnManager {
    fun getTurn(): Player
    fun nextTurn(): TurnManager
}

class OneToOneTurnManager: TurnManager {
    private val currentTurn: Player

    constructor () {
        this.currentTurn = Player.WHITE
    }

    private constructor(player: Player) {
        this.currentTurn = player
    }

    override fun getTurn(): Player {
        return currentTurn
    }

    override fun nextTurn(): TurnManager {
        return OneToOneTurnManager(!currentTurn)
    }
}

interface PlayerInputProvider {
    fun requestPlayerMove(player: Player): Pair<String, String>
}
