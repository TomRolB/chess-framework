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

class PieceOfPlayerHypothesis1 {
    val next: PieceOfType = //Instead of PieceOfType, we should have a type that specifies
                            //the input verify() will receive (see conclusion below)
        PieceOfType()
    fun verify(board: GameBoard, pos: Position, player: Player): Any? {
//        board
//            .getPieceAt(pos).takeIf { board.containsPieceOfPlayer(pos, player) }
        
        return if (board.containsPieceOfPlayer(pos, player)) next.verify(board.getPieceAt(pos))
               else null
        // Conclusion: the next Rule must have as the type of verify()'s parameter the
        // same type that we here pass at next.verify
    }
}

class PieceOfType: RuleChain<Piece> {
    override fun verify(piece: Piece): Any? {
        return if (piece.rules is RookPieceRules) piece.rules else null
    }
}

class PieceOfPlayerHypothesis2(
    val board: GameBoard,
    val pos: Position,
    val player: Player,
    val next: RuleChain<Piece>)
{
    fun verify(): Any? {
        return board
            .getPieceAt(pos).takeIf { board.containsPieceOfPlayer(pos, player) }
            ?.let {next.verify(it)}
    }
}