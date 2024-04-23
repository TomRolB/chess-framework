package edu.austral.dissis.chess.engine

interface Action {
    fun execute(): GameBoard
    fun setBoard(board: GameBoard): Action
}

class Play(val actions: Iterable<Action>, private val board: GameBoard) : Action {
    override fun execute(): GameBoard {
//        var gameBoardAfter = board
//        for (action in actions) {
//            action.javaClass.getConstructor().newInstance()
//            gameBoardAfter = action.execute()
//        }
//        return gameBoardAfter

        return actions
            .reduce { action, next -> next.setBoard(action.execute()) }
            .execute()
    }

    override fun setBoard(board: GameBoard): Action {
        return Play(actions, board)
    }
}

class Move : Action {
    val from: Position
    val to: Position
    val board: GameBoard
    val pieceNextTurn: Piece

    constructor(from: Position, to: Position, board: GameBoard) {
        this.from = from
        this.to = to
        this.board = board
        this.pieceNextTurn = board.getPieceAt(from)!! // At this point, we've made all necessary checks
    }

    constructor(from: Position, to: Position, board: GameBoard, pieceNextTurn: Piece) {
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

    override fun setBoard(board: GameBoard): Action {
        return Move(from, to, board, pieceNextTurn)
    }

    fun asPlay(): Play {
        return Play(listOf(this), board)
    }
}



 class Take(val position: Position, val board: GameBoard) : Action {
     override fun execute(): GameBoard {
        val gameBoardAfter = board.delPieceAt(position)
        return gameBoardAfter
     }

     override fun setBoard(board: GameBoard): Action {
         return Take(position, board)
     }
 }

// TODO: Promote
// class Promote(val position: Position, val board: GameBoard, val promotionPieceRules: PieceRules) : Action {
//    override fun execute() {
//        val player = board.getPieceAt(position)
//        board.setPieceAt(position, Piece(player, promotionPieceRules))
//    }
// }
