package edu.austral.dissis.chess.engine.pieces

import edu.austral.dissis.chess.chess.pieces.PieceState
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position

// TODO: maybe it's possible to completely decouple rules from Piece.
//  It would be done by getting the piece type on IsPlayValid and
//  mapping it to the corresponding rule.
//  The first question to be asked, anyway, is whether this is strictly
//  necessary, since we are good with polymorphism.

class Piece {
    val type: PieceType
    val player: Player
    val rules: PieceRule
    val states: Set<PieceState>

    constructor(type: PieceType, player: Player, rules: PieceRule) {
        this.type = type
        this.player = player
        this.rules = rules
        this.states = emptySet()
    }

    private constructor(type: PieceType, player: Player, rules: PieceRule, states: Set<PieceState>) {
        this.type = type
        this.player = player
        this.rules = rules
        this.states = states
    }

    override fun toString(): String {
        return "$player $type"
    }

    fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        return rules.getValidPlays(board, position)
    }

    fun getPlayResult(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        return rules.getPlayResult(board, from, to)
    }

    override fun hashCode(): Int {
        return (player to type).hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is Piece &&
            this.hashCode() == other.hashCode()
    }

    fun withState(state: PieceState): Piece {
        return Piece(type, player, rules, states.plus(state))
    }

    fun withoutState(state: PieceState): Piece {
        return Piece(type, player, rules, states.minus(state))
    }

    fun hasState(state: PieceState): Boolean {
        return state in states
    }

    fun withRules(rules: PieceRule): Piece {
        return Piece(type, player, rules, states)
    }

    fun withPlayer(player: Player): Piece {
        return Piece(type, player, rules, states)
    }
}

// TODO: Consider converting this to the kind of result we saw in class,
// since sometimes we have null plays or unnecessary messages.
data class PlayResult(val play: Play?, val message: String)

interface PieceRule {
    fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play>

    fun getPlayResult(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult
}
