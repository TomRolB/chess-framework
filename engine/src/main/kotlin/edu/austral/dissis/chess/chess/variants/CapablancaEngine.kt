package edu.austral.dissis.chess.chess.variants

import edu.austral.dissis.chess.engine.Game
import edu.austral.dissis.chess.engine.Player.BLACK
import edu.austral.dissis.chess.engine.Player.WHITE
import edu.austral.dissis.chess.engine.board.BoardBuilder
import edu.austral.dissis.chess.engine.board.PositionValidator
import edu.austral.dissis.chess.engine.board.RectangleBoardValidator
import edu.austral.dissis.chess.chess.pieces.CapablancaPieceTypes.ARCHBISHOP
import edu.austral.dissis.chess.chess.pieces.CapablancaPieceTypes.CHANCELLOR
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.PAWN
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.ROOK
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.BISHOP
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.QUEEN
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.KNIGHT
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.KING
import edu.austral.dissis.chess.engine.pieces.PieceType
import edu.austral.dissis.chess.chess.pieces.getArchbishop
import edu.austral.dissis.chess.chess.pieces.getBishop
import edu.austral.dissis.chess.chess.pieces.getChancellor
import edu.austral.dissis.chess.chess.pieces.getKing
import edu.austral.dissis.chess.chess.pieces.getKnight
import edu.austral.dissis.chess.chess.pieces.getPawn
import edu.austral.dissis.chess.chess.pieces.getQueen
import edu.austral.dissis.chess.chess.pieces.getRook
import edu.austral.dissis.chess.engine.turns.OneToOneTurnManager
import edu.austral.dissis.chess.chess.rules.gamerules.ClassicPostPlayValidator
import edu.austral.dissis.chess.chess.rules.gamerules.ClassicPrePlayValidator
import edu.austral.dissis.chess.chess.rules.gamerules.ClassicWinCondition
import edu.austral.dissis.chess.rules.standard.gamerules.StandardGameRules
import edu.austral.dissis.chess.ui.StandardGameEngine
import edu.austral.dissis.chess.ui.UiPieceAdapter

fun getCapablancaEngine(): StandardGameEngine {
    val validator = RectangleBoardValidator(10, 10)
    val board = getInitialBoard(validator)

    val pieceAdapter = UiPieceAdapter(getPieceIdMap())

    val gameRules =
        StandardGameRules(
            ClassicPrePlayValidator(),
            ClassicPostPlayValidator(),
            ClassicWinCondition(),
        )

    val game = Game(gameRules, board, OneToOneTurnManager())

    return StandardGameEngine(game, validator, pieceAdapter)
}

private fun getInitialBoard(validator: PositionValidator) =
    BoardBuilder(validator)
        .fillRow(
            1,
            listOf(
                getRook(WHITE),
                getKnight(WHITE),
                getArchbishop(WHITE),
                getBishop(WHITE),
                getQueen(WHITE),
                getKing(WHITE),
                getBishop(WHITE),
                getChancellor(WHITE),
                getKnight(WHITE),
                getRook(WHITE),
            ),
        )
        .fillRow(2, List(10) { getPawn(WHITE) })
        .fillRow(9, List(10) { getPawn(BLACK) })
        .fillRow(
            10,
            listOf(
                getRook(BLACK),
                getKnight(BLACK),
                getArchbishop(BLACK),
                getBishop(BLACK),
                getQueen(BLACK),
                getKing(BLACK),
                getBishop(BLACK),
                getChancellor(BLACK),
                getKnight(BLACK),
                getRook(BLACK),
            ),
        )
        .build()

private fun getPieceIdMap(): Map<PieceType, String> {
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
