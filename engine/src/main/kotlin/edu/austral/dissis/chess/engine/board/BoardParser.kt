package edu.austral.dissis.chess.engine.board

import edu.austral.dissis.chess.chess.board.HashGameBoard
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.pieces.Piece

private const val ROW_DELIMITER = "|\n|"

private const val POSITION_DELIMITER = '|'

class BoardParser private constructor(val pieces: Map<Char, Piece>) {
    companion object {
        fun withPieces(pieces: Map<Char, Piece>): BoardParser {
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
//                    .drop(n = 1)
                    .map { parseSymbols(it) }
            }
            .also { boardBuilder = BoardBuilder(getValidator(it)) }
            .forEachIndexed() { idx, row ->
                boardBuilder = boardBuilder.fillRow(idx + 1, row)
            }
            .let { boardBuilder.build() }
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
            numberCols = rows[0].size
        )
    }

    private fun allRowsHaveEqualLength(rows: List<List<Piece?>>) = rows.all { it.size == rows[0].size }

    private fun parseSymbols(symbols: String): Piece? {
        require(symbols.length == 2) {
            "Invalid board pattern: tiles should hold two characters. Received '$symbols'."
        }

        val pieceSymbol = symbols[1]
        val playerSymbol = symbols[0]

        return if (bothSymbolsAreEmpty(pieceSymbol, playerSymbol)) null
        else pieces[pieceSymbol]
            ?.withPlayer(
                parsePlayer(playerSymbol)
            )
            ?: throw IllegalArgumentException(
                "Did not pass a piece to map $pieceSymbol to"
            )
    }

    private fun bothSymbolsAreEmpty(
        pieceSymbol: Char,
        playerSymbol: Char
    ) = pieceSymbol == ' ' && playerSymbol == ' '

    private fun parsePlayer(char: Char): Player {
        return when (char) {
            'W' -> Player.WHITE
            'B' -> Player.BLACK
            else -> throw IllegalArgumentException(
                "Cannot parse player symbol '${char}'"
            )
        }
    }
}