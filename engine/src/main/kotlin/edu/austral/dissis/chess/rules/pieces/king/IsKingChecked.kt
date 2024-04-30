package edu.austral.dissis.chess.rules.pieces.king

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.not
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.rules.Rule

class IsKingChecked(val board: ChessBoard, val player: Player) : Rule<Boolean> {
    override fun verify(): Boolean {
        val kingPosition = board.getKingPosition(player)

        // Return true if any enemy piece "can capture" the King
        // We don't include the enemy king, since the kings cannot
        // check each other
        return board.getAllPositionsOfPlayer(!player, true).any {
            val enemyPosition: Position = it
            val enemyPiece: Piece = board.getPieceAt(enemyPosition)!!
            val kingCapture: Play? = enemyPiece.type.getPlayIfValid(board, enemyPosition, kingPosition).play

            kingCapture != null
        }
    }
}
