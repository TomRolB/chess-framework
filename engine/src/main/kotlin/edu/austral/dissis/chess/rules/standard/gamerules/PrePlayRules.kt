package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.*
import edu.austral.dissis.chess.rules.All
import edu.austral.dissis.chess.rules.RuleChain

class PrePlayRules(
    val board: GameBoard,
    val from: Position,
    val to: Position,
    val player: Player,
    val next: RuleChain<Piece, RuleResult>
) : RuleChain<Any?, RuleResult> {
    override fun verify(arg: Any?): RuleResult {
        //TODO: Check whether to implement a way to have messages,
        // as before (although they were being printed before,
        // not returned. Should actually consider the messaging policy).

        val subRules =  All(
            from != to,
            board.positionExists(from),
            board.positionExists(to),
            board.containsPieceOfPlayer(from, player),
            !board.containsPieceOfPlayer(to, player)
        ).verify()

        return if (!subRules) RuleResult(board, null, EngineResult.GENERAL_MOVE_VIOLATION)
        else {
            val piece = board.getPieceAt(from)!!
            next.verify(piece)
        }

//        if (from == to) {
//            println("'from' and 'to' must be different")
//            return false
//        }
//        if (!board.positionExists(from)) {
//            println("Invalid board position: '${from}'")
//            return false
//        }
//        if (!board.positionExists(to)) {
//            println("Invalid board position: '${to}'")
//            return false
//        }
//
//        if (!board.containsPieceOfPlayer(from, player)) {
//            println("This position does not hold any piece of yours (player: $player)")
//            return false
//        }
//
//        val piece = board.getPieceAt(from)!!
//
//        if (board.containsPieceOfPlayer(to, piece.player)) {
//            println("Cannot move to square containing ally piece")
//            return false
//        }
//
//        return true
    }
}