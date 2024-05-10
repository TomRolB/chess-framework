package edu.austral.dissis.chess.checkers.rules

import edu.austral.dissis.chess.checkers.rules.CheckersPieceState.CAN_TAKE_ENEMY
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.PostPlayValidator
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.pieces.PlayResult

//TODO: Modify play to update all pieces' state
class JumpChainValidator: PostPlayValidator {
    override fun getPostPlayResult(play: Play, board: GameBoard, player: Player): PlayResult {
        return if (piecesHavePendingMoves(board, player)) {
            PlayResult(null, "One of your pieces has a pending move")
        }
        else PlayResult(play, "Valid move")
    }

    private fun piecesHavePendingMoves(
        board: GameBoard,
        player: Player,
    ) = board
        .getAllPositionsOfPlayer(player)
        .any {
            board.getPieceAt(it)!!.hasState(CAN_TAKE_ENEMY)
        }
}