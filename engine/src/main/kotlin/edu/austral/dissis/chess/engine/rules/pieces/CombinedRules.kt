package edu.austral.dissis.chess.engine.rules.pieces

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.PlayResult

class CombinedRules(vararg rules: PieceRule) : PieceRule {
    val rules: Iterable<PieceRule> = rules.toList()

    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        return rules.flatMap { it.getValidPlays(board, position) }
    }

    override fun getPlayResult(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        var playResult = PlayResult(null, "Piece cannot move this way")

        for (rule in rules) {
            playResult = rule.getPlayResult(board, from, to)
            if (playResult.play != null) return playResult
        }

        return playResult
    }
}
