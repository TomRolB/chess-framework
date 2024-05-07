package edu.austral.dissis.chess.engine.board

import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.pieces.Piece
import java.lang.IllegalStateException

class BoardBuilder {
    private val validator: PositionValidator
    private val pieces: List<Pair<Position, Piece>>
    private val whiteKingPosition: Position?
    private val blackKingPosition: Position?

    constructor(validator: PositionValidator) {
        this.validator = validator
        this.pieces = emptyList()
        this.whiteKingPosition = null
        this.blackKingPosition = null
    }

    private constructor(
        validator: PositionValidator,
        pieces: List<Pair<Position, Piece>>,
        whiteKingPosition: Position?,
        blackKingPosition: Position?
    ) {
        this.validator = validator
        this.pieces = pieces
        this.whiteKingPosition = whiteKingPosition
        this.blackKingPosition = blackKingPosition
    }

    fun fillRow(row: Int, pieces: List<Piece>): BoardBuilder {
        for (col in 1..pieces.size) {
            if (!validator.positionExists(Position(row, col))) {
                throw IllegalArgumentException(
                    "Cannot fill row: trying to set piece at ($row, $col), which is off-limits"
                )
            }
        }

        var blackKingPosition: Position? = null
        var whiteKingPosition: Position? = null

        val piecesWithPos = pieces.mapIndexed {
            col, piece ->
            require (!validator.positionExists(Position(row, col))) {
                "Cannot fill row: trying to set piece at ($row, $col), which is off-limits"
            }

            val position = Position(row, col)

            if (piece.type == "king") {
                when (piece.player) {
                    Player.BLACK -> blackKingPosition = position
                    Player.WHITE -> whiteKingPosition = position
                }
            }

            position to piece
        }

        return BoardBuilder(validator, piecesWithPos, whiteKingPosition, blackKingPosition)
    }

    fun build(): HashChessBoard {
        check (whiteKingPosition != null) {
            "No white king was passed"
        }

        check (blackKingPosition != null) {
            "No black king was passed"
        }

        return HashChessBoard.build(validator, pieces, whiteKingPosition, blackKingPosition)
    }
}