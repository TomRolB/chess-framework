package edu.austral.dissis.chess.checkers.rules

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.includesTakeAction
import edu.austral.dissis.chess.engine.pieces.InvalidPlay
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.pieces.PlayResult
import edu.austral.dissis.chess.engine.pieces.ValidPlay
import edu.austral.dissis.chess.engine.rules.pieces.PieceRule

class JumpsWhenCompulsory(val previousRule: PieceRule) : PieceRule {
    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        val validPlays = previousRule.getValidPlays(board, position)

        val hasAvailableJumps = validPlays.any { it.includesTakeAction() }

        return validPlays
            .filter {
                !hasAvailableJumps || pieceActuallyTookAnEnemy(it)
            }
    }

    override fun getPlayResult(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val pieceBeforePlay = board.getPieceAt(from)!!
        val result = previousRule.getPlayResult(board, from, to)

        return when {
            result !is ValidPlay -> {
                result
            }
            isIgnoringCompulsoryJump(result.play, pieceBeforePlay, board, from) -> {
                InvalidPlay("This piece has a compulsory jump")
            }
            else -> {
                result
            }
        }
    }

    private fun isIgnoringCompulsoryJump(
        play: Play,
        pieceBeforePlay: Piece,
        board: GameBoard,
        from: Position,
    ) = HasAvailableJumps(pieceBeforePlay, board, from).verify() &&
        !pieceActuallyTookAnEnemy(play)

    private fun pieceActuallyTookAnEnemy(play: Play) = play.includesTakeAction()
}
