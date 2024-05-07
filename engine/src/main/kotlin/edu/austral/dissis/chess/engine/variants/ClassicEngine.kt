package edu.austral.dissis.chess.engine.variants

import edu.austral.dissis.chess.engine.Game
import edu.austral.dissis.chess.engine.Player.BLACK
import edu.austral.dissis.chess.engine.Player.WHITE
import edu.austral.dissis.chess.engine.board.BoardBuilder
import edu.austral.dissis.chess.engine.board.PositionValidator
import edu.austral.dissis.chess.engine.board.RectangleBoardValidator
import edu.austral.dissis.chess.engine.pieces.ClassicPieceType.BISHOP
import edu.austral.dissis.chess.engine.pieces.ClassicPieceType.KING
import edu.austral.dissis.chess.engine.pieces.ClassicPieceType.KNIGHT
import edu.austral.dissis.chess.engine.pieces.ClassicPieceType.PAWN
import edu.austral.dissis.chess.engine.pieces.ClassicPieceType.QUEEN
import edu.austral.dissis.chess.engine.pieces.ClassicPieceType.ROOK
import edu.austral.dissis.chess.engine.pieces.PieceType
import edu.austral.dissis.chess.engine.pieces.getBishop
import edu.austral.dissis.chess.engine.pieces.getKing
import edu.austral.dissis.chess.engine.pieces.getKnight
import edu.austral.dissis.chess.engine.pieces.getPawn
import edu.austral.dissis.chess.engine.pieces.getQueen
import edu.austral.dissis.chess.engine.pieces.getRook
import edu.austral.dissis.chess.engine.turns.OneToOneTurnManager
import edu.austral.dissis.chess.rules.standard.gamerules.ClassicPostPlayValidator
import edu.austral.dissis.chess.rules.standard.gamerules.ClassicPrePlayValidator
import edu.austral.dissis.chess.rules.standard.gamerules.ClassicWinCondition
import edu.austral.dissis.chess.rules.standard.gamerules.StandardGameRules
import edu.austral.dissis.chess.ui.StandardGameEngine
import edu.austral.dissis.chess.ui.UiPieceAdapter

fun getClassicEngine(): StandardGameEngine {
    val validator = RectangleBoardValidator(8, 8)
    val board = getClassicInitialBoard(validator)

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

fun getClassicInitialBoard(validator: PositionValidator) =
    BoardBuilder(validator)
        .fillRow(
            1,
            listOf(
                getRook(WHITE),
                getKnight(WHITE),
                getBishop(WHITE),
                getQueen(WHITE),
                getKing(WHITE),
                getBishop(WHITE),
                getKnight(WHITE),
                getRook(WHITE),
            ),
        )
        .fillRow(2, List(8) { getPawn(WHITE) })
        .fillRow(7, List(8) { getPawn(BLACK) })
        .fillRow(
            8,
            listOf(
                getRook(BLACK),
                getKnight(BLACK),
                getBishop(BLACK),
                getQueen(BLACK),
                getKing(BLACK),
                getBishop(BLACK),
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
    ).toMap()
}
