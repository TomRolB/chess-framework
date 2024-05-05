package edu.austral.dissis.chess.engine.pieces

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.MoveType
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.rules.pieces.king.IsKingChecked

class Piece {
    val player: Player
    val rules: PieceRule
    val states: Set<String>

    constructor(player: Player, type: PieceRule) {
        this.player = player
        this.rules = type
        this.states = emptySet()
    }

    private constructor(player: Player, type: PieceRule, states: Set<String>) {
        this.player = player
        this.rules = type
        this.states = states
    }

    // TODO: may make this class a Proxy of PieceType (which may
    //  actually be renamed to PieceRules), so that we don't have
    //  to access the field 'type' each time.
    override fun toString(): String {
        return "$player, $rules"
    }

    // TODO: May have to change hashCode
    override fun hashCode(): Int {
        return (player to rules::class).hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is Piece &&
            this.hashCode() == other.hashCode()
    }

    fun withState(state: String): Piece {
        return Piece(player, rules, states.plus(state))
    }

    fun withoutState(state: String): Piece {
        return Piece(player, rules, states.minus(state))
    }

    fun hasState(state: String): Boolean {
        return state in states
    }
}

data class PlayResult(val play: Play?, val message: String)

interface PieceRule {
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

interface MoveDependantPieceRule : PieceRule {
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
