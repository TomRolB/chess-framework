package edu.austral.dissis.chess.chess.variants

import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getBishop
import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getKing
import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getKnight
import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getPawn
import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getQueen
import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getRook
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.BISHOP
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.KING
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.KNIGHT
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.PAWN
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.QUEEN
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.ROOK
import edu.austral.dissis.chess.chess.rules.gamerules.ClassicPostPlayValidator
import edu.austral.dissis.chess.chess.rules.gamerules.ClassicWinCondition
import edu.austral.dissis.chess.engine.Game
import edu.austral.dissis.chess.engine.Player.BLACK
import edu.austral.dissis.chess.engine.Player.WHITE
import edu.austral.dissis.chess.engine.board.BoardParser
import edu.austral.dissis.chess.engine.board.RectangularBoardValidator
import edu.austral.dissis.chess.engine.pieces.PieceType
import edu.austral.dissis.chess.engine.rules.gameflow.StandardGameRules
import edu.austral.dissis.chess.engine.rules.gameflow.preplay.CompoundPrePlayValidator
import edu.austral.dissis.chess.engine.rules.gameflow.preplay.PieceOfPlayer
import edu.austral.dissis.chess.engine.rules.gameflow.preplay.CannotStayStill
import edu.austral.dissis.chess.engine.turns.OneToOneTurnManager
import edu.austral.dissis.chess.ui.StandardGameEngine
import edu.austral.dissis.chess.ui.UiPieceAdapter

fun getChessEngine(): StandardGameEngine {
    val validator = RectangularBoardValidator(numberRows = 8, numberCols = 8)
    val board = getClassicChessBoard()

    val pieceAdapter = UiPieceAdapter(getPieceIdMap())

    val gameRules = getChessGameRules()

    val game = Game(gameRules, board, OneToOneTurnManager(WHITE))

    return StandardGameEngine(game, validator, pieceAdapter)
}

fun getChessGameRules() =
    StandardGameRules(
        CompoundPrePlayValidator(
            CannotStayStill(),
            PieceOfPlayer(),
        ),
        ClassicPostPlayValidator(),
        ClassicWinCondition(),
    )

fun getClassicChessBoard() =
    BoardParser
        .withPieces(mapOf(
            "WP" to getPawn(WHITE),
            "WR" to getRook(WHITE),
            "WN" to getKnight(WHITE),
            "WB" to getBishop(WHITE),
            "WQ" to getQueen(WHITE),
            "WK" to getKing(WHITE),
            "BP" to getPawn(BLACK),
            "BR" to getRook(BLACK),
            "BN" to getKnight(BLACK),
            "BB" to getBishop(BLACK),
            "BQ" to getQueen(BLACK),
            "BK" to getKing(BLACK),
        ))
        .parse(
            """
                |WR|WN|WB|WQ|WK|WB|WN|WR|
                |WP|WP|WP|WP|WP|WP|WP|WP|
                |  |  |  |  |  |  |  |  |
                |  |  |  |  |  |  |  |  |
                |  |  |  |  |  |  |  |  |
                |  |  |  |  |  |  |  |  |
                |BP|BP|BP|BP|BP|BP|BP|BP|
                |BR|BN|BB|BQ|BK|BB|BN|BR|
            """.trimIndent()
        )

fun getPieceIdMap(): Map<PieceType, String> {
    return listOf(
        PAWN to "pawn",
        ROOK to "rook",
        BISHOP to "bishop",
        KNIGHT to "knight",
        QUEEN to "queen",
        KING to "king",
    ).toMap()
}
