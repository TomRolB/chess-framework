package edu.austral.dissis.chess.rules.pieces

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult
import kotlin.math.absoluteValue

// TODO: code is similar to UpdateMoveState.
//  See if there's some way of dealing with redundancy,
//  while also taking into consideration that it may not be
//  necessary to do it, since we only have two similar classes.

class UpdateTwoPlacesState(val subRule: PieceRule): PieceRule {
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
                    removeOrAddState(it, board)
                } else it
            }
            .let {
                Play(it)
            }
    }

    private fun removeOrAddState(
        move: Move,
        board: ChessBoard,
    ): Move {
        return if (movedTwoPlaces(move)) {
            val pieceNextTurn = board.getPieceAt(move.from)!!.withState("moved two places")
            move.withPiece(pieceNextTurn)
        } else {
            val pieceNextTurn = board.getPieceAt(move.from)!!.withoutState("moved two places")
            move.withPiece(pieceNextTurn)
        }
    }

    private fun movedTwoPlaces(move: Move): Boolean {
        return (move.to.row - move.from.row).absoluteValue == 2
    }
}