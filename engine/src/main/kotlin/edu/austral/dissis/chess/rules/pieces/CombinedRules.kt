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

    override fun getPlayIfValid(
        board: ChessBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        for (rule in rules) {
            val playResult = rule.getPlayIfValid(board, from, to)
            if (playResult.play != null) return playResult
        }

        // TODO: consider returning more descriptive messages
        return PlayResult(null, "Piece cannot move this way")

        //        val firstResult = first.getPlayIfValid(board, from, to)
//
//        return if (firstResult.play != null) firstResult
//        // TODO: consider actually returning a combination of failure messages
//        else second.getPlayIfValid(board, from, to)
    }
}
