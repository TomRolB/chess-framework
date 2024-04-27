package edu.austral.dissis.chess.rules

import edu.austral.dissis.chess.engine.*

//class RuleChain<Any>(val inn: Any, val condition: Boolean, val out: Any, val next: RuleChain<Any>?) : Rule {
//    fun verify(): Any {
//        return if (condition) next?.verify()?: out else out
//    }
//}

interface RuleChain<T> {
    fun verify(arg: T): Any?
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

class PieceOfType<T: PieceRules>(val pieceType: Class<T>) : RuleChain<Piece> {

    override fun verify(piece: Piece): Any? {
        return if (pieceType.isInstance(piece.rules)) piece.rules else null
    }
}

class PieceDidNotMove<T: MoveDependant>(
    val pieceType: Class<T>,
    val next: RuleChain<PieceRules>) : RuleChain<Piece> {

    override fun verify(piece: Piece): Any? {
        // TODO: Problem now is we have to pass RookPieceRules dynamically, but
        //  2. How to indicate it has the attribute hasEverMoved? -> Solution?: define interface with getter
        return piece.rules
            .takeIf {
                pieceType.isInstance(piece.rules)
                && !(piece.rules as MoveDependant).hasEverMoved
            }
            ?.let { next.verify(piece.rules) }
    }
}

class PieceOfPlayerHypothesis2(
    val board: GameBoard,
    val pos: Position,
    val player: Player,
    val next: RuleChain<Piece>) : RuleChain<Any?>
{
    override fun verify(arg: Any?): Any? {
        return board
            .getPieceAt(pos).takeIf { board.containsPieceOfPlayer(pos, player) }
            ?.let {next.verify(it)}
    }
}

class RuleChainIsNotNull(val next: RuleChain<Any?>) : RuleChain<Any?> {
    override fun verify(arg: Any?): Boolean {
        return next.verify(arg) != null
    }
}

class EndChain<T>() : RuleChain<T> {
    override fun verify(arg: T): Any? {
        return arg
    }
}