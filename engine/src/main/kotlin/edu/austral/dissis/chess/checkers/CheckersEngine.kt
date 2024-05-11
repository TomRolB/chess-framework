package edu.austral.dissis.chess.checkers

import edu.austral.dissis.chess.checkers.CheckersPieceProvider.getMan
import edu.austral.dissis.chess.checkers.CheckersPieceType.KING
import edu.austral.dissis.chess.checkers.CheckersPieceType.MAN
import edu.austral.dissis.chess.checkers.rules.ViolatesCompulsoryJumps
import edu.austral.dissis.chess.checkers.rules.ViolatesPendingJumps
import edu.austral.dissis.chess.engine.Game
import edu.austral.dissis.chess.engine.Player.BLACK
import edu.austral.dissis.chess.engine.Player.WHITE
import edu.austral.dissis.chess.engine.board.BoardBuilder
import edu.austral.dissis.chess.engine.board.PositionValidator
import edu.austral.dissis.chess.engine.board.RectangleBoardValidator
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
    val validator = RectangleBoardValidator(numberRows = 8, numberCols = 8)
    val board = getClassicInitialBoard(validator)

    val pieceAdapter = UiPieceAdapter(getPieceIdMap())

    val gameRules = getCheckersGameRules()

    val turnManager = getCheckersTurnManager()
    val game = Game(gameRules, board, turnManager)

    return StandardGameEngine(game, validator, pieceAdapter)
}

fun getCheckersTurnManager() = MultiTurnManager(WHITE)

fun getCheckersGameRules() = StandardGameRules(
    CompoundPrePlayValidator(
        StaysStill(),
        NoPieceOfPlayer(),
        ViolatesPendingJumps(),
        ViolatesCompulsoryJumps()
    ),
    NoPostPlayValidator(),
    ExtinctionWinCondition()
)

// TODO: may create a parser to avoid this long function,
//  and to make all board creations much more clear
fun getClassicInitialBoard(validator: PositionValidator) =
    BoardBuilder(validator)
        .fillRow(
            1,
            listOf(
                getMan(WHITE),
                null,
                getMan(WHITE),
                null,
                getMan(WHITE),
                null,
                getMan(WHITE),
                null,
            ),
        )
        .fillRow(
            2,
            listOf(
                null,
                getMan(WHITE),
                null,
                getMan(WHITE),
                null,
                getMan(WHITE),
                null,
                getMan(WHITE),
            ),
        )
        .fillRow(
            row = 3,
            listOf(
                getMan(WHITE),
                null,
                getMan(WHITE),
                null,
                getMan(WHITE),
                null,
                getMan(WHITE),
                null,
            ),
        )
        .fillRow(
            row = 6,
            listOf(
                null,
                getMan(BLACK),
                null,
                getMan(BLACK),
                null,
                getMan(BLACK),
                null,
                getMan(BLACK),
            ),
        )
        .fillRow(
            row = 7,
            listOf(
                getMan(BLACK),
                null,
                getMan(BLACK),
                null,
                getMan(BLACK),
                null,
                getMan(BLACK),
                null,
            ),
        )
        .fillRow(
            row = 8,
            listOf(
                null,
                getMan(BLACK),
                null,
                getMan(BLACK),
                null,
                getMan(BLACK),
                null,
                getMan(BLACK),
            ),
        )
        .build()

private fun getPieceIdMap(): Map<PieceType, String> {
    return listOf(
        MAN to "pawn",
        KING to "king",
    ).toMap()
}
