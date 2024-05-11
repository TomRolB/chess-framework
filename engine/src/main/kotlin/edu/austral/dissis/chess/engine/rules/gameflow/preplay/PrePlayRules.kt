package edu.austral.dissis.chess.engine.rules.gameflow.preplay

import edu.austral.dissis.chess.engine.EngineResult.VALID_MOVE
import edu.austral.dissis.chess.engine.Player
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
        val result = validator.getResult(board, from, to, player)

        return if (result.engineResult != VALID_MOVE) {
            result
        } else {
            val piece = board.getPieceAt(from)!!
            next.verify(piece)
        }
    }
}
