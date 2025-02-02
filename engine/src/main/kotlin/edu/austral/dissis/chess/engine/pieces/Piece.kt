package edu.austral.dissis.chess.engine.pieces

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.rules.pieces.PieceRule

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

    fun clone(): Piece {
        return Piece(type, player, rules, HashSet(states))
    }
}
