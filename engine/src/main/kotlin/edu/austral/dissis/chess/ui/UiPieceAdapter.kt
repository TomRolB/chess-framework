package edu.austral.dissis.chess.ui

import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.pieces.PieceType
import edu.austral.dissis.chess.gui.ChessPiece
import edu.austral.dissis.chess.gui.PlayerColor
import edu.austral.dissis.chess.gui.Position

class UiPieceAdapter(private val pieceIdMap: Map<PieceType, String>) {
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
                pieceIdMap[piece.type] ?: throw IllegalArgumentException(
                    "No pieceId was specified for this piece type",
                ),
        )
    }

    fun adaptNew(
        piece: Piece,
        pos: edu.austral.dissis.chess.engine.board.Position,
    ): ChessPiece {
        return ChessPiece(
            id = pos.row.toString() + pos.col.toString(),
            color = adapt(piece.player),
            position = Position(pos.row, pos.col),
            pieceId =
                pieceIdMap[piece.type] ?: throw IllegalArgumentException(
                    "No pieceId was specified for piece type ${piece.type}",
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
