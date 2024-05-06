package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.Piece

sealed interface Action {
    fun execute(): ChessBoard

    fun setBoard(board: ChessBoard): Action
}

class Play(val actions: Iterable<Action>) : Action {
    override fun execute(): ChessBoard {
        return actions
            .reduce { action, next -> next.setBoard(action.execute()) }
            .execute()
    }

    override fun setBoard(board: ChessBoard): Action {
        return Play(actions)
    }
}

class Move : Action {
    val from: Position
    val to: Position
    val board: ChessBoard
    val pieceNextTurn: Piece

    constructor(from: Position, to: Position, board: ChessBoard) {
        this.from = from
        this.to = to
        this.board = board
        this.pieceNextTurn = board.getPieceAt(from)!! // At this point, we've made all necessary checks
    }

    constructor(from: Position, to: Position, board: ChessBoard, pieceNextTurn: Piece) {
        this.from = from
        this.to = to
        this.board = board
        this.pieceNextTurn = pieceNextTurn
    }

    override fun execute(): ChessBoard {
        val gameBoardAfter =
            board
                .setPieceAt(to, pieceNextTurn)
                .delPieceAt(from)

        return gameBoardAfter
    }

    override fun setBoard(board: ChessBoard): Action {
        return Move(from, to, board, pieceNextTurn)
    }

    fun asPlay(): Play {
        return Play(listOf(this))
    }

    fun withPiece(piece: Piece): Move {
        return Move(from, to, board, piece)
    }
}

class Take(val position: Position, val board: ChessBoard) : Action {
    override fun execute(): ChessBoard {
        val gameBoardAfter = board.delPieceAt(position)
        return gameBoardAfter
    }

    override fun setBoard(board: ChessBoard): Action {
        return Take(position, board)
    }
}

fun Play.extractMove(): Move {
    return this.actions
        .find { it is Move }
        ?.let { it as Move }
        ?: throw IllegalArgumentException(
            "There is no Move in this Play",
        )
}
