package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.checkers.CheckersPieceType
import edu.austral.dissis.chess.checkers.getCheckersBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.Piece
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class BoardBuildingTests {
    @Test
    fun `build initial checkers board`() {
        val gameBoard = getCheckersBoard()

        for (position in getAllCombinationsAsPositions(1..8, 1..8)) {
            val piece: Piece? = gameBoard.getPieceAt(position)
            if (shouldPositionBeEmpty(position)) assertNull(piece)
            else assertPiece(position, piece)
        }
    }

    private fun assertPiece(
        position: Position,
        piece: Piece?,
    ) {
        assertEquals(piece!!.type, CheckersPieceType.MAN)

        if (position.row <= 3) assertEquals(Player.WHITE, piece.player)
        else assertEquals(Player.BLACK, piece.player)
    }

    private fun shouldPositionBeEmpty(position: Position) =
        isEven(position) || (position.row in 4..5)

    private fun getAllCombinationsAsPositions(rows: IntRange, cols: IntRange): Iterable<Position> {
        return rows.flatMap { row -> cols.map { col -> Position(row, col) } }
    }

    private fun isEven(position: Position): Boolean {
        return (position.row + position.col) % 2 == 0
    }
}