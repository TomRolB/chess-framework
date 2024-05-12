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
    val tilesLeft: Int
    val jumpsNeeded: Int
    val jumpsLeft: Int

    constructor(pathLimit: Int, minJumps: Int, maxJumps: Int) {
        this.isBlocked = false
        this.tilesLeft = pathLimit - 1
        this.jumpsNeeded = minJumps
        this.jumpsLeft = maxJumps
        this.takes = emptyList()
    }

    private constructor(
        pathLimit: Int,
        minJumps: Int,
        maxJumps: Int,
        takes: List<Take>,
        isBlocked: Boolean,
    ) {
        this.isBlocked = isBlocked
        this.tilesLeft = pathLimit - 1
        this.jumpsNeeded = minJumps
        this.jumpsLeft = maxJumps
        this.takes = takes
    }

    override fun processPosition(
        board: GameBoard,
        from: Position,
        to: Position,
        player: Player,
    ): Pair<PathManager, Play?> {
        val hasEnemyPiece = board.containsPieceOfPlayer(to, !player)

        val play =
            if (board.isOccupied(to) || jumpsNeeded > 0) {
                null
            } else {
                Play(takes + Move(from, to, board))
            }

        val manager =
            JumpManager(
                pathLimit = tilesLeft - 1,
                minJumps = if (hasEnemyPiece) jumpsNeeded - 1 else jumpsNeeded,
                maxJumps = if (hasEnemyPiece) jumpsLeft - 1 else jumpsLeft,
                takes = if (hasEnemyPiece) takes.plus(Take(to, board)) else takes,
                isBlocked =
                    !board.positionExists(to) ||
                        board.containsPieceOfPlayer(to, player) ||
                        tilesLeft <= 0 ||
                        (jumpsLeft <= 0 && hasEnemyPiece),
            )

        return manager to play
    }
}
