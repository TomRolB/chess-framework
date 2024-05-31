package edu.austral.dissis.chess.engine.board

import edu.austral.dissis.chess.chess.board.HashGameBoard
import edu.austral.dissis.chess.engine.pieces.Piece

private const val ROW_DELIMITER = "|\n|"

private const val POSITION_DELIMITER = '|'

class BoardParser private constructor(val pieces: Map<String, Piece>) {
    companion object {
        fun withPieces(pieces: Map<String, Piece>): BoardParser {
            return BoardParser(pieces)
        }
    }

    fun parse(stringBoard: String): HashGameBoard {
//        String format example:
//        """
//            |  |  |  |  |  |  |  |WK|
//            |  |  |  |  |  |  |  |  |
//            |  |  |BP|  |  |  |  |  |
//            |  |  |  |BP|  |  |  |  |
//            |  |  |  |  |  |  |  |  |
//            |  |  |  |  |  |  |  |  |
//            |  |  |  |  |  |  |WB|  |
//            |BK|  |  |  |  |  |  |  |
//        """

        var boardBuilder: BoardBuilder

        return stringBoard
            .drop(n = 1)
            .dropLast(n = 1)
            .split(ROW_DELIMITER)
            .map { stringRow ->
                stringRow
                    .split(POSITION_DELIMITER)
                    .map { getPiece(it) }
            }
            .also { boardBuilder = BoardBuilder(getValidator(it)) }
            .forEachIndexed { idx, row ->
                boardBuilder = boardBuilder.fillRow(idx + 1, row)
            }
            .let { boardBuilder.build() }
    }

    private fun getPiece(symbol: String): Piece? {
        return if (symbol == "  ") {
            null
        } else {
            pieces[symbol]
                ?.clone()
                ?: throw IllegalArgumentException(
                    "None of the passed pieces can be associated with $symbol",
                )
        }
    }

    private fun getValidator(rows: List<List<Piece?>>): RectangularBoardValidator {
        require(rows.isNotEmpty()) {
            "Cannot build a board with no rows"
        }

        require(allRowsHaveEqualLength(rows)) {
            "Cannot build a board with varying row lengths"
        }

        return RectangularBoardValidator(
            numberRows = rows.size,
            numberCols = rows[0].size,
        )
    }

    private fun allRowsHaveEqualLength(rows: List<List<Piece?>>) = rows.all { it.size == rows[0].size }
}
