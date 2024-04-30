package edu.austral.dissis.chess.engine.board

import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.pieces.King
import edu.austral.dissis.chess.engine.pieces.Piece

class HashChessBoard private constructor(
    private val validator: PositionValidator,
    private val boardMap: Map<Position, Piece>,
    private val whiteKingPosition: Position,
    private val blackKingPosition: Position,
) : ChessBoard {
    companion object {
        fun build(
            validator: PositionValidator,
            pieces: List<Pair<Position, Piece>>,
            whiteKingPosition: Position,
            blackKingPosition: Position,
        ): HashChessBoard {
            return HashChessBoard(validator, pieces.toMap(), whiteKingPosition, blackKingPosition)
        }
    }

    override fun isOccupied(position: Position): Boolean {
        return boardMap[position] != null
    }

    override fun getPieceAt(position: Position): Piece? {
        return boardMap[position]
    }

    override fun setPieceAt(
        position: Position,
        piece: Piece,
    ): HashChessBoard {
        val newMap = boardMap + (position to piece)

        var newWhiteKingPosition = whiteKingPosition
        var newBlackKingPosition = blackKingPosition

        if (piece.type is King) {
            when (piece.player) {
                Player.WHITE -> newWhiteKingPosition = position
                Player.BLACK -> newBlackKingPosition = position
            }
        }

        return HashChessBoard(validator, newMap, newWhiteKingPosition, newBlackKingPosition)
    }

    override fun delPieceAt(position: Position): HashChessBoard {
//        require(position != whiteKingPosition && position != blackKingPosition) {
//            "A king cannot be deleted without being set in other position first"
//        }

        val newMap: HashMap<Position, Piece> = HashMap(boardMap)
        newMap.remove(position)

        return HashChessBoard(validator, newMap, whiteKingPosition, blackKingPosition)
    }

    override fun positionExists(position: Position): Boolean {
        return validator.positionExists(position)
    }

    override fun isPositionOnUpperLimit(position: Position): Boolean {
        return validator.isPositionOnLastRow(position)
    }

    override fun containsPieceOfPlayer(
        position: Position,
        player: Player,
    ): Boolean {
        val piece = getPieceAt(position) ?: return false
        return piece.player == player
    }

    override fun getAllPositions(): Iterable<Position> {
        return boardMap.keys
    }

    override fun getAllPositionsOfPlayer(
        player: Player,
        includeKing: Boolean,
    ): Iterable<Position> {
        return boardMap.keys.filter {
            val position: Position = it
            val piece = boardMap[position]!!

            if (includeKing) {
                piece.player == player
            } else {
                piece.player == player && (piece.type !is King)
            }
        }
    }

    override fun getRowAsWhite(
        position: Position,
        player: Player,
    ): Int {
        return validator.getRowAsWhite(position, player)
    }

    override fun getKingPosition(player: Player): Position {
        return when (player) {
            Player.WHITE -> whiteKingPosition
            Player.BLACK -> blackKingPosition
        }
    }
}
