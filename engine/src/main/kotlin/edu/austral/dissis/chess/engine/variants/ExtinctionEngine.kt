package edu.austral.dissis.chess.engine.variants

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.Game
import edu.austral.dissis.chess.engine.GameResult
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.Player.BLACK
import edu.austral.dissis.chess.engine.Player.WHITE
import edu.austral.dissis.chess.engine.PostPlayValidator
import edu.austral.dissis.chess.engine.WinCondition
import edu.austral.dissis.chess.engine.board.BoardBuilder
import edu.austral.dissis.chess.engine.board.ChessBoard
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

fun getExtinctionEngine(): StandardGameEngine {
    val validator = RectangleBoardValidator(8, 8)
    val board = getInitialBoard(validator)

    val pieceAdapter = UiPieceAdapter(getPieceIdMap())

    val gameRules = StandardGameRules(
        ClassicPrePlayValidator(),
        NoPostPlayValidator(),
        ExtinctionWinCondition()
    )

    val game = Game(gameRules, board, OneToOneTurnManager())

    return StandardGameEngine(game, validator, pieceAdapter)
}



private fun getInitialBoard(validator: PositionValidator) =
    BoardBuilder(validator)
        .fillRow(
            1, listOf(
                getRook(WHITE),
                getKnight(WHITE),
                getBishop(WHITE),
                getQueen(WHITE),
                getKing(WHITE),
                getBishop(WHITE),
                getKnight(WHITE),
                getRook(WHITE),
            )
        )
        .fillRow(2, List(8) { getPawn(WHITE) })
        .fillRow(7, List(8) { getPawn(BLACK) })
        .fillRow(
            8, listOf(
                getRook(BLACK),
                getKnight(BLACK),
                getBishop(BLACK),
                getQueen(BLACK),
                getKing(BLACK),
                getBishop(BLACK),
                getKnight(BLACK),
                getRook(BLACK),
            )
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

class NoPostPlayValidator : PostPlayValidator {
    override fun isStateInvalid(board: ChessBoard, player: Player): Boolean {
        return false
    }
}

class ExtinctionWinCondition : WinCondition {
    override fun getGameResult(board: ChessBoard, play: Play, player: Player): GameResult {
        return when {
            playerWentExtinct(board, WHITE) ->
                GameResult(board, null, EngineResult.BLACK_WINS, "Black wins!")
            playerWentExtinct(board, BLACK) ->
                GameResult(board, null, EngineResult.WHITE_WINS, "White wins!")
            else ->
                GameResult(board, play, EngineResult.VALID_MOVE, "Valid move")
        }
    }

    private fun playerWentExtinct(board: ChessBoard, player: Player) =
        board.getAllPositionsOfPlayer(player, true).none()
}