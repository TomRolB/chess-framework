package edu.austral.dissis.chess.engine.rules.pieces.path

import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.Player.BLACK
import edu.austral.dissis.chess.engine.areVectorsParallel
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.doVectorsShareOrientation
import edu.austral.dissis.chess.engine.pieces.InvalidPlay
import edu.austral.dissis.chess.engine.pieces.PlayResult
import edu.austral.dissis.chess.engine.pieces.ValidPlay
import edu.austral.dissis.chess.engine.rules.pieces.PieceRule

class PathMovementRules : PieceRule {
    private val rowIncrement: Int
    private val colIncrement: Int
    private val pathManager: PathManager

    constructor(increments: Pair<Int, Int>, manager: PathManager) {
        this.rowIncrement = increments.first
        this.colIncrement = increments.second
        this.pathManager = manager
    }

    constructor(increments: Pair<Int, Int>, asPlayer: Player, manager: PathManager) {
        this.rowIncrement = if (asPlayer == BLACK) -increments.first else increments.first
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

        return when {
            invalidDirection(moveData) -> {
                InvalidPlay("Moving in invalid direction")
            }
            else -> {
                val play = getPlayIfValid(moveData, board, player)
                if (play == null) {
                    InvalidPlay("Cannot move there: the path is blocked")
                } else {
                    ValidPlay(play)
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
