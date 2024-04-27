package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.*
import edu.austral.dissis.chess.rules.IsKingChecked
import edu.austral.dissis.chess.rules.RuleChain

class PostPlayRules(
    val from: Position,
    val to: Position,
    val player: Player,
    val next: RuleChain<Pair<Play, GameBoard>, RuleResult>
) : RuleChain<Play, RuleResult> {
    override fun verify(arg: Play): RuleResult {
        var board = arg.execute()
        val piece = board.getPieceAt(to)!!

        // Get the piece's position as if it was the white player
        val rowAsWhite: Int = board.getRowAsWhite(to, player)
        val positionAsWhite = Position(rowAsWhite, to.col)

        // Promote pawn
        if (piece.rules is PawnPieceRules && board.isPositionOnUpperLimit(positionAsWhite)) {
            val promotionPiece = Piece(piece.player, QueenPieceRules(piece.player))

            board = board.setPieceAt(to, promotionPiece)
        }

        // King should not be checked
        return if (IsKingChecked(board, player).verify()) {
            RuleResult(board, null, EngineResult.POST_PLAY_VIOLATION)
        }
        else next.verify(arg to board)
    }
}