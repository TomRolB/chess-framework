package edu.austral.dissis.chess.engine.pieces

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.MoveType
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.rules.pieces.king.IsKingChecked

// TODO: maybe it's possible to completely decouple rules from Piece.
//  It would be done by getting the piece type on IsPlayValid and
//  mapping it to the corresponding rule.
//  The first question to be asked, anyway, is whether this is strictly
//  necessary, since we are good with polymorphism.

class Piece {
    // TODO: may have to find some way of replacing string by a class implementing an interface
    //  (will probably use an enum that implements an interface PieceType)
    val type: String
    val player: Player
    val rules: PieceRule

    // TODO: may have to find some way of replacing strings by classes implementing an interface
    val states: Set<String>

    constructor(type: String, player: Player, rules: PieceRule) {
        this.type = type
        this.player = player
        this.rules = rules
        this.states = emptySet()
    }

    private constructor(type: String, player: Player, rules: PieceRule, states: Set<String>) {
        this.type = type
        this.player = player
        this.rules = rules
        this.states = states
    }

    // TODO: may make this class a Proxy of PieceType (which may
    //  actually be renamed to PieceRules), so that we don't have
    //  to access the field 'type' each time.
    override fun toString(): String {
        return "$player $type"
    }

    // TODO: May have to change hashCode
    override fun hashCode(): Int {
        return (player to type).hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is Piece &&
            this.hashCode() == other.hashCode()
    }

    fun withState(state: String): Piece {
        return Piece(type, player, rules, states.plus(state))
    }

    fun withoutState(state: String): Piece {
        return Piece(type, player, rules, states.minus(state))
    }

    fun hasState(state: String): Boolean {
        return state in states
    }
}

// TODO: Consider converting this to the kind of result we saw in class,
// since sometimes we have null plays or unnecessary messages.
data class PlayResult(val play: Play?, val message: String)

interface PieceRule {
    fun getValidPlays(
        board: ChessBoard,
        position: Position,
    ): Iterable<Play>

    // TODO: should be renamed to getPlayResult
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
