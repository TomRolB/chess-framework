package edu.austral.dissis.chess.chess.board

import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.board.PositionValidator
import edu.austral.dissis.chess.engine.pieces.Piece

class HashGameBoard private constructor(
    private val validator: PositionValidator,
    private val boardMap: Map<Position, Piece>,
) : GameBoard {
    companion object {
        fun build(
            validator: PositionValidator,
            pieces: List<Pair<Position, Piece>>,
        ): HashGameBoard {
            return HashGameBoard(validator, pieces.toMap())
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
    ): HashGameBoard {
        val newMap = boardMap + (position to piece)

        return HashGameBoard(validator, newMap)
    }

    override fun delPieceAt(position: Position): HashGameBoard {
        val newMap: HashMap<Position, Piece> = HashMap(boardMap)
        newMap.remove(position)

        return HashGameBoard(validator, newMap)
    }

    override fun positionExists(position: Position): Boolean {
        return validator.positionExists(position)
    }

    override fun isPositionOnUpperLimit(
        position: Position,
        player: Player,
    ): Boolean {
        return validator.isPositionOnLastRow(position, player)
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

    override fun getAllPositionsOfPlayer(player: Player): Iterable<Position> {
        return boardMap.keys.filter {
            val position: Position = it
            val piece = boardMap[position]!!

            piece.player == player
        }
    }

    override fun getRowAsWhite(
        position: Position,
        player: Player,
    ): Int {
        return validator.getRowAsWhite(position, player)
    }
}
