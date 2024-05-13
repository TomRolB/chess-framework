package edu.austral.dissis.chess.engine.rules.pieces.updaters

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.rules.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult

class Update(val updater: PlayUpdater, val subRule: PieceRule) : PieceRule {
    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        return subRule
            .getValidPlays(board, position)
            .map { updater.update(it, board) }
    }

    override fun getPlayResult(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val result = subRule.getPlayResult(board, from, to)

        return if (result.play == null) {
            result
        } else {
            PlayResult(
                play = updater.update(result.play, board),
                message = result.message,
            )
        }
    }
}
