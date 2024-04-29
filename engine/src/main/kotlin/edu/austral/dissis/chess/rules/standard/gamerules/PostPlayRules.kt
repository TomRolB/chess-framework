package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.pieces.Pawn
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.Queen
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.rules.pieces.king.IsKingChecked
import edu.austral.dissis.chess.rules.RuleChain

class PostPlayRules(
    val from: Position,
    val to: Position,
    val player: Player,
    val next: RuleChain<Pair<Play, GameBoard>, RuleResult>,
) : RuleChain<Play, RuleResult> {
    override fun verify(arg: Play): RuleResult {
        var board = arg.execute()
        val piece = board.getPieceAt(to)!!

        // Get the piece's position as if it was the white player
        val rowAsWhite: Int = board.getRowAsWhite(to, player)
        val positionAsWhite = Position(rowAsWhite, to.col)

        // Promote pawn
        if (piece.type is Pawn && board.isPositionOnUpperLimit(positionAsWhite)) {
            val promotionPiece = Piece(piece.player, Queen(piece.player))

            board = board.setPieceAt(to, promotionPiece)
        }

        // King should not be checked
        return if (IsKingChecked(board, player).verify()) {
            RuleResult(board, null, EngineResult.POST_PLAY_VIOLATION, "That movement would leave your king checked")
        } else {
            next.verify(arg to board)
        }
    }
}
