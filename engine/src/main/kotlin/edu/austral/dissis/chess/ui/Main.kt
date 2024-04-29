package edu.austral.dissis.chess.ui

import edu.austral.dissis.chess.engine.pieces.Bishop
import edu.austral.dissis.chess.engine.Game
import edu.austral.dissis.chess.engine.HashGameBoard
import edu.austral.dissis.chess.engine.pieces.King
import edu.austral.dissis.chess.engine.pieces.Knight
import edu.austral.dissis.chess.engine.OneToOneTurnManager
import edu.austral.dissis.chess.engine.pieces.Pawn
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.pieces.PieceType
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.Queen
import edu.austral.dissis.chess.engine.board.RectangleBoardValidator
import edu.austral.dissis.chess.engine.pieces.Rook
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
import kotlin.reflect.KClass

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
        const val GAME_TITLE = "Chess"
        private val ONE_TO_EIGHT = 1..8
        private val WHITE_PIECES = getFromPlayer(1, 2, Player.WHITE)
        private val BLACK_PIECES = getFromPlayer(8, 7, Player.BLACK)
        private val VALIDATOR = RectangleBoardValidator(8, 8)
        private val WK_POSITION = Position(1, 5)
        private val BK_POSITION = Position(8, 5)

        private fun getFromPlayer(
            borderRow: Int,
            pawnsRow: Int,
            player: Player,
        ): List<Pair<Position, Piece>> {
            val firstRow =
                listOf(
                    Rook(player),
                    Knight(player),
                    Bishop(player),
                    Queen(player),
                    King(player),
                    Bishop(player),
                    Knight(player),
                    Rook(player),
                )
                    .zip(ONE_TO_EIGHT)
                    .map { (pieceRules, col) -> Position(borderRow, col) to Piece(player, pieceRules) }
            val secondRow =
                ONE_TO_EIGHT
                    .map { Position(pawnsRow, it) to Piece(player, Pawn(player)) }
            return firstRow + secondRow
        }

        private fun getInitialPieces(): List<Pair<Position, Piece>> {
            return WHITE_PIECES + BLACK_PIECES
        }

        private fun getPieceIdMap(): Map<KClass<out PieceType>, String> {
            return listOf(
                Pawn::class to "pawn",
                Rook::class to "rook",
                Bishop::class to "bishop",
                Knight::class to "knight",
                Queen::class to "queen",
                King::class to "king",
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
            val board =
                HashGameBoard.build(
                    validator = VALIDATOR,
                    pieces = getInitialPieces(),
                    whiteKingPosition = WK_POSITION,
                    blackKingPosition = BK_POSITION,
                )
            val pieceAdapter = UiPieceAdapter(getPieceIdMap())
            val postPlayProcedures = getPostPlayProcedures(VALIDATOR)

            val game = Game(StandardGameRules(), board, OneToOneTurnManager())

            return StandardGameEngine(game, VALIDATOR, pieceAdapter, postPlayProcedures)
        }
    }
}
