package edu.austral.dissis.chess.engine

interface Action {
    fun execute()
}

class Play(val actions: Iterable<Action>): Action {
    override fun execute() {
        for (action in actions) action.execute()
    }
}

class Move(val from: String, val to: String, val board: GameBoard) : Action {
    override fun execute() {
        val piece: Piece = board.getPieceAt(from)!! // At this point, we've made all necessary checks
        board.setPieceAt(to, piece)
        board.delPieceAt(from)
    }
}

class Take(val position: String, val board: GameBoard) : Action {
    override fun execute() {
        board.delPieceAt(position)
    }
}


// TODO: Promote
//class Promote(val position: String, val board: GameBoard, val promotionPieceRules: PieceRules) : Action {
//    override fun execute() {
//        val player = board.getPieceAt(position)
//        board.setPieceAt(position, Piece(player, promotionPieceRules))
//    }
//}