package edu.austral.dissis.chess.engine.rules.pieces.updaters

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.PlayResult
import edu.austral.dissis.chess.engine.pieces.ValidPlay
import edu.austral.dissis.chess.engine.rules.pieces.PieceRule

class MapPlay(val mapper: PlayMapper, val previousRule: PieceRule) : PieceRule {
    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        return previousRule
            .getValidPlays(board, position)
            .map { mapper.update(it, board) }
    }

    override fun getPlayResult(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val result = previousRule.getPlayResult(board, from, to)

        return if (result !is ValidPlay) {
            result
        } else {
            ValidPlay(
                play = mapper.update(result.play, board),
            )
        }
    }
}
