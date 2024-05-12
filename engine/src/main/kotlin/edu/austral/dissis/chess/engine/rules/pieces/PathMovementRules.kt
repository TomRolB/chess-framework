package edu.austral.dissis.chess.engine.rules.pieces

import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.Player.WHITE
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult
import kotlin.math.sign

// TODO: class too long?
// TODO: modularize
// TODO: fix messages
class PathMovementRules : PieceRule {
    private val increments: Pair<Int, Int>
    private val mirroredRowIncrement: Boolean
    private val manager: PathManager

    constructor(increments: Pair<Int, Int>, manager: PathManager) {
        this.increments = increments
        this.mirroredRowIncrement = false
        this.manager = manager
    }

    constructor(increments: Pair<Int, Int>, mirroredRowIncrement: Boolean, manager: PathManager) {
        this.increments = increments
        this.mirroredRowIncrement = mirroredRowIncrement
        this.manager = manager
    }

    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        var (row, col) = position
        val player = board.getPieceAt(position)!!.player

        val rowIncrement = getMirrored(increments.first, player)
        val colIncrement = increments.second

        row += rowIncrement
        col += colIncrement

        val result: MutableList<Play> = mutableListOf()

        var currentManager = manager

        while (true) {
            val possibleTo = Position(row, col)

            val (newManager, play) = currentManager.processPosition(board, position, possibleTo, player)

            currentManager = newManager

            if (currentManager.isBlocked) break
            if (play != null) result.add(play)

            row += rowIncrement
            col += colIncrement
        }

        return result
    }

    private fun getMirrored(rowIncrement: Int, player: Player) =
        if (!mirroredRowIncrement || player == WHITE) rowIncrement else -rowIncrement

    override fun getPlayResult(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val player = board.getPieceAt(from)!!.player
        val moveData =
            if (mirroredRowIncrement) MovementData(from, to, board, player)
            else MovementData(from, to)

        //TODO: improve
        return if (isViolated(moveData)) {
            PlayResult(null, "Piece cannot move this way")
        } else {
            val play = getPlayIfValid(moveData, board, player)
            if (play == null) {
                PlayResult(null, "Cannot move there: the path is blocked")
            } else {
                PlayResult(play, "Valid move")
            }
        }
    }

    private fun isViolated(moveData: MovementData): Boolean {
        return !(
            areVectorsParallel(moveData, increments) &&
                doVectorsShareOrientation(moveData, increments)
        )
    }

    private fun areVectorsParallel(
        moveData: MovementData,
        increments: Pair<Int, Int>,
    ) = moveData.colDelta * increments.first == moveData.rowDelta * increments.second

    private fun doVectorsShareOrientation(
        moveData: MovementData,
        increment: Pair<Int, Int>,
    ): Boolean {
        return moveData.rowDelta.sign == increment.first.sign
    }

    private fun getPlayIfValid(
        moveData: MovementData,
        board: GameBoard,
        player: Player,
    ): Play? {
        val rowIncrement = getMirrored(increments.first, player)
        val colIncrement = increments.second

        var row = moveData.fromRow + rowIncrement
        var col = moveData.fromCol + colIncrement

        var currentManager = manager
        var play: Play? = null

        while (!currentManager.isBlocked) {
            val to = Position(row, col)

            val (newManager, newPlay) = currentManager.processPosition(board, moveData.from, to, player)
            currentManager = newManager

            if (!reachedFinalPosition(row, moveData, col)) {
                play = newPlay
                break
            }

            row += rowIncrement
            col += colIncrement
        }

        return play
    }

    private fun reachedFinalPosition(
        row: Int,
        moveData: MovementData,
        col: Int,
    ) = !(row == moveData.toRow && col == moveData.toCol)
}
