package edu.austral.dissis.chess.checkers

import edu.austral.dissis.chess.checkers.CheckersPieceProvider.getMan
import edu.austral.dissis.chess.checkers.CheckersPieceType.KING
import edu.austral.dissis.chess.checkers.CheckersPieceType.MAN
import edu.austral.dissis.chess.checkers.rules.ViolatesCompulsoryJumps
import edu.austral.dissis.chess.checkers.rules.ViolatesPendingJumps
import edu.austral.dissis.chess.engine.Game
import edu.austral.dissis.chess.engine.Player.*
import edu.austral.dissis.chess.engine.board.BoardParser
import edu.austral.dissis.chess.engine.board.RectangularBoardValidator
import edu.austral.dissis.chess.engine.pieces.PieceType
import edu.austral.dissis.chess.engine.rules.gameflow.StandardGameRules
import edu.austral.dissis.chess.engine.rules.gameflow.postplay.NoPostPlayValidator
import edu.austral.dissis.chess.engine.rules.gameflow.preplay.CompoundPrePlayValidator
import edu.austral.dissis.chess.engine.rules.gameflow.preplay.NoPieceOfPlayer
import edu.austral.dissis.chess.engine.rules.gameflow.preplay.StaysStill
import edu.austral.dissis.chess.engine.rules.gameflow.wincondition.ExtinctionWinCondition
import edu.austral.dissis.chess.ui.StandardGameEngine
import edu.austral.dissis.chess.ui.UiPieceAdapter

fun getCheckersEngine(): StandardGameEngine {
    val board = getClassicInitialBoard()

    val pieceAdapter = UiPieceAdapter(getPieceIdMap())

    val gameRules = getCheckersGameRules()

    val turnManager = getCheckersTurnManager()
    val game = Game(gameRules, board, turnManager)

    return StandardGameEngine(game, board.validator as RectangularBoardValidator, pieceAdapter)
}

fun getCheckersTurnManager() = MultiTurnManager(WHITE)

fun getCheckersGameRules() =
    StandardGameRules(
        CompoundPrePlayValidator(
            StaysStill(),
            NoPieceOfPlayer(),
            ViolatesPendingJumps(),
            ViolatesCompulsoryJumps(),
        ),
        NoPostPlayValidator(),
        ExtinctionWinCondition(),
    )

fun getClassicInitialBoard() =
    BoardParser
        .withPieces(mapOf("WM" to getMan(WHITE), "BM" to getMan(BLACK)))
        .parse(
            """
                |  |WM|  |WM|  |WM|  |WM|
                |WM|  |WM|  |WM|  |WM|  |
                |  |WM|  |WM|  |WM|  |WM|
                |  |  |  |  |  |  |  |  |
                |  |  |  |  |  |  |  |  |
                |BM|  |BM|  |BM|  |BM|  |
                |  |BM|  |BM|  |BM|  |BM|
                |BM|  |BM|  |BM|  |BM|  |
            """.trimIndent()
        )

private fun getPieceIdMap(): Map<PieceType, String> {
    return listOf(
        MAN to "pawn",
        KING to "king",
    ).toMap()
}
