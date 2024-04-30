package edu.austral.dissis.chess.engine.pieces

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.MoveType
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.rules.pieces.king.IsKingChecked

// TODO: put pieces in different file?
// TODO: we are supposed to make rules easy to change. Should see if
//  this applies to each implementation of PieceRules.

data class Piece(val player: Player, val type: PieceType) {
    override fun toString(): String {
        return "$player, $type"
    }

    override fun hashCode(): Int {
        return (player to type::class).hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is Piece &&
            this.hashCode() == other.hashCode()
    }
}

data class PlayResult(val play: Play?, val message: String)

interface PieceType {
    fun getValidPlays(
        board: ChessBoard,
        position: Position,
    ): Iterable<Play>

    fun getPlayIfValid(
        board: ChessBoard,
        from: Position,
        to: Position,
    ): PlayResult
}

interface MoveDependantPieceType : PieceType {
    val hasEverMoved: Boolean
}

fun getValidPlaysFromMoveType(
    moveType: MoveType,
    board: ChessBoard,
    position: Position,
    player: Player,
) = moveType
    .getPossiblePositions(board, position)
    .map {
        Play(listOf(Move(position, it, board)))
    }
    .filter {
        val futureBoard = it.execute()
        !IsKingChecked(futureBoard, player).verify()
    }
