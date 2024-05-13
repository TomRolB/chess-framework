package edu.austral.dissis.chess.checkers.rules

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.includesTakeAction
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.rules.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult

class JumpsWhenCompulsory(val subRule: PieceRule) : PieceRule {
    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        val validPlays = subRule.getValidPlays(board, position)

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
        val result = subRule.getPlayResult(board, from, to)

        return when {
            result.play == null -> {
                result
            }
            isIgnoringCompulsoryJump(result.play, pieceBeforePlay, board, from) -> {
                PlayResult(null, "This piece has a compulsory jump")
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
