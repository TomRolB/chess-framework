package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.Piece

sealed interface Action {
    fun execute(): GameBoard

    fun setBoard(board: GameBoard): Action
}

class Play(val actions: Iterable<Action>) : Action {
    override fun execute(): GameBoard {
        return actions
            .reduce { action, next -> next.setBoard(action.execute()) }
            .execute()
    }

    override fun setBoard(board: GameBoard): Action {
        return Play(actions)
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
        return Play(listOf(this))
    }

    fun withPiece(piece: Piece): Move {
        return Move(from, to, board, piece)
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

fun Play.extractMove(): Move {
    return this.actions
        .find { it is Move }
        ?.let { it as Move }
        ?: throw IllegalArgumentException(
            "There is no Move in this Play",
        )
}
