package edu.austral.dissis.chess.checkers

import edu.austral.dissis.chess.checkers.AmericanCheckersPieceType.AMERICAN_MAN
import edu.austral.dissis.chess.checkers.AmericanCheckersPieceType.AMERICAN_KING
import edu.austral.dissis.chess.checkers.CheckersPieceType.KING
import edu.austral.dissis.chess.checkers.CheckersPieceType.MAN
import edu.austral.dissis.chess.checkers.rules.JumpManager
import edu.austral.dissis.chess.checkers.rules.JumpsWhenCompulsory
import edu.austral.dissis.chess.checkers.rules.PendingJumpUpdater
import edu.austral.dissis.chess.chess.rules.updaters.PromotionUpdater
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.rules.pieces.CombinedRules
import edu.austral.dissis.chess.engine.rules.pieces.IncrementalMovement
import edu.austral.dissis.chess.engine.rules.pieces.NoPieceAtFinalPosition
import edu.austral.dissis.chess.engine.rules.pieces.PathMovementRules
import edu.austral.dissis.chess.engine.rules.pieces.updaters.MoveInPlayUpdater
import edu.austral.dissis.chess.engine.rules.pieces.updaters.Update

// TODO: may combine three below to create AmericanCheckers
// TODO: variant where men can only take forwards
// TODO: variant with no compulsory jumps
// TODO: variant with no flying kings

object CheckersPieceProvider {
    fun getMan(player: Player) =
        Piece(
            type = MAN,
            player = player,
            rules =
            Update(
                MoveInPlayUpdater(PromotionUpdater(getKing(player))),
                subRule =
                JumpsWhenCompulsory(
                    subRule =
                    Update(
                        MoveInPlayUpdater(PendingJumpUpdater()),
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
                MoveInPlayUpdater(PendingJumpUpdater()),
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

    fun getAmericanMan(player: Player) =
        Piece(
            type = AMERICAN_MAN,
            player = player,
            rules =
            Update(
                MoveInPlayUpdater(PromotionUpdater(getAmericanKing(player))),
                subRule =
                CombinedRules(
                    PathMovementRules(
                        increments = 1 to 1,
                        mirroredRowIncrement = true,
                        JumpManager(2, 1, 1),
                    ),
                    PathMovementRules(
                        increments = 1 to -1,
                        mirroredRowIncrement = true,
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
        )

    fun getAmericanKing(player: Player) =
        Piece(
            type = AMERICAN_KING,
            player = player,
            rules =
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
                NoPieceAtFinalPosition(
                    subRule =
                    IncrementalMovement(-1, 1, player),
                ),
                NoPieceAtFinalPosition(
                    subRule =
                    IncrementalMovement(-1, -1, player),
                ),
            ),
        )
}
