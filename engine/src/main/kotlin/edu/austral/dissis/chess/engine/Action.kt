package edu.austral.dissis.chess.engine

interface Action {
    fun execute(): GameBoard
}

class Play(val actions: Iterable<Action>, val board: GameBoard): Action {
    override fun execute(): GameBoard {
        var gameBoardAfter = board
        for (action in actions) gameBoardAfter = action.execute()
        return gameBoardAfter
    }
}

class Move : Action {
    val from: String
    val to: String
    val board: GameBoard
    val pieceNextTurn: Piece

    constructor(from: String, to: String, board: GameBoard) {
        this.from = from
        this.to = to
        this.board = board
        this.pieceNextTurn = board.getPieceAt(from)!! // At this point, we've made all necessary checks
    }

    constructor(from: String, to: String, board: GameBoard, pieceNextTurn: Piece) {
        this.from = from
        this.to = to
        this.board = board
        this.pieceNextTurn = pieceNextTurn
    }

    override fun execute(): GameBoard {
        val gameBoardAfter =
            board
                .setPieceAt(to, pieceNextTurn)
                .delPieceAt(from)

        return gameBoardAfter
    }
}

class Take(val position: String, val board: GameBoard) : Action {
    override fun execute(): GameBoard {
        val gameBoardAfter = board.delPieceAt(position)
        return gameBoardAfter
    }
}


// TODO: Promote
//class Promote(val position: String, val board: GameBoard, val promotionPieceRules: PieceRules) : Action {
//    override fun execute() {
//        val player = board.getPieceAt(position)
//        board.setPieceAt(position, Piece(player, promotionPieceRules))
//    }
//}