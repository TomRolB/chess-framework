package edu.austral.dissis.chess.rules.pieces

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult

class CombinedRules(vararg rules: PieceRule) : PieceRule {
    val rules: Iterable<PieceRule> = rules.toList()

    override fun getValidPlays(
        board: ChessBoard,
        position: Position,
    ): Iterable<Play> {
        return rules.flatMap { it.getValidPlays(board, position) }

        // TODO: clean
        // return first.getValidPlays(board, position) + second.getValidPlays(board, position)
    }

    override fun getPlayResult(
        board: ChessBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        var playResult = PlayResult(null, "Piece cannot move this way")

        for (rule in rules) {
            playResult = rule.getPlayResult(board, from, to)
            if (playResult.play != null) return playResult
        }

        // TODO: consider actually returning a combination of failure messages

        // TODO: Cannot select last message

        return playResult
    }
}
