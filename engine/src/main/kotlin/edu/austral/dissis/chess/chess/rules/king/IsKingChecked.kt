package edu.austral.dissis.chess.chess.rules.king

import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.KING
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.not
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.pieces.PlayResult
import edu.austral.dissis.chess.engine.pieces.ValidPlay
import edu.austral.dissis.chess.engine.rules.Rule

class IsKingChecked(val board: GameBoard, val player: Player) : Rule<Boolean> {
    override fun verify(): Boolean {
        val kingPosition = getKingPosition(board, player)

        return board.getAllPositionsOfPlayer(!player).any {
            val enemyPosition: Position = it
            val enemyPiece: Piece = board.getPieceAt(enemyPosition)!!
            val kingCapture: PlayResult = enemyPiece.getPlayResult(board, enemyPosition, kingPosition)

            kingCapture is ValidPlay
        }
    }

    private fun getKingPosition(
        board: GameBoard,
        player: Player,
    ): Position {
        return board
            .getAllPositions()
            .first {
                val piece = board.getPieceAt(it)!!
                piece.type == KING && piece.player == player
            }
    }
}
