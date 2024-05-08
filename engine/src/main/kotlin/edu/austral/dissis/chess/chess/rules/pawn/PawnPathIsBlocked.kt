package edu.austral.dissis.chess.chess.rules.pawn

import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.rules.Rule

class PawnPathIsBlocked(
    private val board: GameBoard,
    private val moveData: MovementData,
) : Rule<Boolean> {
    override fun verify(): Boolean {
        val frontPos = Position((moveData.fromRow + moveData.toRow) / 2, moveData.fromCol)
        return board.isOccupied(moveData.to) || board.isOccupied(frontPos)
    }
}
