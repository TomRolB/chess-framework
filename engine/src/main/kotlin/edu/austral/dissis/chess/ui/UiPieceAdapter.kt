package edu.austral.dissis.chess.ui

import edu.austral.dissis.chess.engine.Piece
import edu.austral.dissis.chess.engine.PieceRules
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.gui.ChessPiece
import edu.austral.dissis.chess.gui.PlayerColor
import edu.austral.dissis.chess.gui.Position
import kotlin.reflect.KClass

class UiPieceAdapter(private val pieceIdMap: Map<KClass<out PieceRules>, String>) {
    fun adapt(
        piece: Piece,
        id: String,
        to: Position,
    ): ChessPiece {
        return ChessPiece(
            id = id,
            color = adapt(piece.player),
            position = to,
            pieceId =
                pieceIdMap[piece.rules::class] ?: throw IllegalArgumentException(
                    "No pieceId was specified for this piece type",
                ),
        )
    }

    fun adaptNew(
        piece: Piece,
        pos: edu.austral.dissis.chess.engine.Position,
    ): ChessPiece {
        return ChessPiece(
            id = pos.toString(),
            color = adapt(piece.player),
            position = Position(pos.row, pos.col),
            pieceId =
                pieceIdMap[piece.rules::class] ?: throw IllegalArgumentException(
                    "No pieceId was specified for this piece type",
                ),
        )
    }

    companion object {
        fun adapt(player: Player): PlayerColor {
            return when (player) {
                Player.WHITE -> PlayerColor.WHITE
                Player.BLACK -> PlayerColor.BLACK
            }
        }
    }
}
