package edu.austral.dissis.chess.engine.rules.pieces

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.extractMoveAction
import edu.austral.dissis.chess.engine.pieces.InvalidPlay
import edu.austral.dissis.chess.engine.pieces.PlayResult

class FinalPosShouldContainPieceOfPlayer(
    val player: Player,
    val shouldContain: Boolean,
    val onFailMessage: String,
    val previousRule: PieceRule,
) : PieceRule {
    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        return previousRule
            .getValidPlays(board, position)
            .filter {
                val finalPosition = it.extractMoveAction().to
                board.containsPieceOfPlayer(finalPosition, player) == shouldContain
            }
    }

    override fun getPlayResult(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val playResult = previousRule.getPlayResult(board, from, to)

        return if (
            playResult is InvalidPlay ||
            board.containsPieceOfPlayer(to, player) == shouldContain
        ) {
            playResult
        } else {
            InvalidPlay(onFailMessage)
        }
    }
}
