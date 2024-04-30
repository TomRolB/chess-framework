package edu.austral.dissis.chess.engine.exam

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.Game
import edu.austral.dissis.chess.engine.GameData
import edu.austral.dissis.chess.engine.GameResult
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.TurnManager
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.HashChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.board.RectangleBoardValidator
import edu.austral.dissis.chess.engine.pieces.King
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.rules.RuleChain
import edu.austral.dissis.chess.test.TestBoard
import edu.austral.dissis.chess.test.TestPosition
import edu.austral.dissis.chess.test.game.BlackCheckMate
import edu.austral.dissis.chess.test.game.TestGameRunner
import edu.austral.dissis.chess.test.game.TestMoveDraw
import edu.austral.dissis.chess.test.game.TestMoveFailure
import edu.austral.dissis.chess.test.game.TestMoveResult
import edu.austral.dissis.chess.test.game.TestMoveSuccess
import edu.austral.dissis.chess.test.game.WhiteCheckMate

class AdapterTestGameRunner : TestGameRunner {
    private val actionAdapter: ActionAdapter
    private val pieceAdapter: PieceAdapter

    private val gameRules: RuleChain<GameData, GameResult>
    private val turnManager: TurnManager

    private lateinit var game: Game
    private lateinit var testBoard: TestBoard

    // Lazy constructor to initialize game once withBoard() is called
    constructor(
        pieceAdapter: PieceAdapter,
        postPlayProcedures: (TestBoard) -> TestBoard,
        gameRules: RuleChain<GameData, GameResult>,
        turnManager: TurnManager,
    ) {
        this.pieceAdapter = pieceAdapter
        this.actionAdapter = ActionAdapter(pieceAdapter, postPlayProcedures)
        this.gameRules = gameRules
        this.turnManager = turnManager
    }

    // Actual game initializer
    constructor(game: Game, testBoard: TestBoard, pieceAdapter: PieceAdapter, actionAdapter: ActionAdapter) {
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
        val ruleResult = game.movePiece(adapt(from), adapt(to))
        val play = ruleResult.play
        val engineResult = ruleResult.engineResult

        val boardAfterMove: TestBoard = actionAdapter.applyPlay(testBoard, play)

        return when (engineResult) {
            EngineResult.GENERAL_MOVE_VIOLATION, EngineResult.PIECE_VIOLATION, EngineResult.POST_PLAY_VIOLATION -> {
                TestMoveFailure(boardAfterMove)
            }
            EngineResult.WHITE_WINS -> WhiteCheckMate(boardAfterMove)
            EngineResult.BLACK_WINS -> BlackCheckMate(boardAfterMove)
            EngineResult.TIE_BY_WHITE, EngineResult.TIE_BY_BLACK -> TestMoveDraw(boardAfterMove)
            EngineResult.VALID_MOVE -> {
                val adapterNextTurn = AdapterTestGameRunner(game, boardAfterMove, pieceAdapter, actionAdapter)
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
        // return AdapterTestGameRunner(game, board, pieceTypes)

        // Set pieces at Game (also map board size to validator)
        // (board is initialized here)

        val engineBoard: ChessBoard = initEngineBoard(board)
        val game = Game(gameRules, engineBoard, turnManager)

        return AdapterTestGameRunner(game, board, pieceAdapter, actionAdapter)
    }

    private fun initEngineBoard(board: TestBoard): ChessBoard {
        val validator = RectangleBoardValidator(board.size.rows, board.size.cols)
        val piecePositions = getEnginePieces(board)
        val whiteKingPosition =
            piecePositions
                .find { it.second.type is King && it.second.player == Player.WHITE }
                ?.first
                ?: throw IllegalArgumentException("The board must be initialized with a white king")
        val blackKingPosition =
            piecePositions
                .find { it.second.type is King && it.second.player == Player.BLACK }
                ?.first
                ?: throw IllegalArgumentException("The board must be initialized with a white king")

        return HashChessBoard.build(validator, piecePositions, whiteKingPosition, blackKingPosition)
    }

    private fun getEnginePieces(board: TestBoard): List<Pair<Position, Piece>> {
        return board.pieces.map { (testPos, testPiece) ->
            val position = Position(testPos.row, testPos.col)
            val piece = pieceAdapter.adapt(testPiece)
            position to piece
        }
    }
}
