package edu.austral.dissis.chess.checkers

import edu.austral.dissis.chess.checkers.AmericanCheckersPieceType.AMERICAN_KING
import edu.austral.dissis.chess.checkers.AmericanCheckersPieceType.AMERICAN_MAN
import edu.austral.dissis.chess.checkers.CheckersPieceProvider.getAmericanMan
import edu.austral.dissis.chess.engine.Game
import edu.austral.dissis.chess.engine.Player.BLACK
import edu.austral.dissis.chess.engine.Player.WHITE
import edu.austral.dissis.chess.engine.board.BoardParser
import edu.austral.dissis.chess.engine.board.RectangularBoardValidator
import edu.austral.dissis.chess.engine.pieces.PieceType
import edu.austral.dissis.chess.engine.rules.gameflow.StandardGameRules
import edu.austral.dissis.chess.engine.rules.gameflow.postplay.NoPostPlayValidator
import edu.austral.dissis.chess.engine.rules.gameflow.preplay.CannotStayStill
import edu.austral.dissis.chess.engine.rules.gameflow.preplay.CompoundPrePlayValidator
import edu.austral.dissis.chess.engine.rules.gameflow.preplay.PieceOfPlayer
import edu.austral.dissis.chess.engine.rules.gameflow.wincondition.CompoundWinCondition
import edu.austral.dissis.chess.engine.rules.gameflow.wincondition.DeadlockWinCondition
import edu.austral.dissis.chess.engine.rules.gameflow.wincondition.ExtinctionWinCondition
import edu.austral.dissis.chess.ui.AdapterGameEngine
import edu.austral.dissis.chess.ui.UiPieceAdapter

fun getAmericanCheckersEngine(): AdapterGameEngine {
    val board = getAmericanCheckersBoard()

    val pieceAdapter = UiPieceAdapter(getPieceIdMap())

    val gameRules = getAmericanCheckersGameRules()

    val turnManager = getCheckersTurnManager()
    val game = Game(gameRules, board, turnManager)

    return AdapterGameEngine(game, board.validator as RectangularBoardValidator, pieceAdapter)
}

fun getAmericanCheckersGameRules() =
    StandardGameRules(
        CompoundPrePlayValidator(
            CannotStayStill(),
            PieceOfPlayer(),
        ),
        NoPostPlayValidator(),
        CompoundWinCondition(
            ExtinctionWinCondition(),
            DeadlockWinCondition(),
        ),
    )

fun getAmericanCheckersBoard() =
    BoardParser
        .withPieces(mapOf("WM" to getAmericanMan(WHITE), "BM" to getAmericanMan(BLACK)))
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
            """.trimIndent(),
        )

private fun getPieceIdMap(): Map<PieceType, String> {
    return listOf(
        AMERICAN_MAN to "pawn",
        AMERICAN_KING to "king",
    ).toMap()
}
