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

        return board.getAllPositionsOfPlayer(!player, true).any {
            val enemyPosition: Position = it
            val enemyPiece: Piece = board.getPieceAt(enemyPosition)!!
            val kingCapture: Play? = enemyPiece.getPlayResult(board, enemyPosition, kingPosition).play

            kingCapture != null
        }
    }
}
