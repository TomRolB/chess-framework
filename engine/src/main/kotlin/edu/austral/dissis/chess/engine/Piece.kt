package edu.austral.dissis.chess.engine

class Piece(val player: Player, val rules: PieceRules) {
    fun isPlayValid(
        from: String,
        to: String,
    ): Boolean {
        return rules.isPlayValid(from, to)
    }

//    fun getValidPlays(): Set<Play> {
//        return rules.getValidPlays()
//    }

//    fun getPlayIfValid(from: String, to: String): Play? {
//        return rules.getPlayIfValid(from, to)
//    }
}

interface PieceRules {
    fun isPlayValid(
        from: String,
        to: String,
    ): Boolean

    fun getValidPlays(
        board: GameBoard,
        position: String,
    ): Iterable<Play>

    fun getPlayIfValid(
        board: GameBoard,
        from: String,
        to: String,
    ): Play?
}

class PawnPieceRules : PieceRules {
    private val player: Player
    private val hasEverMoved: Boolean
    private val hasJustMovedTwoPlaces: Boolean
    private val increments = listOf(1 to 1, 0 to 1, -1 to 1, 0 to 2)

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

    override fun isPlayValid(
        from: String,
        to: String,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValidPlays(
        board: GameBoard,
        position: String,
    ): Iterable<Play> {
        val (row, col) = board.unpackPosition(position)

        return increments
            .map { getStringPosition(row + it.first, col + it.second) }
            .mapNotNull { getPlayIfValid(board, position, it) }
    }

    override fun getPlayIfValid(
        board: GameBoard,
        from: String,
        to: String,
    ): Play? {
        // In this case, moveData is always created from
        // WHITE's point of view, to avoid splitting rules
        // based on the player
        val moveData = MovementData(from, to, board, player)

        // TODO: en passant

        return when (moveData.rowDelta) {
            1 -> moveAheadOrDiagonallyIfValid(board, moveData)
            2 -> moveTwoPlacesIfValid(board, moveData)
            else -> {
                println("A pawn cannot move this way")
                null
            }
        }
    }

    private fun moveTwoPlacesIfValid(
        board: GameBoard,
        moveData: MovementData,
    ): Play? {
        if (hasEverMoved || pathIsBlocked(board, moveData)) {
            return null
        }

        val rulesNextTurn = PawnPieceRules(player, State.MOVED_TWO_PLACES)
        val pieceNextTurn = Piece(player, rulesNextTurn)
        return Play(listOf(Move(moveData.from, moveData.to, board, pieceNextTurn)), board)
    }

    private fun pathIsBlocked(
        board: GameBoard,
        moveData: MovementData,
    ): Boolean {
        val front: String = getStringPosition(moveData.fromCol, moveData.fromRow + 1)
        return board.isOccupied(moveData.to) || board.isOccupied(front)
    }

    private fun moveAheadOrDiagonallyIfValid(
        board: GameBoard,
        moveData: MovementData,
    ): Play? {
        return when (moveData.colDelta) {
            0 -> {
                if (board.isOccupied(moveData.to)) {
                    null
                } else {
                    val rulesNextTurn = PawnPieceRules(player, State.MOVED)
                    val pieceNextTurn = Piece(player, rulesNextTurn)
                    Move(moveData.from, moveData.to, board, pieceNextTurn).asPlay()
                }
            }
            1, -1 -> {
                if (board.containsPieceOfPlayer(moveData.to, !player)) {
                    val rulesNextTurn = PawnPieceRules(player, State.MOVED)
                    val pieceNextTurn = Piece(player, rulesNextTurn)
                    Move(moveData.from, moveData.to, board, pieceNextTurn).asPlay()
                } else {
                    enPassantIfValid(board, moveData)
                }
            }

            else -> {
                println("A pawn cannot move this way")
                null
            }
        }
    }

    private fun enPassantIfValid(board: GameBoard, moveData: MovementData): Play? {
        val enemyPawnPosition = getStringPosition(moveData.fromRow, moveData.toCol)
        if (!board.containsPieceOfPlayer(enemyPawnPosition, !player)) return null

        val enemyPawn = board.getPieceAt(enemyPawnPosition)!!
        if (enemyPawn.rules !is PawnPieceRules) return null

        val enemyRules = enemyPawn.rules as PawnPieceRules
        if (!enemyRules.hasJustMovedTwoPlaces) return null

        return Play(
            listOf(
                Move(moveData.from, moveData.to, board),
                Take(enemyPawnPosition, board)
            ),
            board
        )
    }
}

class RookPieceRules : PieceRules {
    private val moveType = MoveType.VERTICAL_AND_HORIZONTAL
    private val player: Player
    private val hasEverMoved: Boolean

    constructor(player: Player) {
        this.player = player
        this.hasEverMoved = false
    }

    constructor(player: Player, hasEverMoved: Boolean) {
        this.player = player
        this.hasEverMoved = true
    }

    override fun isPlayValid(
        from: String,
        to: String,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValidPlays(
        board: GameBoard,
        position: String,
    ): Iterable<Play> {
        return moveType
            .getPossiblePositions(board, position)
            .map { Play(listOf(Move(position, it, board)), board) }
    }

    override fun getPlayIfValid(
        board: GameBoard,
        from: String,
        to: String,
    ): Play? {
        val moveData = MovementData(from, to, board)

        return when {
            moveType.isViolated(moveData) -> {
                println("A tower cannot move this way")
                null
            }
            moveType.isPathBlocked(moveData, board) -> {
                println("Cannot move there: the path is blocked")
                null
            }
            !hasEverMoved -> {
                val rulesNextTurn = RookPieceRules(player, true)
                val pieceNextTurn = Piece(player, rulesNextTurn)
                Move(from, to, board, pieceNextTurn).asPlay()
            }
            else -> {
                Move(from, to, board).asPlay()
            }
        }
    }
}

class BishopPieceRules : PieceRules {
    val moveType = MoveType.DIAGONAL

    override fun isPlayValid(
        from: String,
        to: String,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValidPlays(
        board: GameBoard,
        position: String,
    ): Iterable<Play> {
        return moveType
            .getPossiblePositions(board, position)
            .map {
                Play(listOf(Move(position, it, board)), board)
            }
    }

    override fun getPlayIfValid(
        board: GameBoard,
        from: String,
        to: String,
    ): Play? {
        val moveData = MovementData(from, to, board)

        return when {
            moveType.isViolated(moveData) -> {
                println("A bishop cannot move this way")
                null
            }
            moveType.isPathBlocked(moveData, board) -> {
                println("Cannot move there: the path is blocked")
                null
            }
            else -> {
                Move(from, to, board).asPlay()
            }
        }
    }
}

class QueenPieceRules : PieceRules {
    val moveType = MoveType.DIAGONAL

    override fun isPlayValid(
        from: String,
        to: String,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValidPlays(
        board: GameBoard,
        position: String,
    ): Iterable<Play> {
        return moveType
            .getPossiblePositions(board, position)
            .map { Play(listOf(Move(position, it, board)), board) }
    }

    override fun getPlayIfValid(
        board: GameBoard,
        from: String,
        to: String,
    ): Play? {
        val moveData = MovementData(from, to, board)

        return when {
            MoveType.ANY_STRAIGHT_LINE.isViolated(moveData) -> {
                println("A queen cannot move this way")
                null
            }
            MoveType.ANY_STRAIGHT_LINE.isPathBlocked(moveData, board) -> {
                println("Cannot move there: the path is blocked")
                null
            }
            else -> {
                Move(from, to, board).asPlay()
            }
        }
    }
}

class KnightPieceRules : PieceRules {
    val moveType = MoveType.L_SHAPED

    override fun isPlayValid(
        from: String,
        to: String,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValidPlays(
        board: GameBoard,
        position: String,
    ): Iterable<Play> {
        return moveType
            .getPossiblePositions(board, position)
            .map { Move(position, it, board).asPlay() }
    }

    override fun getPlayIfValid(
        board: GameBoard,
        from: String,
        to: String,
    ): Play? {
        val moveData = MovementData(from, to, board)

        return when {
            moveType.isViolated(moveData) -> {
                println("A bishop cannot move this way")
                null
            }
            else -> Move(from, to, board).asPlay()
        }
    }
}

class KingPieceRules(val player: Player) : PieceRules {
    val moveType = MoveType.ADJACENT_SQUARE

    override fun isPlayValid(
        from: String,
        to: String,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValidPlays(
        board: GameBoard,
        position: String,
    ): Iterable<Play> {
        return moveType
            .getPossiblePositions(board, position)
            .filter {
                val moveData = MovementData(position, it, board)
                !becomesChecked(board, moveData)
            }
            .map { Move(position, it, board).asPlay() }
    }

    override fun getPlayIfValid(
        board: GameBoard,
        from: String,
        to: String,
    ): Play? {
        val moveData = MovementData(from, to, board)
        return when {
            moveType.isViolated(moveData) -> {
                println("The king cannot move this way")
                null
            }
            becomesChecked(board, moveData) -> {
                println("Invalid movement: the king would become checked")
                null
            }
            else -> Move(from, to, board).asPlay()
        }
    }
    
    private fun becomesChecked(
        board: GameBoard,
        moveData: MovementData,
    ): Boolean {
        val futureBoard: GameBoard = Move(moveData.from, moveData.to, board).execute()
        return isChecked(futureBoard, player)
    }

    companion object {
        val possibleIncrements = listOf(
            1 to 0, 1 to 1, 0 to 1, -1 to 1,
            -1 to 0, -1 to -1, 0 to -1, -1 to 1
        )

        fun isChecked(board: GameBoard, player: Player): Boolean {
            val kingPosition = board.getKingPosition(player)

            // Return true if any enemy piece "can capture" the King
            // We don't include the enemy king, since the kings cannot
            // check each other
            return board.getAllPositionsOfPlayer(!player, false).any {
                val enemyPosition: String = it
                val enemyPiece: Piece = board.getPieceAt(enemyPosition)!!
                val kingCapture: Play? = enemyPiece.rules.getPlayIfValid(board, enemyPosition, kingPosition)

                kingCapture != null
            }
        }

        //        fun willBeChecked(board: GameBoard, player: Player): Boolean {
//            val kingPosition = board.getKingPosition(player)
//
//            return getPossibleFuturePositions(board, player, kingPosition)
//                .all {
//                    val futureBoard = Play(listOf(Move(kingPosition, it, board)), board).execute()
//                    isChecked(futureBoard, player)
//                }
//        }
        fun willBeChecked(board: GameBoard, player: Player): Boolean {
            return board.getAllPositionsOfPlayer(player, true).all {
                val piece = board.getPieceAt(it)!!
                allMovementsEndInCheck(board, piece, it)
            }
        }

        private fun allMovementsEndInCheck(board: GameBoard, piece: Piece, position: String): Boolean {
            return piece.rules.getValidPlays(board, position).all {
                val futureBoard = it.execute()
                isChecked(futureBoard, piece.player)
            }
        }

        private fun getPossibleFuturePositions(board: GameBoard, player: Player, kingPosition: String): Iterable<String> {
            val (row, col) = board.unpackPosition(kingPosition)

            return possibleIncrements
                .map { getStringPosition(row + it.first, col + it.second) }
                .filter { board.positionExists(it) && !board.containsPieceOfPlayer(it, player) }
        }

        fun getPlayerState(board: GameBoard, player: Player): PlayerState {
            val isChecked: Int = if (isChecked(board, player)) 1 else 0
            val willBeChecked: Int = if (willBeChecked(board, player)) 1 else 0
            val combinedStatus: Int = isChecked * 2 + willBeChecked

            return when (combinedStatus) {
                0 -> PlayerState.NORMAL
                2 -> PlayerState.CHECKED
                1 -> PlayerState.STALEMATE
                3 -> PlayerState.CHECKMATE
                else -> throw IllegalStateException("Invalid combined status")
            }
        }
    }
}
