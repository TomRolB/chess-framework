package edu.austral.dissis.chess.engine.exam

import edu.austral.dissis.chess.chess.board.HashGameBoard
import edu.austral.dissis.chess.engine.EngineResult.BLACK_WINS
import edu.austral.dissis.chess.engine.EngineResult.GENERAL_MOVE_VIOLATION
import edu.austral.dissis.chess.engine.EngineResult.PIECE_VIOLATION
import edu.austral.dissis.chess.engine.EngineResult.POST_PLAY_VIOLATION
import edu.austral.dissis.chess.engine.EngineResult.TIE_BY_BLACK
import edu.austral.dissis.chess.engine.EngineResult.TIE_BY_WHITE
import edu.austral.dissis.chess.engine.EngineResult.VALID_MOVE
import edu.austral.dissis.chess.engine.EngineResult.WHITE_WINS
import edu.austral.dissis.chess.engine.Game
import edu.austral.dissis.chess.engine.GameData
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.board.RectangularBoardValidator
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.rules.RuleChain
import edu.austral.dissis.chess.engine.turns.TurnManager
import edu.austral.dissis.chess.test.TestBoard
import edu.austral.dissis.chess.test.TestPosition
import edu.austral.dissis.chess.test.TestSize
import edu.austral.dissis.chess.test.game.BlackCheckMate
import edu.austral.dissis.chess.test.game.TestGameRunner
import edu.austral.dissis.chess.test.game.TestMoveDraw
import edu.austral.dissis.chess.test.game.TestMoveFailure
import edu.austral.dissis.chess.test.game.TestMoveResult
import edu.austral.dissis.chess.test.game.TestMoveSuccess
import edu.austral.dissis.chess.test.game.WhiteCheckMate

class TestFrameworkGameRunner : TestGameRunner {
    private val actionAdapter: ActionAdapter
    private val pieceAdapter: PieceAdapter

    private val gameRules: RuleChain<GameData, RuleResult>
    private val turnManager: TurnManager

    private lateinit var game: Game
    private lateinit var testBoard: TestBoard

    // Lazy constructor to initialize game once withBoard() is called
    constructor(
        pieceAdapter: PieceAdapter,
        gameRules: RuleChain<GameData, RuleResult>,
        turnManager: TurnManager,
    ) {
        this.pieceAdapter = pieceAdapter
        this.actionAdapter = ActionAdapter(pieceAdapter)
        this.gameRules = gameRules
        this.turnManager = turnManager
    }

    // Actual game initializer
    constructor(
        game: Game,
        testBoard: TestBoard,
        pieceAdapter: PieceAdapter,
        actionAdapter: ActionAdapter,
    ) {
        this.game = game
        this.gameRules = game.gameRules
        this.turnManager = game.turnManager
        this.testBoard = testBoard
        this.pieceAdapter = pieceAdapter
        this.actionAdapter = actionAdapter
    }

    override fun executeMove(
        from: TestPosition,
        to: TestPosition,
    ): TestMoveResult {
        val (ruleResult, newGame) = game.movePiece(adapt(from), adapt(to))
        val play = ruleResult.play
        val engineResult = ruleResult.engineResult

        val boardAfterMove: TestBoard = actionAdapter.applyPlay(testBoard, play)

        return when (engineResult) {
            GENERAL_MOVE_VIOLATION, PIECE_VIOLATION, POST_PLAY_VIOLATION -> {
                TestMoveFailure(boardAfterMove)
            }
            WHITE_WINS -> WhiteCheckMate(boardAfterMove)
            BLACK_WINS -> BlackCheckMate(boardAfterMove)
            TIE_BY_WHITE, TIE_BY_BLACK -> TestMoveDraw(boardAfterMove)
            VALID_MOVE -> {
                this.game = newGame
                this.testBoard = boardAfterMove

                TestMoveSuccess(this)
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
        val engineBoard: GameBoard = initEngineBoard(board)
        val game = Game(gameRules, engineBoard, turnManager)

        return TestFrameworkGameRunner(game, board, pieceAdapter, actionAdapter)
    }

    private fun initEngineBoard(board: TestBoard): GameBoard {
        val validator = RectangularBoardValidator(board.size.rows, board.size.cols)
        val piecePositions = getEnginePieces(board)

        return HashGameBoard.build(validator, piecePositions)
    }

    private fun getEnginePieces(board: TestBoard): List<Pair<Position, Piece>> {
        return board.pieces.map { (testPos, testPiece) ->
            val position = Position(testPos.row, testPos.col)
            val piece = pieceAdapter.adapt(testPiece)
            position to piece
        }
    }

    override fun redo(): TestMoveResult {
        val nextGame = game.redo()
        val nextTestBoard = getTestBoard(nextGame.board)
        testBoard = nextTestBoard
        game = nextGame

        return TestMoveSuccess(this)
    }

    override fun undo(): TestMoveResult {
        val previousGame = game.undo()
        val previousTestBoard = getTestBoard(previousGame.board)
        testBoard = previousTestBoard
        game = previousGame

        return TestMoveSuccess(this)
    }

    private fun getTestBoard(board: GameBoard): TestBoard {
        val testPieces =
            board.getAllPositions()
                .associate { TestPosition(it.row, it.col) to pieceAdapter.adapt(board.getPieceAt(it)!!) }

        require(board is HashGameBoard && board.validator is RectangularBoardValidator) {
            "Only rectangular boards are accepted"
        }

        return TestBoard(
            TestSize(
                (board.validator as RectangularBoardValidator).numberRows,
                (board.validator as RectangularBoardValidator).numberCols,
            ),
            testPieces,
        )
    }
}
