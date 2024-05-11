package edu.austral.dissis.chess.checkers

import edu.austral.dissis.chess.checkers.CheckersPieceType.KING
import edu.austral.dissis.chess.checkers.CheckersPieceType.MAN
import edu.austral.dissis.chess.checkers.rules.JumpManager
import edu.austral.dissis.chess.checkers.rules.JumpsWhenCompulsory
import edu.austral.dissis.chess.checkers.rules.PendingJumpUpdater
import edu.austral.dissis.chess.chess.rules.updaters.PromotionUpdater
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.pieces.PieceType
import edu.austral.dissis.chess.engine.rules.pieces.CombinedRules
import edu.austral.dissis.chess.engine.rules.pieces.IncrementalMovement
import edu.austral.dissis.chess.engine.rules.pieces.NoPieceAtFinalPosition
import edu.austral.dissis.chess.engine.rules.pieces.PathMovementRules
import edu.austral.dissis.chess.engine.rules.pieces.Update

//TODO: variant where men can only take forwards (may combine with the three below)
//TODO: variant with no compulsory jumps
//TODO: variant with no flying kings
fun getMan(player: Player) =
    Piece(
        type = MAN,
        player = player,
        rules =
            Update(
                PromotionUpdater(getKing(player)),
                subRule =
                    JumpsWhenCompulsory(
                        subRule =
                            Update(
                                PendingJumpUpdater(),
                                subRule =
                                    CombinedRules(
                                        PathMovementRules(
                                            increments = 1 to 1,
                                            JumpManager(2, 1, 1),
                                        ),
                                        PathMovementRules(
                                            increments = 1 to -1,
                                            JumpManager(2, 1, 1),
                                        ),
                                        PathMovementRules(
                                            increments = -1 to 1,
                                            JumpManager(2, 1, 1),
                                        ),
                                        PathMovementRules(
                                            increments = -1 to -1,
                                            JumpManager(2, 1, 1),
                                        ),
                                        NoPieceAtFinalPosition(
                                            subRule =
                                                IncrementalMovement(1, 1, player),
                                        ),
                                        NoPieceAtFinalPosition(
                                            subRule =
                                                IncrementalMovement(1, -1, player),
                                        ),
                                    ),
                            ),
                    ),
            ),
    )

fun getKing(player: Player) =
    Piece(
        type = KING,
        player = player,
        rules =
            Update(
                PendingJumpUpdater(),
                subRule =
                    JumpsWhenCompulsory(
                        subRule =
                            CombinedRules(
                                PathMovementRules(
                                    increments = 1 to 1,
                                    JumpManager(Int.MAX_VALUE, 0, 1),
                                ),
                                PathMovementRules(
                                    increments = 1 to -1,
                                    JumpManager(Int.MAX_VALUE, 0, 1),
                                ),
                                PathMovementRules(
                                    increments = -1 to 1,
                                    JumpManager(Int.MAX_VALUE, 0, 1),
                                ),
                                PathMovementRules(
                                    increments = -1 to -1,
                                    JumpManager(Int.MAX_VALUE, 0, 1),
                                ),
                            ),
                    ),
            ),
    )

enum class CheckersPieceType : PieceType {
    MAN,
    KING,
}
