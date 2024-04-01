package edu.austral.dissis.chess.engine

class Play(val gameBoard: GameBoard, val actions: Iterable<Action>) {
    fun executeActions() {
        for (action in actions) action.execute()
    }
}

interface Action {
    fun execute()
}

class Move(val from: String, val to: String, val board: GameBoard) : Action {
    override fun execute() {
        val piece = board.getPieceAt(from)
        board.setPieceAt(to, piece)
        board.setPieceAt(from, null)
    }
}

class Take(val position: String, val board: GameBoard) : Action {
    override fun execute() {
        board.setPieceAt(position, null)
    }
}


// TODO: Promote
//class Promote(val position: String, val board: GameBoard, val promotionPieceRules: PieceRules) : Action {
//    override fun execute() {
//        val player = board.getPieceAt(position)
//        board.setPieceAt(position, Piece(player, promotionPieceRules))
//    }
//}