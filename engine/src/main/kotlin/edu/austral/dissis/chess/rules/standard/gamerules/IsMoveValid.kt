package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.GameBoard
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.Position
import edu.austral.dissis.chess.rules.All
import edu.austral.dissis.chess.rules.Rule
import edu.austral.dissis.chess.rules.SimpleRule

class IsMoveValid(
    val board: GameBoard,
    val from: Position,
    val to: Position,
    val player: Player
) : Rule<Boolean> {
    override fun verify(): Boolean {
        //TODO: Check whether to implement a way to have messages,
        // as before (although they were being printed before,
        // not returned. Should actually consider the messaging policy).

        return All(
            from != to,
            board.positionExists(from),
            board.positionExists(to),
            board.containsPieceOfPlayer(from, player),
            !board.containsPieceOfPlayer(to, player)
        ).verify()

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