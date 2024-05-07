package edu.austral.dissis.chess.ui

import edu.austral.dissis.chess.engine.Game
import edu.austral.dissis.chess.engine.turns.OneToOneTurnManager
import edu.austral.dissis.chess.engine.Player
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
import edu.austral.dissis.chess.engine.turns.IncrementalTurnManager
import edu.austral.dissis.chess.gui.CachedImageResolver
import edu.austral.dissis.chess.gui.DefaultImageResolver
import edu.austral.dissis.chess.gui.GameView
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

        private fun getEngine(): StandardGameEngine {
            val validator = RectangleBoardValidator(8, 8)
            val board = getInitialBoard(validator)

            val pieceAdapter = UiPieceAdapter(getPieceIdMap())

            val game = Game(StandardGameRules(), board, OneToOneTurnManager())

            return StandardGameEngine(game, validator, pieceAdapter)
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
