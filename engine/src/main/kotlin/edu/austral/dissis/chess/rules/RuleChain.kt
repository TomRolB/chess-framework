package edu.austral.dissis.chess.rules

import edu.austral.dissis.chess.engine.*

//class RuleChain<Any>(val inn: Any, val condition: Boolean, val out: Any, val next: RuleChain<Any>?) : Rule {
//    fun verify(): Any {
//        return if (condition) next?.verify()?: out else out
//    }
//}

interface RuleChain<T, R> {
    fun verify(arg: T): R
}


//class PieceOfPlayerHypothesis1 {
//    val next: PieceOfType = //Instead of PieceOfType, we should have a type that specifies
//                            //the input verify() will receive (see conclusion below)
//        PieceOfType()
//    fun verify(board: GameBoard, pos: Position, player: Player): Any? {
////        board
////            .getPieceAt(pos).takeIf { board.containsPieceOfPlayer(pos, player) }
//
//        return if (board.containsPieceOfPlayer(pos, player)) next.verify(board.getPieceAt(pos))
//               else null
//        // Conclusion: the next Rule must have as the type of verify()'s parameter the
//        // same type that we here pass at next.verify
//    }
//}

class PieceHasNeverMoved<T: MoveDependant>(
    val pieceType: Class<T>,
    val next: RuleChain<PieceRules, Boolean>) : RuleChain<Piece, Boolean> {

    override fun verify(piece: Piece): Boolean {
        return if (
            pieceType.isInstance(piece.rules)
            && !(piece.rules as MoveDependant).hasEverMoved
        ) next.verify(piece.rules)
        else false


//        return piece.rules
//            .takeIf {
//                pieceType.isInstance(piece.rules)
//                && !(piece.rules as MoveDependant).hasEverMoved
//            }
//            ?.let { next.verify(piece.rules) }
    }
}

class PieceOfPlayer(
    val board: GameBoard,
    val pos: Position,
    val player: Player,
    val next: RuleChain<Piece, Boolean>) : RuleChain<Any?, Boolean>
{
    override fun verify(arg: Any?): Boolean {
        return board
            .getPieceAt(pos).takeIf { board.containsPieceOfPlayer(pos, player) }
            ?.let {next.verify(it)}
            ?: false
    }
}


// Sometimes an element of the chain is the last one, but
// we need to pass the next RuleChain object either way.
// Succeed serves this purpose: it will simply return true.
class Succeed<T> : RuleChain<T, Boolean> {
    override fun verify(arg: T): Boolean {
        return true
    }
}
