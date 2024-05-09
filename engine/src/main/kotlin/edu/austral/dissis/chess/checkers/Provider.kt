package edu.austral.dissis.chess.checkers

import edu.austral.dissis.chess.checkers.CheckersPieceType.KING
import edu.austral.dissis.chess.checkers.CheckersPieceType.MAN
import edu.austral.dissis.chess.checkers.rules.JumpManager
import edu.austral.dissis.chess.chess.rules.updaters.PromotionUpdater
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.pieces.PieceType
import edu.austral.dissis.chess.engine.rules.pieces.CombinedRules
import edu.austral.dissis.chess.engine.rules.pieces.IncrementalMovement
import edu.austral.dissis.chess.engine.rules.pieces.NoPieceAtFinalPosition
import edu.austral.dissis.chess.engine.rules.pieces.PathMovementRules
import edu.austral.dissis.chess.engine.rules.pieces.Update

// TODO: idea: create a rule which is passed a LIMIT. This rules makes
// the piece move to a certain place only if taking some piece in the
// process, but it cannot surpass LIMIT.
// The question is whether this can be implemented in PathMovementRules,
// to have a more generic move.

fun getMan(player: Player) =
    Piece(
        type = MAN,
        player = player,
        rules =
            Update(
                PromotionUpdater(getKing(player)),
                CombinedRules(
                    // TODO: Should mirror inside PathMovementRules
                    PathMovementRules(1 to 1, JumpManager(2, 1, 1)),
                    PathMovementRules(1 to -1, JumpManager(2, 1, 1)),
                    PathMovementRules(-1 to 1, JumpManager(2, 1, 1)),
                    PathMovementRules(-1 to -1, JumpManager(2, 1, 1)),
                    NoPieceAtFinalPosition(IncrementalMovement(1, 1, player)),
                    NoPieceAtFinalPosition(IncrementalMovement(1, -1, player)),
                ),
            ),
    )

fun getKing(player: Player) =
    Piece(
        type = KING,
        player = player,
        rules =
            CombinedRules(
                PathMovementRules(1 to 1, JumpManager(Int.MAX_VALUE, 0, 1)),
                PathMovementRules(1 to -1, JumpManager(Int.MAX_VALUE, 0, 1)),
                PathMovementRules(-1 to 1, JumpManager(Int.MAX_VALUE, 0, 1)),
                PathMovementRules(-1 to -1, JumpManager(Int.MAX_VALUE, 0, 1)),
            ),
    )

enum class CheckersPieceType : PieceType {
    MAN,
    KING,
}
