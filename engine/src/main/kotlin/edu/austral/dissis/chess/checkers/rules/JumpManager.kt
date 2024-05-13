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
    private val takeActions: List<Take>
    private val tilesLeft: Int
    private val jumpsNeeded: Int
    private val jumpsLeft: Int

    constructor(pathLimit: Int, minJumps: Int, maxJumps: Int) {
        this.isBlocked = false
        this.tilesLeft = pathLimit - 1
        this.jumpsNeeded = minJumps
        this.jumpsLeft = maxJumps
        this.takeActions = emptyList()
    }

    private constructor(
        tileLimit: Int,
        minJumps: Int,
        maxJumps: Int,
        takeActions: List<Take>,
        isBlocked: Boolean,
    ) {
        this.isBlocked = isBlocked
        this.tilesLeft = tileLimit - 1
        this.jumpsNeeded = minJumps
        this.jumpsLeft = maxJumps
        this.takeActions = takeActions
    }

    override fun processPosition(
        board: GameBoard,
        from: Position,
        to: Position,
        player: Player,
    ): Pair<PathManager, Play?> {
        val hasEnemyPiece = board.containsPieceOfPlayer(to, !player)

        val play = getPlayIfValid(board, to, from)

        val manager = getUpdatedManager(hasEnemyPiece, to, board, player)

        return manager to play
    }

    private fun getUpdatedManager(
        hasEnemyPiece: Boolean,
        to: Position,
        board: GameBoard,
        player: Player,
    ) = JumpManager(
        tileLimit = tilesLeft - 1,
        minJumps = if (hasEnemyPiece) jumpsNeeded - 1 else jumpsNeeded,
        maxJumps = if (hasEnemyPiece) jumpsLeft - 1 else jumpsLeft,
        takeActions = if (hasEnemyPiece) takeActions.plus(Take(to, board)) else takeActions,
        isBlocked =
            !board.positionExists(to) ||
                board.containsPieceOfPlayer(to, player) ||
                tilesLeft <= 0 ||
                (jumpsLeft <= 0 && hasEnemyPiece),
    )

    private fun getPlayIfValid(
        board: GameBoard,
        to: Position,
        from: Position,
    ): Play? {
        return if (cannotMoveThere(board, to)) {
            null
        } else {
            Play(takeActions + Move(from, to, board))
        }
    }

    private fun cannotMoveThere(
        board: GameBoard,
        to: Position,
    ) = !board.positionExists(to) || board.isOccupied(to) || jumpsNeeded > 0
}
