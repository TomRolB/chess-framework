package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.rules.RuleChain
import edu.austral.dissis.chess.rules.standard.gamerules.PrePlayRules

data class GameData (
    val board: GameBoard,
    val turnManager: TurnManager,
    val from: Position,
    val to: Position
)

data class RuleResult(
    val board: GameBoard,
    val play: Play?,
    val engineResult: EngineResult
)

class Game(val gameRules: RuleChain<GameData, RuleResult>,
           var board: GameBoard,
           var turnManager: TurnManager) {

    fun movePiece(from: Position, to: Position): Pair<Play?, EngineResult> {
        val gameData = GameData(board, turnManager, from, to)
        val ruleResult = gameRules.verify(gameData)

        if (ruleResult.engineResult == EngineResult.VALID_MOVE) {
            board = ruleResult.board
            turnManager = turnManager.nextTurn()
        }

        return ruleResult.play to ruleResult.engineResult

//        gameRules.prePlayRules()
//
//        if (!gameRules.prePlayRules(board, playerOnTurn, from, to)) {
//            return null to EngineResult.GENERAL_MOVE_VIOLATION
//        }
//
//        val piece = board.getPieceAt(from)
//        val play = piece!!.rules.getPlayIfValid(board, from, to)
//        if (play == null) {
//            println("This movement does not abide by the piece's rules")
//            return null to EngineResult.PIECE_VIOLATION
//        }
//
//        val gameBoardAfterPlay = play.execute()
//
//        // TODO: make this optional (in the extinction variant,
//        //  there is no check). Actually, should be a post-play procedure
//        if (KingPieceRules.isChecked(gameBoardAfterPlay, playerOnTurn)) {
//            println("Invalid movement: this would leave your king checked")
//            return null to EngineResult.POST_PLAY_VIOLATION
//        }
//
//        val gameBoardAfterProcedures = gameRules.postPlayRules(gameBoardAfterPlay, piece, to)
//
//        val enemyPlayerState: PlayerState = KingPieceRules.getPlayerState(gameBoardAfterProcedures, !playerOnTurn)
//
//        if (gameRules.playerReachedWinCondition(!playerOnTurn, enemyPlayerState)) {
//            println("${!playerOnTurn} wins!")
//            return play to (
//                if (playerOnTurn == Player.WHITE) EngineResult.WHITE_WINS else EngineResult.BLACK_WINS
//            )
//        }
//
//        if (gameRules.wasTieReached(!playerOnTurn, enemyPlayerState)) {
//            println("It's a tie!")
//            return play to EngineResult.TIE
//        }
//
//        board = gameBoardAfterProcedures
//        turnManager = turnManager.nextTurn()
//
//        return play to EngineResult.VALID_MOVE
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

                if (!gameRules.prePlayRules(board, playerOnTurn, from, to)) continue

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

                val gameBoardAfterProcedures = gameRules.postPlayRules(gameBoardAfterPlay, piece, to)

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

    fun prePlayRules(
        board: GameBoard,
        player: Player,
        from: Position,
        to: Position

    ): RuleChain<GameBoard, Pair<Play?, EngineResult>>

    fun playerReachedWinCondition(player: Player, enemyState: PlayerState) : RuleChain<GameBoard, Pair<Play?, EngineResult>>
    fun wasTieReached(playerOnTurn: Player, enemyState: PlayerState) : RuleChain<GameBoard, Pair<Play?, EngineResult>>

    fun postPlayRules(
        board: GameBoard,
        piece: Piece,
        finalPosition: Position
    ): RuleChain<GameBoard, Pair<Play?, EngineResult>>
}

class TestableStandardGameRules : GameRules {
    //TODO: Why kept as "Testable"?

    override fun prePlayRules(board: GameBoard, player: Player, from: Position, to: Position): Boolean {
        return PrePlayRules(board, from, to, player).verify()
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

    override fun postPlayRules(
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