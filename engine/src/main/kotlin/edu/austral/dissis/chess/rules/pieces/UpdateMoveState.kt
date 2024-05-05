package edu.austral.dissis.chess.rules.pieces

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult

class UpdateMoveState(val subRule: PieceRule): PieceRule {
    override fun getValidPlays(board: ChessBoard, position: Position): Iterable<Play> {
        return subRule
            .getValidPlays(board, position)
            .map { updatePieceStateInMovements(it, board) }
    }

    override fun getPlayIfValid(board: ChessBoard, from: Position, to: Position): PlayResult {
        val result = subRule.getPlayIfValid(board, from, to)


        return if (result.play == null) result
        else PlayResult(
            play = updatePieceStateInMovements(result.play, board),
            message = result.message
        )
    }

    fun updatePieceStateInMovements(play: Play, board: ChessBoard): Play {
        // TODO: make clear

        return play
            .actions
            .map {
                if (it is Move) {
                    val pieceNextTurn = board.getPieceAt(it.from)!!.withState("moved")
                    it.withPiece(pieceNextTurn)
                }
                else it
            }
            .let {
                Play(it)
            }
    }
}