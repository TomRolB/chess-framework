package edu.austral.dissis.chess.engine.pieces

import edu.austral.dissis.chess.engine.ClassicMoveType
import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.rules.pieces.king.IsKingChecked

class Rook : MoveDependantPieceType {
    private val moveType = ClassicMoveType.VERTICAL_AND_HORIZONTAL
    private val player: Player
    override val hasEverMoved: Boolean

    constructor(player: Player) {
        this.player = player
        this.hasEverMoved = false
    }

    constructor(player: Player, hasEverMoved: Boolean) {
        this.player = player
        this.hasEverMoved = hasEverMoved
    }

    override fun getValidPlays(
        board: ChessBoard,
        position: Position,
    ): Iterable<Play> {
        return moveType
            .getPossiblePositions(board, position)
            .map {
                val pieceNextTurn = Piece(player, Rook(player, hasEverMoved = true))
                Play(listOf(Move(position, it, board, pieceNextTurn)))
            }
            .filter {
                val futureBoard = it.execute()
                !IsKingChecked(futureBoard, player).verify()
            }
    }

    override fun getPlayIfValid(
        board: ChessBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val moveData = MovementData(from, to)

        return when {
            moveType.isViolated(moveData) -> PlayResult(null, "A rook cannot move this way")
            moveType.isPathBlocked(moveData, board) -> PlayResult(null, "Cannot move there: the path is blocked")
            !hasEverMoved -> {
                val rulesNextTurn = Rook(player, true)
                val pieceNextTurn = Piece(player, rulesNextTurn)
                PlayResult(Move(from, to, board, pieceNextTurn).asPlay(), "Valid play")
            }
            else -> PlayResult(Move(from, to, board).asPlay(), "Valid play")
        }
    }
}
