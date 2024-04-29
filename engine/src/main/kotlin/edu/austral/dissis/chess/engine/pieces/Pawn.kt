package edu.austral.dissis.chess.engine.pieces

import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.rules.pieces.king.IsKingChecked
import edu.austral.dissis.chess.rules.pieces.pawn.PawnValidMove

class Pawn : MoveDependantPieceType {
    private val player: Player
    private val increments = listOf(1 to 1, 0 to 1, -1 to 1, 0 to 2)
    val hasJustMovedTwoPlaces: Boolean
    override val hasEverMoved: Boolean

    // TODO: consider modifying this
    enum class State {
        MOVED,
        MOVED_TWO_PLACES,
    }

    constructor(player: Player) {
        this.player = player
        this.hasEverMoved = false
        this.hasJustMovedTwoPlaces = false
    }

    constructor(player: Player, state: State) {
        this.player = player
        when (state) {
            State.MOVED -> {
                this.hasEverMoved = true
                this.hasJustMovedTwoPlaces = false
            }
            State.MOVED_TWO_PLACES -> {
                this.hasEverMoved = true
                this.hasJustMovedTwoPlaces = true
            }
        }
    }

    override fun getValidPlays(
        board: ChessBoard,
        position: Position,
    ): Iterable<Play> {
        val (row, col) = position

        return increments
            .map { Position(row + it.first, col + it.second) }
            .mapNotNull { getPlayIfValid(board, position, it).play }
            .filter {
                val futureBoard = it.execute()
                !IsKingChecked(futureBoard, player).verify()
            }
    }

    override fun getPlayIfValid(
        board: ChessBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        return PawnValidMove(board, from, to, player, hasEverMoved)
            .verify()
            ?.let { PlayResult(it, "Valid play") }
            ?: PlayResult(null, "Pawn cannot move this way")
    }
}