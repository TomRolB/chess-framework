package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.GameBoard
import edu.austral.dissis.chess.engine.Piece
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.Position
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.rules.IndependentRuleChain
import edu.austral.dissis.chess.rules.RuleChain

class PrePlayRules(
    val board: GameBoard,
    val from: Position,
    val to: Position,
    val player: Player,
    val next: RuleChain<Piece, RuleResult>,
) : RuleChain<Any?, RuleResult> {
    override fun verify(arg: Any?): RuleResult {
        // TODO: Check whether to implement a way to have messages,
        // as before (although they were being printed before,
        // not returned. Should actually consider the messaging policy).

        return IndependentRuleChain(
            board.positionExists(from) to "Starting position does not exist",
            board.positionExists(to) to "Final position does not exist",
            board.containsPieceOfPlayer(from, player) to "This tile does not contain a piece of yours",
            (from != to) to "Cannot stay in the same place",
            !board.containsPieceOfPlayer(to, player) to "Cannot move over piece of yours",
        )
            .verify()
            ?.let { RuleResult(board, null, EngineResult.GENERAL_MOVE_VIOLATION, it) }
            ?: let {
                val piece = board.getPieceAt(from)!!
                next.verify(piece)
            }
    }
}
