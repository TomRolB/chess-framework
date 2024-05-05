package edu.austral.dissis.chess.engine.pieces

import edu.austral.dissis.chess.engine.ClassicMoveType
import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.rules.castling.Castling
import edu.austral.dissis.chess.rules.pieces.king.IsKingChecked

private const val C_COLUMN = 3

private const val G_COLUMN = 7

class King : MoveDependantPieceRule {
    val player: Player
    private val moveType = ClassicMoveType.ADJACENT_SQUARE
    override val hasEverMoved: Boolean

    constructor(player: Player) {
        this.player = player
        this.hasEverMoved = false
    }

    constructor(player: Player, hasEverMoved: Boolean) {
        this.player = player
        this.hasEverMoved = hasEverMoved
    }

    fun asMoved(): Piece {
        // Return this piece with hasEverMoved = true
        return Piece("king", player, King(player, hasEverMoved = true))
    }

    override fun getValidPlays(
        board: ChessBoard,
        position: Position,
    ): Iterable<Play> {
        return moveType
            .getPossiblePositions(board, position)
            .map {
                val pieceNextTurn = Piece("king", player, King(player, hasEverMoved = true))
                Move(position, it, board, pieceNextTurn).asPlay()
            }
            .filter {
                val futureBoard = it.execute()
                !IsKingChecked(futureBoard, player).verify()
            }
            .plus(
                // Possible castling
                listOf(
                    Position(position.row, C_COLUMN),
                    Position(position.row, G_COLUMN),
                ).mapNotNull {
                    getPlayIfValid(board, position, it).play
                },
            )
    }

    override fun getPlayIfValid(
        board: ChessBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val moveData = MovementData(from, to)
        return when {
            moveType.isViolated(moveData) -> {
                Castling(this, hasEverMoved, board, from, to).verify()
                    ?.let { PlayResult(it, "Valid play") }
                    ?: PlayResult(null, "King cannot move this way")
            }
            else -> {
                val pieceNextTurn = Piece("king", player, King(player, hasEverMoved = true))
                PlayResult(
                    Move(from, to, board, pieceNextTurn).asPlay(),
                    "Valid play",
                )
            }
        }
    }
}
