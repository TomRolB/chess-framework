package edu.austral.dissis.chess.rules

import edu.austral.dissis.chess.engine.GameBoard
import edu.austral.dissis.chess.engine.Piece
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.Position
import edu.austral.dissis.chess.engine.not

class IsKingChecked(val board: GameBoard, val player: Player) : Rule<Boolean> {
    override fun verify(): Boolean {
        val kingPosition = board.getKingPosition(player)

        // Return true if any enemy piece "can capture" the King
        // We don't include the enemy king, since the kings cannot
        // check each other
        return board.getAllPositionsOfPlayer(!player, false).any {
            val enemyPosition: Position = it
            val enemyPiece: Piece = board.getPieceAt(enemyPosition)!!
            val kingCapture: Play? = enemyPiece.rules.getPlayIfValid(board, enemyPosition, kingPosition).play

            kingCapture != null
        }
    }
}
