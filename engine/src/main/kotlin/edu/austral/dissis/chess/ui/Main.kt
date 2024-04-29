package edu.austral.dissis.chess.ui

import edu.austral.dissis.chess.engine.BishopPieceRules
import edu.austral.dissis.chess.engine.Game
import edu.austral.dissis.chess.engine.HashGameBoard
import edu.austral.dissis.chess.engine.KingPieceRules
import edu.austral.dissis.chess.engine.KnightPieceRules
import edu.austral.dissis.chess.engine.OneToOneTurnManager
import edu.austral.dissis.chess.engine.PawnPieceRules
import edu.austral.dissis.chess.engine.Piece
import edu.austral.dissis.chess.engine.PieceRules
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.Position
import edu.austral.dissis.chess.engine.QueenPieceRules
import edu.austral.dissis.chess.engine.RectangleBoardValidator
import edu.austral.dissis.chess.engine.RookPieceRules
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
        val ONE_TO_EIGHT = 1..8
        val WHITE_PIECES = getFromPlayer(1, 2, Player.WHITE)
        val BLACK_PIECES = getFromPlayer(8, 7, Player.BLACK)
        val VALIDATOR = RectangleBoardValidator(8, 8)
        val WK_POSITION = Position(1, 5)
        val BK_POSITION = Position(8, 5)

        private fun getFromPlayer(
            borderRow: Int,
            pawnsRow: Int,
            player: Player,
        ): List<Pair<Position, Piece>> {
            val firstRow =
                listOf(
                    RookPieceRules(player),
                    KnightPieceRules(player),
                    BishopPieceRules(player),
                    QueenPieceRules(player),
                    KingPieceRules(player),
                    BishopPieceRules(player),
                    KnightPieceRules(player),
                    RookPieceRules(player),
                )
                    .zip(ONE_TO_EIGHT)
                    .map { (pieceRules, col) -> Position(borderRow, col) to Piece(player, pieceRules) }
            val secondRow =
                ONE_TO_EIGHT
                    .map { Position(pawnsRow, it) to Piece(player, PawnPieceRules(player)) }
            return firstRow + secondRow
        }

        private fun getInitialPieces(): List<Pair<Position, Piece>> {
            return WHITE_PIECES + BLACK_PIECES
        }

        private fun getPieceIdMap(): Map<KClass<out PieceRules>, String> {
            return listOf(
                PawnPieceRules::class to "pawn",
                RookPieceRules::class to "rook",
                BishopPieceRules::class to "bishop",
                KnightPieceRules::class to "knight",
                QueenPieceRules::class to "queen",
                KingPieceRules::class to "king",
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
