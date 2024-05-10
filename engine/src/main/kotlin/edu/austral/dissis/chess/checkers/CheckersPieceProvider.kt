package edu.austral.dissis.chess.checkers

import edu.austral.dissis.chess.checkers.CheckersPieceType.KING
import edu.austral.dissis.chess.checkers.CheckersPieceType.MAN
import edu.austral.dissis.chess.checkers.rules.JumpManager
import edu.austral.dissis.chess.checkers.rules.MovesWhenCompulsory
import edu.austral.dissis.chess.checkers.rules.CanAttackUpdater
import edu.austral.dissis.chess.chess.rules.updaters.PromotionUpdater
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.pieces.PieceType
import edu.austral.dissis.chess.engine.rules.pieces.CombinedRules
import edu.austral.dissis.chess.engine.rules.pieces.IncrementalMovement
import edu.austral.dissis.chess.engine.rules.pieces.NoPieceAtFinalPosition
import edu.austral.dissis.chess.engine.rules.pieces.PathMovementRules
import edu.austral.dissis.chess.engine.rules.pieces.Update

fun getMan(player: Player) =
    Piece(
        type = MAN,
        player = player,
        rules =
//        MovesWhenCompulsory(
//            subRule =
            Update(
                PromotionUpdater(getKing(player)),
                subRule =
//                Update(
//                    CanAttackUpdater(),
//                    subRule =
                    CombinedRules(
                        // TODO: Should mirror inside PathMovementRules
                        PathMovementRules(
                            increments = 1 to 1,
                            JumpManager(2, 1, 1)
                        ),
                        PathMovementRules(
                            increments = 1 to -1,
                            JumpManager(2, 1, 1)
                        ),
                        PathMovementRules(
                            increments = -1 to 1,
                            JumpManager(2, 1, 1)
                        ),
                        PathMovementRules(
                            increments = -1 to -1,
                            JumpManager(2, 1, 1)
                        ),
                        NoPieceAtFinalPosition(
                            subRule =
                            IncrementalMovement(1, 1, player)
                        ),
                        NoPieceAtFinalPosition(
                            subRule =
                            IncrementalMovement(1, -1, player)
                        ),
                    )
//                ),
            ),
//        )
    )

fun getKing(player: Player) =
    Piece(
        type = KING,
        player = player,
        rules =
//        MovesWhenCompulsory(
//            subRule =
//            Update(
//                CanAttackUpdater(),
//                subRule =
                CombinedRules(
                    PathMovementRules(
                        increments = 1 to 1,
                        JumpManager(Int.MAX_VALUE, 0, 1)
                    ),
                    PathMovementRules(
                        increments = 1 to -1,
                        JumpManager(Int.MAX_VALUE, 0, 1)
                    ),
                    PathMovementRules(
                        increments = -1 to 1,
                        JumpManager(Int.MAX_VALUE, 0, 1)
                    ),
                    PathMovementRules(
                        increments = -1 to -1,
                        JumpManager(Int.MAX_VALUE, 0, 1)
                    ),
                )
//            ),
//        )
    )

enum class CheckersPieceType : PieceType {
    MAN,
    KING,
}
