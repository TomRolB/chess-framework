package edu.austral.dissis.chess.engine

class Game(private val gameRules: GameRules,
           private var board: GameBoard,
           private val turnManager: TurnManager) {

    fun movePiece(from: Position, to: Position): Pair<Play?, EngineResult> {
        val playerOnTurn: Player = turnManager.getTurn()

        if (!gameRules.isMoveValid(board, playerOnTurn, from, to)) {
            return null to EngineResult.GENERAL_MOVE_VIOLATION
        }

        val piece = board.getPieceAt(from)
        val play = piece!!.rules.getPlayIfValid(board, from, to)
        if (play == null) {
            println("This movement does not abide by the piece's rules")
            return null to EngineResult.PIECE_VIOLATION
        }

        val gameBoardAfterPlay = play.execute()

        // TODO: make this optional (in the extinction variant,
        //  there is no check). Actually, should be a post-play procedure
        if (KingPieceRules.isChecked(gameBoardAfterPlay, playerOnTurn)) {
            println("Invalid movement: this would leave your king checked")
            return null to EngineResult.POST_PLAY_VIOLATION
        }

        val gameBoardAfterProcedures = gameRules.runPostPlayProcedures(gameBoardAfterPlay, piece, to)

        board = gameBoardAfterProcedures


        val playerState: PlayerState = KingPieceRules.getPlayerState(board, playerOnTurn)

        if (gameRules.playerReachedWinCondition(!playerOnTurn, playerState)) {
            println("${!playerOnTurn} wins!")
            return play to (
                if (playerOnTurn == Player.WHITE) EngineResult.WHITE_WINS else EngineResult.BLACK_WINS
            )
        }

        if (gameRules.wasTieReached(!playerOnTurn, playerState)) {
            println("It's a tie!")
            return play to EngineResult.TIE
        }

        return play to EngineResult.VALID_MOVE
    }
}

enum class EngineResult {
    GENERAL_MOVE_VIOLATION,
    PIECE_VIOLATION,
    POST_PLAY_VIOLATION,
    WHITE_WINS,
    BLACK_WINS,
    TIE,
    VALID_MOVE
}

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

                if (!gameRules.isMoveValid(board, playerOnTurn, from, to)) continue

                val piece = board.getPieceAt(from)
                val play = piece!!.rules.getPlayIfValid(board, from, to)
                if (play == null) {
                    println("This movement is not valid")
                    continue
                }

                val gameBoardAfterPlay = play.execute()

                // TODO: make this optional (in the extinction variant,
                //  there is no check)
                if (KingPieceRules.isChecked(gameBoardAfterPlay, playerOnTurn)) {
                    println("Invalid movement: this would leave your king checked")
                    continue
                }

                val gameBoardAfterProcedures = gameRules.runPostPlayProcedures(gameBoardAfterPlay, piece, to)

                board = gameBoardAfterProcedures
                turnManager = turnManager.nextTurn()

                break
            }
        }
    }

    fun movePiece(
        from: Position,
        to: Position,
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
    fun isPieceMovable(position: Position): Boolean {
        TODO(
            // Determines if, given the circumstances, the piece can be moved.
            // Typically, a piece cannot be moved when the king is checked,
            // unless its movements cancels the check.

            // Although, in the end, I think I will need to plug in these rules
            // into each PieceRules instance
        )
    }

    fun isMoveValid(
        board: GameBoard,
        player: Player,
        from: Position,
        to: Position

    ): Boolean

    fun playerReachedWinCondition(player: Player, enemyState: PlayerState): Boolean
    fun wasTieReached(playerOnTurn: Player, enemyState: PlayerState): Boolean

    fun playerIsChecked(player: Player): Boolean
    fun runPostPlayProcedures(board: GameBoard, piece: Piece, finalPosition: Position): GameBoard
}

class TestableStandardGameRules : GameRules {


    override fun isMoveValid(board: GameBoard, player: Player, from: Position, to: Position): Boolean {
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

        if (!board.containsPieceOfPlayer(from, player)) {
            println("This position does not hold any piece of yours")
            return false
        }

        val piece = board.getPieceAt(from)!!

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

    override fun runPostPlayProcedures(
        board: GameBoard,
        piece: Piece,
        finalPosition: Position,
    ): GameBoard {
        val (_, col) = finalPosition
        val rowAsWhite: Int = board.getRowAsWhite(finalPosition, piece.player)
        val positionAsWhite = Position(rowAsWhite, col)

        if (piece.rules is PawnPieceRules && board.isPositionOnUpperLimit(positionAsWhite)) {
            val promotionPiece = Piece(piece.player, QueenPieceRules(piece.player))

            return board.setPieceAt(finalPosition, promotionPiece)
        }
        else return board
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
    fun requestPlayerMove(player: Player): Pair<Position, Position>
    fun requestPromotionPiece(player: Player): PieceRules
}