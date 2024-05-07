package edu.austral.dissis.chess.ui

import edu.austral.dissis.chess.engine.Game
import edu.austral.dissis.chess.engine.OneToOneTurnManager
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.BoardBuilder
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.board.PositionValidator
import edu.austral.dissis.chess.engine.board.RectangleBoardValidator
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.pieces.getBishop
import edu.austral.dissis.chess.engine.pieces.getKing
import edu.austral.dissis.chess.engine.pieces.getKnight
import edu.austral.dissis.chess.engine.pieces.getPawn
import edu.austral.dissis.chess.engine.pieces.getQueen
import edu.austral.dissis.chess.engine.pieces.getRook
import edu.austral.dissis.chess.gui.CachedImageResolver
import edu.austral.dissis.chess.gui.ChessPiece
import edu.austral.dissis.chess.gui.DefaultImageResolver
import edu.austral.dissis.chess.gui.GameView
import edu.austral.dissis.chess.gui.PlayerColor
import edu.austral.dissis.chess.rules.standard.gamerules.StandardGameRules
import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Scene
import javafx.stage.Stage

fun main() {
    launch(ChessGameApplication::class.java)
}

class ChessGameApplication : Application() {
    private val gameEngine = getEngine()
    private val imageResolver = CachedImageResolver(DefaultImageResolver())

    override fun start(primaryStage: Stage) {
        primaryStage.title = GAME_TITLE

        val root = GameView(gameEngine, imageResolver)
        primaryStage.scene = Scene(root)

        primaryStage.show()
    }

    companion object {
        // TODO: All below is weird. Should define a better way of creating boards with pieces
        const val GAME_TITLE = "Chess"
        private val ONE_TO_EIGHT = 1..8
        private val WHITE_PIECES = getFromPlayer(1, 2, Player.WHITE)
        private val BLACK_PIECES = getFromPlayer(8, 7, Player.BLACK)
        private val WK_POSITION = Position(1, 5)
        private val BK_POSITION = Position(8, 5)

        private fun getFromPlayer(
            borderRow: Int,
            pawnsRow: Int,
            player: Player,
        ): List<Pair<Position, Piece>> {
            val firstRow =
                listOf(
                    getRook(player),
                    getKnight(player),
                    getBishop(player),
                    getQueen(player),
                    getKing(player),
                    getBishop(player),
                    getKnight(player),
                    getRook(player),
                )
                    .zip(ONE_TO_EIGHT)
                    .map { (piece, col) -> Position(borderRow, col) to piece }
            val secondRow =
                ONE_TO_EIGHT
                    .map { Position(pawnsRow, it) to getPawn(player) }
            return firstRow + secondRow
        }

        private fun getInitialPieces(): List<Pair<Position, Piece>> {
            return WHITE_PIECES + BLACK_PIECES
        }

        private fun getPieceIdMap(): Map<String, String> {
            return listOf(
                "pawn" to "pawn",
                "rook" to "rook",
                "bishop" to "bishop",
                "knight" to "knight",
                "queen" to "queen",
                "king" to "king",
            ).toMap()
        }

        private fun getPostPlayProcedures(
            validator: RectangleBoardValidator,
        ): (UiBoard) -> Map<edu.austral.dissis.chess.gui.Position, ChessPiece> {
            return {
                    uiBoard: UiBoard ->

                uiBoard.map {
                    val (pos, piece) = it
                    if (
                        pos.row == validator.numberRows &&
                        piece.color == PlayerColor.WHITE &&
                        piece.pieceId == "pawn"
                    ) {
                        pos to piece.copy(pieceId = "queen")
                    } else if (
                        pos.row == 1 &&
                        piece.color == PlayerColor.BLACK &&
                        piece.pieceId == "pawn"
                    ) {
                        pos to piece.copy(pieceId = "queen")
                    } else {
                        pos to piece
                    }
                }.toMap()
            }
        }

        private fun getEngine(): StandardGameEngine {
            val validator = RectangleBoardValidator(8, 8)
            val board =
//                HashChessBoard.build(
//                    validator = VALIDATOR,
//                    pieces = getInitialPieces(),
//                    whiteKingPosition = WK_POSITION,
//                    blackKingPosition = BK_POSITION,
//                )
                getInitialBoard(validator)

            val pieceAdapter = UiPieceAdapter(getPieceIdMap())
            val postPlayProcedures = getPostPlayProcedures(validator)

            val game = Game(StandardGameRules(), board, OneToOneTurnManager())

            return StandardGameEngine(game, validator, pieceAdapter, postPlayProcedures)
        }

        private fun getInitialBoard(validator: PositionValidator) =
            BoardBuilder(validator)
            .fillRow(
                1, listOf(
                    getRook(Player.WHITE),
                    getKnight(Player.WHITE),
                    getBishop(Player.WHITE),
                    getQueen(Player.WHITE),
                    getKing(Player.WHITE),
                    getBishop(Player.WHITE),
                    getKnight(Player.WHITE),
                    getRook(Player.WHITE),
                )
            )
            .fillRow(2, List(8) { getPawn(Player.WHITE) })
            .fillRow(7, List(8) { getPawn(Player.BLACK) })
            .fillRow(
                8, listOf(
                    getRook(Player.BLACK),
                    getKnight(Player.BLACK),
                    getBishop(Player.BLACK),
                    getQueen(Player.BLACK),
                    getKing(Player.BLACK),
                    getBishop(Player.BLACK),
                    getKnight(Player.BLACK),
                    getRook(Player.BLACK),
                )
            )
            .build()
    }
}
