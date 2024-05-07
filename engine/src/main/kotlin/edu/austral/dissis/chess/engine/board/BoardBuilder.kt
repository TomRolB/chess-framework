package edu.austral.dissis.chess.engine.board

import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.pieces.ClassicPieceType.KING
import edu.austral.dissis.chess.engine.pieces.Piece

//TODO: decouple from the kings' positions
class BoardBuilder {
    private val validator: PositionValidator
    private val piecesWithPositions: List<Pair<Position, Piece>>

    constructor(validator: PositionValidator) {
        this.validator = validator
        this.piecesWithPositions = emptyList()
    }

    private constructor(
        validator: PositionValidator,
        pieces: List<Pair<Position, Piece>>,
    ) {
        this.validator = validator
        this.piecesWithPositions = pieces
    }

    fun fillRow(
        row: Int,
        pieces: List<Piece>,
    ): BoardBuilder {
        val rowPiecesWithPositions =
            pieces.mapIndexed {
                    idx, piece ->
                val col = idx + 1

                require(validator.positionExists(Position(row, col))) {
                    "Cannot fill row: trying to set piece at ($row, $col), which is off-limits"
                }

                Position(row, col) to piece
            }

        return BoardBuilder(
            validator,
            piecesWithPositions + rowPiecesWithPositions,
        )
    }

    fun build(): HashChessBoard {
        return HashChessBoard.build(validator, piecesWithPositions)
    }
}
