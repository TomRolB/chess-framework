package edu.austral.dissis.chess.engine.pieces

import edu.austral.dissis.chess.engine.ClassicMoveType
import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.rules.pieces.king.IsKingChecked


//TODO: This will be refactored as follows:
// 1. We need to separate MoveType into individual directions
// (South, East, Northeast, etc.)
// 2. Should create a Filter over movement rules which handles
// the hasEverMoved state and situations
// 3.
class Rook : MoveDependantPieceRule {
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


        //TODO: Idea: convert to something like IndependentRuleChain. It should evaluate many
        // rules which support getPlayIfValid() and return the corresponding PlayResult
        return when {
            moveType.isViolated(moveData) -> PlayResult(null, "A rook cannot move this way")
            moveType.isPathBlocked(moveData, board) -> PlayResult(null, "Cannot move there: the path is blocked")
            //TODO: is it possible to decouple hasEverMoved from the piece?
            !hasEverMoved -> {
                val rulesNextTurn = Rook(player, true)
                val pieceNextTurn = Piece(player, rulesNextTurn)
                PlayResult(Move(from, to, board, pieceNextTurn).asPlay(), "Valid play")
            }
            else -> PlayResult(Move(from, to, board).asPlay(), "Valid play")
        }
    }

// TODO: rules should be sth like...
//    list rules = rules
//    for rule in rules
//    rule. verify
//  ...or similar. This allows to create piece rules on the fly.
//  Maybe using something like IndependentRuleChain again

// TODO: would be nice to create some of the chess variants,
//  to test the design. e.g. Initializing a different game,
//  creating special pieces, different win conditions,
//  being able to change board size, etc.
//  Create the variant for the next week, if possible.
//  This will eventually be evaluated.

}
