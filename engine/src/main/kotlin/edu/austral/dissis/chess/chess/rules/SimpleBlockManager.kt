package edu.austral.dissis.chess.chess.rules

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.rules.pieces.path.PathManager

class SimpleBlockManager : PathManager {
    override val isBlocked: Boolean
    val limit: Int

    constructor(limit: Int) {
        this.isBlocked = false
        this.limit = limit
    }
    private constructor(limit: Int, isBlocked: Boolean) {
        this.isBlocked = isBlocked
        this.limit = limit
    }

    override fun processPosition(
        board: GameBoard,
        from: Position,
        to: Position,
        player: Player,
    ): Pair<PathManager, Play?> {
        val manager =
            SimpleBlockManager(
                limit = limit - 1,
                isBlocked = !board.positionExists(to) || board.isOccupied(to) || limit == 0,
            )

        val play =
            Move(from, to, board)
                .asPlay()
                .takeIf { !board.containsPieceOfPlayer(to, player) }

        return manager to play
    }
}
