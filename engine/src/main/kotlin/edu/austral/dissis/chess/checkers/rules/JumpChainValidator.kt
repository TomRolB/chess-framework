package edu.austral.dissis.chess.checkers.rules

import edu.austral.dissis.chess.checkers.rules.CheckersPieceState.HAS_PENDING_MOVE
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.PostPlayValidator
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.pieces.PlayResult

class JumpChainValidator: PostPlayValidator {
    override fun getPostPlayResult(play: Play, board: GameBoard, player: Player): PlayResult {
        return if (board
            .getAllPositionsOfPlayer(player)
            .any {
                board.getPieceAt(it)!!.hasState(HAS_PENDING_MOVE)
            }) {
            PlayResult(null, "One of your pieces has a pending move")
        }
        else PlayResult(play, "Valid move")
    }
}