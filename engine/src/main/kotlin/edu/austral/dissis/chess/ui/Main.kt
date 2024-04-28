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

    companion object {
        const val GAME_TITLE = "Chess"
    }

    override fun start(primaryStage: Stage) {
        primaryStage.title = GAME_TITLE

        val root = GameView(gameEngine, imageResolver)
        primaryStage.scene = Scene(root)

        primaryStage.show()
    }

    private fun getEngine(): StandardGameEngine {
        val validator = RectangleBoardValidator(8, 8)
        val board = HashGameBoard.build(
            validator = validator,
            pieces = getInitialPieces(),
            whiteKingPosition = Position(1, 5),
            blackKingPosition = Position(8, 5)
        )
        val pieceAdapter = UiPieceAdapter(getPieceIdMap())
        val postPlayProcedures = getPostPlayProcedures(validator)

        val game = Game(StandardGameRules(), board, OneToOneTurnManager())

        return StandardGameEngine(game, validator, pieceAdapter, postPlayProcedures)
    }

    private fun getPostPlayProcedures(validator: RectangleBoardValidator): (UiBoard) -> Map<edu.austral.dissis.chess.gui.Position, ChessPiece> {
        return {
                uiBoard: UiBoard ->

                uiBoard.map {
                    val (pos, piece) = it
                    if (
                        pos.row == validator.numberRows &&
                        piece.color == PlayerColor.WHITE &&
                        piece.pieceId == "pawn"
                    ) {
                        pos to ChessPiece(piece.id, piece.color, piece.position, "queen")
                    } else if (
                        pos.row == 0 &&
                        piece.color == PlayerColor.BLACK &&
                        piece.pieceId == "pawn"
                    ) {
                        pos to ChessPiece(piece.id, piece.color, piece.position, "queen")
                    } else {
                        pos to piece
                    }
                }.toMap()
        }
    }

    private fun getPieceIdMap(): Map<KClass<out PieceRules>, String> {
        return listOf(
            PawnPieceRules::class to "pawn",
            RookPieceRules::class to "rook",
            BishopPieceRules::class to "bishop",
            KnightPieceRules::class to "knight",
            QueenPieceRules::class to "queen",
            KingPieceRules::class to "king"
        ).toMap()
    }

    private fun getInitialPieces(): List<Pair<Position, Piece>> {
        return getFromPlayer(1, 2, Player.WHITE) + getFromPlayer(8, 7, Player.BLACK)
    }

    private fun getFromPlayer(borderRow: Int, pawnsRow: Int, player: Player): List<Pair<Position, Piece>> {
        return listOf(
            Position(borderRow, 1) to Piece(player, RookPieceRules(player)),
            Position(borderRow, 2) to Piece(player, KnightPieceRules(player)),
            Position(borderRow, 3) to Piece(player, BishopPieceRules(player)),
            Position(borderRow, 4) to Piece(player, QueenPieceRules(player)),
            Position(borderRow, 5) to Piece(player, KingPieceRules(player)),
            Position(borderRow, 6) to Piece(player, BishopPieceRules(player)),
            Position(borderRow, 7) to Piece(player, KnightPieceRules(player)),
            Position(borderRow, 8) to Piece(player, RookPieceRules(player)),
        ) + (1..8).map {
            Position(pawnsRow, it) to Piece(player, PawnPieceRules(player))
        }
    }
}
