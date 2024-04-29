package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.rules.IsKingChecked
import edu.austral.dissis.chess.rules.castling.Castling
import edu.austral.dissis.chess.rules.pieces.pawn.PawnValidMove

// TODO: Is this explanation necessary?
// Our engine is not interested in whether two pieces of the same
// type are different objects or not: the pieces are immutable,
// and the engine identifies the difference in positions merely
// from the game board.

// The only thing to consider is having separate states for these
// pieces, which is achieved with different PieceRules instantiations.

// Therefore, we use a data class, to allow hashmap usage.
data class Piece(val player: Player, val rules: PieceRules) {
    override fun toString(): String {
        return "$player, $rules"
    }

    override fun hashCode(): Int {
        return (player to rules::class).hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is Piece &&
            this.hashCode() == other.hashCode()
    }
}

data class PlayResult(val play: Play?, val message: String)

interface PieceRules {
    fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play>

    fun getPlayIfValid(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult
}

interface MoveDependantPieceRules : PieceRules {
    val hasEverMoved: Boolean
}

class PawnPieceRules : MoveDependantPieceRules {
    private val player: Player
    val hasJustMovedTwoPlaces: Boolean
    private val increments = listOf(1 to 1, 0 to 1, -1 to 1, 0 to 2)
    override val hasEverMoved: Boolean

    // TODO: consider modifying this
    enum class State {
        MOVED,
        MOVED_TWO_PLACES,
    }

    constructor(player: Player) {
        this.player = player
        this.hasEverMoved = false
        this.hasJustMovedTwoPlaces = false
    }

    constructor(player: Player, state: State) {
        this.player = player
        when (state) {
            State.MOVED -> {
                this.hasEverMoved = true
                this.hasJustMovedTwoPlaces = false
            }
            State.MOVED_TWO_PLACES -> {
                this.hasEverMoved = true
                this.hasJustMovedTwoPlaces = true
            }
        }
    }

    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        val (row, col) = position

        return increments
            .map { Position(row + it.first, col + it.second) }
            .mapNotNull { getPlayIfValid(board, position, it).play }
            .filter {
                val futureBoard = it.execute()
                !IsKingChecked(futureBoard, player).verify()
            }
    }

    override fun getPlayIfValid(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        return PawnValidMove(board, from, to, player, hasEverMoved)
            .verify()
            ?.let { PlayResult(it, "Valid play") }
            ?: PlayResult(null, "Pawn cannot move this way")
    }
}

class RookPieceRules : MoveDependantPieceRules {
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
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        return moveType
            .getPossiblePositions(board, position)
            .map {
                val pieceNextTurn = Piece(player, RookPieceRules(player, hasEverMoved = true))
                Play(listOf(Move(position, it, board, pieceNextTurn)))
            }
            .filter {
                val futureBoard = it.execute()
                !IsKingChecked(futureBoard, player).verify()
            }
    }

    override fun getPlayIfValid(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val moveData = MovementData(from, to, board)

        return when {
            moveType.isViolated(moveData) -> PlayResult(null, "A rook cannot move this way")
            moveType.isPathBlocked(moveData, board) -> PlayResult(null, "Cannot move there: the path is blocked")
            !hasEverMoved -> {
                val rulesNextTurn = RookPieceRules(player, true)
                val pieceNextTurn = Piece(player, rulesNextTurn)
                PlayResult(Move(from, to, board, pieceNextTurn).asPlay(), "Valid play")
            }
            else -> PlayResult(Move(from, to, board).asPlay(), "Valid play")
        }
    }
}

class BishopPieceRules(val player: Player) : PieceRules {
    private val moveType = ClassicMoveType.DIAGONAL

    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        return getValidPlaysFromMoveType(moveType, board, position, player)
    }

    override fun getPlayIfValid(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val moveData = MovementData(from, to, board)

        return when {
            moveType.isViolated(moveData)
            -> PlayResult(null, "A bishop cannot move this way")
            moveType.isPathBlocked(moveData, board)
            -> PlayResult(null, "Cannot move there: the path is blocked")
            else -> PlayResult(Move(from, to, board).asPlay(), "Valid move")
        }
    }
}

class QueenPieceRules(val player: Player) : PieceRules {
    private val moveType = ClassicMoveType.ANY_STRAIGHT_LINE

    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        return getValidPlaysFromMoveType(moveType, board, position, player)
    }

    override fun getPlayIfValid(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val moveData = MovementData(from, to, board)

        return when {
            moveType.isViolated(moveData)
            -> PlayResult(null, "A queen cannot move this way")
            moveType.isPathBlocked(moveData, board)
            -> PlayResult(null, "Cannot move there: the path is blocked")
            else -> PlayResult(Move(from, to, board).asPlay(), "Valid move")
        }
    }
}

class KnightPieceRules(val player: Player) : PieceRules {
    private val moveType = ClassicMoveType.L_SHAPED

    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        return getValidPlaysFromMoveType(moveType, board, position, player)
    }

    override fun getPlayIfValid(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val moveData = MovementData(from, to, board)

        return when {
            moveType.isViolated(moveData) ->
                PlayResult(null, "A knight cannot move this way")
            else -> PlayResult(Move(from, to, board).asPlay(), "Valid move")
        }
    }
}

private const val C_COLUMN = 3

private const val G_COLUMN = 7

class KingPieceRules : MoveDependantPieceRules {
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
        return Piece(player, KingPieceRules(player, hasEverMoved = true))
    }

    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        return moveType
            .getPossiblePositions(board, position)
            .map {
                val pieceNextTurn = Piece(player, KingPieceRules(player, hasEverMoved = true))
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
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val moveData = MovementData(from, to, board)
        return when {
            // TODO: Could be better
            moveType.isViolated(moveData) -> {
                Castling(this, hasEverMoved, board, from, to).verify()
                    ?.let { PlayResult(it, "Valid play") }
                    ?: PlayResult(null, "King cannot move this way")
            }
            else -> {
                val pieceNextTurn = Piece(player, KingPieceRules(player, hasEverMoved = true))
                PlayResult(Move(from, to, board, pieceNextTurn).asPlay(), "Valid play")
            }
        }
    }

    companion object {
        // TODO: willBeChecked should be a Rule
        private fun willBeChecked(
            board: GameBoard,
            player: Player,
        ): Boolean {
            return board.getAllPositionsOfPlayer(player, true).all {
                val piece = board.getPieceAt(it)!!
                allMovementsEndInCheck(board, piece, it)
            }
        }

        private fun allMovementsEndInCheck(
            board: GameBoard,
            piece: Piece,
            position: Position,
        ): Boolean {
            return piece.rules.getValidPlays(board, position).all {
                val futureBoard = it.execute()
                IsKingChecked(futureBoard, piece.player).verify()
            }
        }

        fun getPlayerState(
            board: GameBoard,
            player: Player,
        ): PlayerState {
            val isChecked: Int = if (IsKingChecked(board, player).verify()) 1 else 0
            val willBeChecked: Int = if (willBeChecked(board, player)) 1 else 0
            val combinedStatus: Int = isChecked * 2 + willBeChecked

            return PlayerState.entries[combinedStatus]
        }
    }
}

// TODO: Maybe we can manage special case where piece is move-dependant
fun getValidPlaysFromMoveType(
    moveType: MoveType,
    board: GameBoard,
    position: Position,
    player: Player,
) = moveType
    .getPossiblePositions(board, position)
    .map {
        Play(listOf(Move(position, it, board)))
    }
    .filter {
        val futureBoard = it.execute()
        !IsKingChecked(futureBoard, player).verify()
    }
