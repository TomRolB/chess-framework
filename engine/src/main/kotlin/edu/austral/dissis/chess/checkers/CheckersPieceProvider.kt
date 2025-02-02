package edu.austral.dissis.chess.checkers

import edu.austral.dissis.chess.checkers.AmericanCheckersPieceType.AMERICAN_KING
import edu.austral.dissis.chess.checkers.AmericanCheckersPieceType.AMERICAN_MAN
import edu.austral.dissis.chess.checkers.CheckersPieceType.KING
import edu.austral.dissis.chess.checkers.CheckersPieceType.MAN
import edu.austral.dissis.chess.checkers.rules.JumpManager
import edu.austral.dissis.chess.checkers.rules.JumpsWhenCompulsory
import edu.austral.dissis.chess.checkers.rules.PendingJumpUpdater
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.rules.pieces.CombinedRules
import edu.austral.dissis.chess.engine.rules.pieces.IncrementalMovement
import edu.austral.dissis.chess.engine.rules.pieces.NoPieceAtFinalPosition
import edu.austral.dissis.chess.engine.rules.pieces.path.PathMovementRules
import edu.austral.dissis.chess.engine.rules.pieces.updaters.MapPlay
import edu.austral.dissis.chess.engine.rules.pieces.updaters.MovementMapper
import edu.austral.dissis.chess.engine.rules.pieces.updaters.PromotionUpdater

object CheckersPieceProvider {
    fun getMan(player: Player) =
        Piece(
            type = MAN,
            player = player,
            rules =
                getManRules(player),
        )

    fun getKing(player: Player) =
        Piece(
            type = KING,
            player = player,
            rules =
                getKingRules(),
        )

    fun getAmericanKing(player: Player) =
        Piece(
            type = AMERICAN_KING,
            player = player,
            rules =
                getAmericanKingRules(player),
        )

    fun getAmericanMan(player: Player) =
        Piece(
            type = AMERICAN_MAN,
            player = player,
            rules =
                getAmericanManRules(player),
        )

    private fun getManRules(player: Player): MapPlay {
        return MapPlay(
            MovementMapper(PromotionUpdater(getKing(player))),
            previousRule =
                JumpsWhenCompulsory(
                    previousRule =
                        MapPlay(
                            MovementMapper(PendingJumpUpdater()),
                            previousRule =
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
                                        previousRule =
                                            IncrementalMovement(1, 1, player),
                                    ),
                                    NoPieceAtFinalPosition(
                                        previousRule =
                                            IncrementalMovement(1, -1, player),
                                    ),
                                ),
                        ),
                ),
        )
    }

    private fun getKingRules() =
        MapPlay(
            MovementMapper(PendingJumpUpdater()),
            previousRule =
                JumpsWhenCompulsory(
                    previousRule =
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
        )

    private fun getAmericanManRules(player: Player): MapPlay {
        return MapPlay(
            MovementMapper(PromotionUpdater(getAmericanKing(player))),
            previousRule =
                CombinedRules(
                    NoPieceAtFinalPosition(
                        previousRule =
                            IncrementalMovement(1, 1, player),
                    ),
                    NoPieceAtFinalPosition(
                        previousRule =
                            IncrementalMovement(1, -1, player),
                    ),
                    PathMovementRules(
                        increments = 1 to 1,
                        asPlayer = player,
                        JumpManager(2, 1, 1),
                    ),
                    PathMovementRules(
                        increments = 1 to -1,
                        asPlayer = player,
                        JumpManager(2, 1, 1),
                    ),
                ),
        )
    }

    private fun getAmericanKingRules(player: Player) =
        CombinedRules(
            NoPieceAtFinalPosition(
                previousRule =
                    IncrementalMovement(1, 1, player),
            ),
            NoPieceAtFinalPosition(
                previousRule =
                    IncrementalMovement(1, -1, player),
            ),
            NoPieceAtFinalPosition(
                previousRule =
                    IncrementalMovement(-1, 1, player),
            ),
            NoPieceAtFinalPosition(
                previousRule =
                    IncrementalMovement(-1, -1, player),
            ),
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
        )
}
