package edu.austral.dissis.chess.engine.rules.pieces

import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.areVectorsParallel
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.doVectorsShareOrientation
import edu.austral.dissis.chess.engine.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult

// TODO: class too long?
// TODO: modularize
// TODO: fix messages
class PathMovementRules : PieceRule {
    private val rowIncrement: Int
    private val colIncrement: Int
    private val pathManager: PathManager

    constructor(increments: Pair<Int, Int>, manager: PathManager) {
        this.rowIncrement = increments.first
        this.colIncrement = increments.second
        this.pathManager = manager
    }

    constructor(increments: Pair<Int, Int>, mirroredRowIncrement: Boolean, manager: PathManager) {
        this.rowIncrement = if (mirroredRowIncrement) -increments.first else increments.first
        this.colIncrement = increments.second
        this.pathManager = manager
    }

    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        val player = board.getPieceAt(position)!!.player
        val result: MutableList<Play> = mutableListOf()
        var currentManager = pathManager

        for (possibleTo in PositionIterator(position, rowIncrement, colIncrement)) {
            val (newManager, play) = currentManager.processPosition(board, position, possibleTo, player)
            currentManager = newManager

            if (play != null) result.add(play)
            if (currentManager.isBlocked) break
        }

        return result
    }

    override fun getPlayResult(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val player = board.getPieceAt(from)!!.player
        val moveData = MovementData(from, to)

        // TODO: improve
        return when {
            invalidDirection(moveData) -> {
                PlayResult(null, "Moving in invalid direction")
            }
            else -> {
                val play = getPlayIfValid(moveData, board, player)
                if (play == null) {
                    PlayResult(null, "Cannot move there: the path is blocked")
                } else {
                    PlayResult(play, "Valid move")
                }
            }
        }
    }

    private fun invalidDirection(moveData: MovementData): Boolean {
        val dataVector = moveData.rowDelta to moveData.colDelta
        val incrementVector = rowIncrement to colIncrement

        return !areVectorsParallel(dataVector, incrementVector) ||
            !doVectorsShareOrientation(dataVector, incrementVector)
    }

    private fun getPlayIfValid(
        moveData: MovementData,
        board: GameBoard,
        player: Player,
    ): Play? {
        var currentManager = pathManager

        for (pos in PositionIterator(moveData.from, rowIncrement, colIncrement)) {
            val (newManager, newPlay) = currentManager.processPosition(board, moveData.from, pos, player)
            currentManager = newManager

            if (reachedFinalPosition(moveData, pos.row, pos.col)) {
                return newPlay
            }

            if (currentManager.isBlocked) break
        }

        return null
    }

    private fun reachedFinalPosition(
        moveData: MovementData,
        row: Int,
        col: Int,
    ) = row == moveData.toRow && col == moveData.toCol
}
