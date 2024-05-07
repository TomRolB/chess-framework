package edu.austral.dissis.chess.rules.pieces

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.extractMove
import edu.austral.dissis.chess.engine.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult

class FinalPositionContainsPieceOfPlayer(
    val player: Player,
    val shouldContain: Boolean,
    val onFailMessage: String,
    val subRule: PieceRule,
) : PieceRule {
    override fun getValidPlays(
        board: ChessBoard,
        position: Position,
    ): Iterable<Play> {
        return subRule
            .getValidPlays(board, position)
            .filter {
                val finalPosition = it.extractMove().to
                board.containsPieceOfPlayer(finalPosition, player) == shouldContain
            }
    }

    override fun getPlayResult(
        board: ChessBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val playResult = subRule.getPlayResult(board, from, to)

        // TODO: Could this be more readable?
        return if (
            playResult.play != null &&
            board.containsPieceOfPlayer(to, player) == shouldContain
        ) {
            playResult
        } else {
            PlayResult(null, onFailMessage)
        }
    }
}
