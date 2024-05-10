package edu.austral.dissis.chess.checkers

import edu.austral.dissis.chess.checkers.CheckersPieceType.KING
import edu.austral.dissis.chess.checkers.CheckersPieceType.MAN
import edu.austral.dissis.chess.checkers.rules.CompulsoryJumpsValidator
import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.Game
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.Player.BLACK
import edu.austral.dissis.chess.engine.Player.WHITE
import edu.austral.dissis.chess.engine.PostPlayValidator
import edu.austral.dissis.chess.engine.WinCondition
import edu.austral.dissis.chess.engine.board.BoardBuilder
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.PositionValidator
import edu.austral.dissis.chess.engine.board.RectangleBoardValidator
import edu.austral.dissis.chess.engine.pieces.PieceType
import edu.austral.dissis.chess.engine.pieces.PlayResult
import edu.austral.dissis.chess.engine.rules.standard.gamerules.StandardGameRules
import edu.austral.dissis.chess.engine.turns.OneToOneTurnManager
import edu.austral.dissis.chess.ui.StandardGameEngine
import edu.austral.dissis.chess.ui.UiPieceAdapter

// TODO: should see which code is shared across engines and create a basic
//  getEngine() with all specific arguments
fun getCheckersEngine(): StandardGameEngine {
    val validator = RectangleBoardValidator(8, 8)
    val board = getClassicInitialBoard(validator)

    val pieceAdapter = UiPieceAdapter(getPieceIdMap())

    val gameRules =
        StandardGameRules(
            CompulsoryJumpsValidator(),
            object : PostPlayValidator {
                override fun getPostPlayResult(play: Play, board: GameBoard, player: Player): PlayResult {
                    return PlayResult(play, "Valid move")
                }
            },
            object : WinCondition {
                override fun getGameResult(
                    board: GameBoard,
                    play: Play,
                    player: Player,
                ): RuleResult {
                    return RuleResult(board, play, EngineResult.VALID_MOVE, "Valid move")
                }
            },
        )

    val game = Game(gameRules, board, OneToOneTurnManager())

    return StandardGameEngine(game, validator, pieceAdapter)
}

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
            3,
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
            6,
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
            7,
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
            8,
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
