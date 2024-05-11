package edu.austral.dissis.chess.engine.rules.standard.gamerules

import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.PrePlayValidator
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.rules.Rule
import edu.austral.dissis.chess.engine.rules.RuleChain

class PrePlayRules(
    val board: GameBoard,
    val from: Position,
    val to: Position,
    val player: Player,
    val validator: PrePlayValidator,
    val next: RuleChain<Piece, RuleResult>,
) : Rule<RuleResult> {
    override fun verify(): RuleResult {
        val resultOnViolation = validator.getResultOnViolation(board, from, to, player)

        return resultOnViolation ?: let {
            val piece = board.getPieceAt(from)!!
            next.verify(piece)
        }
    }
}
