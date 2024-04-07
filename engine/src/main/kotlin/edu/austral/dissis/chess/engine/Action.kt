package edu.austral.dissis.chess.engine

interface Action {
    fun execute(): GameBoard
}

class Play(private val actions: Iterable<Action>, private val board: GameBoard) : Action {
    override fun execute(): GameBoard {
        var gameBoardAfter = board
        for (action in actions) gameBoardAfter = action.execute()
        return gameBoardAfter
    }
}

class Move : Action {
    private val from: String
    private val to: String
    private val board: GameBoard
    private val pieceNextTurn: Piece

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

    fun asPlay(): Play {
        return Play(listOf(this), board)
    }
}

// class Take(val position: String, val board: GameBoard) : Action {
//    override fun execute(): GameBoard {
//        val gameBoardAfter = board.delPieceAt(position)
//        return gameBoardAfter
//    }
// }

// TODO: Promote
// class Promote(val position: String, val board: GameBoard, val promotionPieceRules: PieceRules) : Action {
//    override fun execute() {
//        val player = board.getPieceAt(position)
//        board.setPieceAt(position, Piece(player, promotionPieceRules))
//    }
// }
