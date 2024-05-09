package edu.austral.dissis.chess.checkers.rules

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.Take
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.not
import edu.austral.dissis.chess.engine.rules.pieces.PathManager

// TODO: make more readable
class JumpManager : PathManager {
    override val isBlocked: Boolean
    val takes: List<Take>
    val pathLimit: Int
    val minTakes: Int
    val maxTakes: Int

    constructor(pathLimit: Int, minTakes: Int, maxTakes: Int) {
        this.isBlocked = false
        this.pathLimit = pathLimit
        this.minTakes = minTakes
        this.maxTakes = maxTakes
        this.takes = emptyList()
    }

    private constructor(
        pathLimit: Int,
        minTakes: Int,
        maxTakes: Int,
        takes: List<Take>,
        isBlocked: Boolean
    ) {
        this.isBlocked = isBlocked
        this.pathLimit = pathLimit
        this.minTakes = minTakes
        this.maxTakes = maxTakes
        this.takes = takes
    }

    override fun processPosition(
        board: GameBoard,
        from: Position,
        to: Position,
        player: Player,
    ): Pair<PathManager, Play?> {
        val hasEnemyPiece = board.containsPieceOfPlayer(to, !player)

        val manager = JumpManager(
            pathLimit = pathLimit - 1,
            minTakes = if (hasEnemyPiece) minTakes - 1 else minTakes,
            maxTakes = if (hasEnemyPiece) maxTakes - 1 else maxTakes,
            takes = if (hasEnemyPiece) takes.plus(Take(to, board)) else takes,
            isBlocked =
            !board.positionExists(to)
                    || board.containsPieceOfPlayer(to, player)
                    || pathLimit == 0
                    || maxTakes == 0
        )

        val play =
            if (board.isOccupied(to) || minTakes != 0) null
            else Play(takes + Move(from, to, board))


        return manager to play
    }
}