package edu.austral.dissis.chess.chess.variants

import edu.austral.dissis.chess.chess.pieces.CapablancaPieceTypes.ARCHBISHOP
import edu.austral.dissis.chess.chess.pieces.CapablancaPieceTypes.CHANCELLOR
import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getArchbishop
import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getBishop
import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getChancellor
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
import edu.austral.dissis.chess.engine.rules.gameflow.preplay.NoPieceOfPlayer
import edu.austral.dissis.chess.engine.rules.gameflow.preplay.StaysStill
import edu.austral.dissis.chess.engine.turns.OneToOneTurnManager
import edu.austral.dissis.chess.ui.StandardGameEngine
import edu.austral.dissis.chess.ui.UiPieceAdapter

fun getCapablancaEngine(): StandardGameEngine {
    val validator = RectangularBoardValidator(numberRows = 10, numberCols = 10)
    val board = getInitialBoard()

    val pieceAdapter = UiPieceAdapter(getCapablancaPieceIdMap())

    val gameRules =
        StandardGameRules(
            CompoundPrePlayValidator(
                StaysStill(),
                NoPieceOfPlayer(),
            ),
            ClassicPostPlayValidator(),
            ClassicWinCondition(),
        )

    val game = Game(gameRules, board, OneToOneTurnManager(WHITE))

    return StandardGameEngine(game, validator, pieceAdapter)
}

private fun getInitialBoard() =
    BoardParser
        .withPieces(mapOf(
            "WP" to getPawn(WHITE),
            "WR" to getRook(WHITE),
            "WN" to getKnight(WHITE),
            "WA" to getArchbishop(WHITE),
            "WB" to getBishop(WHITE),
            "WQ" to getQueen(WHITE),
            "WK" to getKing(WHITE),
            "WC" to getChancellor(WHITE),
            "BP" to getPawn(BLACK),
            "BR" to getRook(BLACK),
            "BN" to getKnight(BLACK),
            "BA" to getArchbishop(BLACK),
            "BB" to getBishop(BLACK),
            "BQ" to getQueen(BLACK),
            "BK" to getKing(BLACK),
            "BC" to getChancellor(BLACK),
        ))
        .parse(
            """
                |WR|WN|WA|WB|WQ|WK|WB|WC|WN|WR|
                |WP|WP|WP|WP|WP|WP|WP|WP|WP|WP|
                |  |  |  |  |  |  |  |  |  |  |
                |  |  |  |  |  |  |  |  |  |  |
                |  |  |  |  |  |  |  |  |  |  |
                |  |  |  |  |  |  |  |  |  |  |
                |  |  |  |  |  |  |  |  |  |  |
                |  |  |  |  |  |  |  |  |  |  |
                |BP|BP|BP|BP|BP|BP|BP|BP|BP|BP|
                |BR|BN|BA|BB|BQ|BK|BB|BC|BN|BR|
            """.trimIndent()
        )

private fun getCapablancaPieceIdMap(): Map<PieceType, String> {
    return listOf(
        PAWN to "pawn",
        ROOK to "rook",
        BISHOP to "bishop",
        KNIGHT to "knight",
        QUEEN to "queen",
        KING to "king",
        CHANCELLOR to "chancellor",
        ARCHBISHOP to "archbishop",
    ).toMap()
}
