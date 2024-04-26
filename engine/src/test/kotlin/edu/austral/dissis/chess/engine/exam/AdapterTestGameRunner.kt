package edu.austral.dissis.chess.engine.exam

import edu.austral.dissis.chess.engine.*
import edu.austral.dissis.chess.engine.EngineResult.*
import edu.austral.dissis.chess.test.TestBoard
import edu.austral.dissis.chess.test.TestPosition
import edu.austral.dissis.chess.test.game.*

class AdapterTestGameRunner : TestGameRunner {
    private val actionAdapter: ActionAdapter
    private val pieceAdapter: PieceAdapter

    private val gameRules: GameRules
    private val turnManager: TurnManager

    private lateinit var game: Game
    private lateinit var testBoard: TestBoard

    // Lazy constructor to initialize game once withBoard() is called
    constructor(pieceAdapter: PieceAdapter,
                gameRules: GameRules,
                turnManager: TurnManager) {
        this.pieceAdapter = pieceAdapter
        this.actionAdapter = ActionAdapter(pieceAdapter)
        this.gameRules = gameRules
        this.turnManager = turnManager
    }

    // Actual game initializer
    constructor(game: Game, testBoard: TestBoard, pieceAdapter: PieceAdapter) {
        this.game = game
        this.gameRules = game.gameRules
        this.turnManager = game.turnManager
        this.testBoard = testBoard
        this.pieceAdapter = pieceAdapter
        this.actionAdapter = ActionAdapter(pieceAdapter)
    }

    override fun executeMove(from: TestPosition, to: TestPosition): TestMoveResult {
        val (play, engineResult) = game.movePiece(adapt(from), adapt(to))

        val boardAfterMove : TestBoard = actionAdapter.applyPlay(testBoard, play)

        return when (engineResult) {
            GENERAL_MOVE_VIOLATION, PIECE_VIOLATION, POST_PLAY_VIOLATION -> {
                TestMoveFailure(boardAfterMove)
            }
            WHITE_WINS -> WhiteCheckMate(boardAfterMove)
            BLACK_WINS -> BlackCheckMate(boardAfterMove)
            TIE -> TestMoveDraw(boardAfterMove)
            VALID_MOVE -> {
                val adapterNextTurn = AdapterTestGameRunner(game, boardAfterMove, pieceAdapter)
                TestMoveSuccess(adapterNextTurn)
            }
        }
    }

    private fun adapt(testPos: TestPosition): Position {
        return Position(testPos.row, testPos.col)
    }

    override fun getBoard(): TestBoard {
        return testBoard
    }

    override fun withBoard(board: TestBoard): TestGameRunner {
        //return AdapterTestGameRunner(game, board, pieceTypes)

        // Set pieces at Game (also map board size to validator)
        // (board is initialized here)

        val engineBoard: GameBoard = initEngineBoard(board)
        val game = Game(gameRules, engineBoard, turnManager)

        return AdapterTestGameRunner(game, board, pieceAdapter)
    }

    private fun initEngineBoard(board: TestBoard): GameBoard {
        val validator = RectangleBoardValidator(board.size.rows, board.size.cols)
        val piecePositions = getEnginePieces(board)
        val whiteKingPosition =
            piecePositions
                .find { it.second.rules is KingPieceRules && it.second.player == Player.WHITE }
                ?.first
                ?: throw IllegalArgumentException("The board must be initialized with a white king")
        val blackKingPosition =
            piecePositions
                .find { it.second.rules is KingPieceRules && it.second.player == Player.BLACK }
                ?.first
                ?: throw IllegalArgumentException("The board must be initialized with a white king")

        return HashGameBoard.build(validator, piecePositions, whiteKingPosition, blackKingPosition)
    }

    private fun getEnginePieces(board: TestBoard): List<Pair<Position, Piece>> {
        return board.pieces.map { (testPos, testPiece) ->
            val position = Position(testPos.row, testPos.col)
            val piece = pieceAdapter.adapt(testPiece)
            position to piece
        }
    }
}
