package edu.austral.dissis.chess.engine

// Game implementation which can be tested.
// There are some reasons we need a separate implementation:
//      1. The movement tests were defined to use movePiece(),
//         which no longer is necessary. However, replacing
//         movePiece() will need a great deal of refactoring
//      2. 'board' should be private, but this makes it
//          impossible to perform any test
//      3. We add the variables 'winner' and 'endedOnTie'
//         to actually know whether the game ended and how

class TestableGame(private val gameRules: GameRules,
                   var board: GameBoard,
                   private var turnManager: TurnManager,
                   private val inputProvider: PlayerInputProvider) {
    var winner: Player? = null
    var endedOnTie = false

    fun run() {
        // TODO: Modularize. The problem with this method is that
        //  some things are very difficult to handle apart.

        while (true) {
            val playerOnTurn: Player = turnManager.getTurn()

            val playerState: PlayerState = KingPieceRules.getPlayerState(board, playerOnTurn)

            if (gameRules.playerReachedWinCondition(!playerOnTurn, playerState)) {
                println("${!playerOnTurn} wins!")
                winner = !playerOnTurn
                return
            }

            if (gameRules.wasTieReached(!playerOnTurn, playerState)) {
                println("It's a tie!")
                endedOnTie = true
                return
            }

            while (true) {
                val (from, to) = inputProvider.requestPlayerMove(playerOnTurn)

                if (!gameRules.isMoveValid(board, from, to)) continue

                val piece = board.getPieceAt(from)
                val play = piece!!.rules.getPlayIfValid(board, from, to)
                if (play == null) {
                    println("This movement is not valid")
                    continue
                }

                val gameBoardAfterPlay = play.execute()

                if (KingPieceRules.isChecked(gameBoardAfterPlay, playerOnTurn)) {
                    println("Invalid movement: this would leave your king checked")
                    continue
                }

                board = gameBoardAfterPlay
                turnManager = turnManager.nextTurn()

                break
            }
        }
    }

    fun movePiece(
        from: String,
        to: String,
    ) {
        require(board.positionExists(from)) { "Invalid board position" }

        val piece = board.getPieceAt(from)
        require(piece != null) { "There is no piece at this position" }

        val play = piece.rules.getPlayIfValid(board, from, to)
        require(play != null) { "This movement is not valid" }

        val gameBoardAfterPlay = play.execute()
        board = gameBoardAfterPlay
    }
}

interface GameRules {
    fun isPieceMovable(position: String): Boolean {
        TODO(
            // Determines if, given the circumstances, the piece can be moved.
            // Typically, a piece cannot be moved when the king is checked,
            // unless its movements cancels the check.

            // Although, in the end, I think I will need to plug in these rules
            // into each PieceRules instance
        )
    }

    fun isMoveValid(
    //    TODO(
    //     One of the purposes of isMoveValid() is to check if we have gone off-limits
    //      We must check as well the piece isn't staying on site
    //      And if there is a piece belonging to the same player at "to".
    //      Although, in the end, I think I will need to plug in these rules
    //      into each PieceRules instance
    //    )
        board: GameBoard,
        from: String,
        to: String,
    ): Boolean

    fun playerReachedWinCondition(player: Player, enemyState: PlayerState): Boolean
    fun wasTieReached(playerOnTurn: Player, enemyState: PlayerState): Boolean

    fun playerIsChecked(player: Player): Boolean
}

class StandardGameRules : GameRules {
    override fun isMoveValid(board: GameBoard, from: String, to: String): Boolean {
        if (from == to) {
            println("'from' and 'to' must be different")
            return false
        }
        if (!board.positionExists(from)) {
            println("Invalid board position: '${from}'")
            return false
        }
        if (!board.positionExists(to)) {
            println("Invalid board position: '${to}'")
            return false
        }

        val piece = board.getPieceAt(from)
        if (piece == null) {
            println("There is no piece at this position")
            return false
        }

//            if (gameRules.isPieceMovable(from)) {
//                println("This piece is not movable")
//                return false
//            }

        if (board.containsPieceOfPlayer(to, piece.player)) {
            println("Cannot move to square containing ally piece")
            return false
        }

        return true
    }

    override fun playerReachedWinCondition(player: Player, enemyState: PlayerState): Boolean {
        return enemyState == PlayerState.CHECKMATE
    }

    override fun wasTieReached(playerOnTurn: Player, enemyState: PlayerState): Boolean {
        return enemyState == PlayerState.STALEMATE
    }

    override fun playerIsChecked(player: Player): Boolean {
        TODO("Not yet implemented")
    }

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