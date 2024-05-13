package edu.austral.dissis.chess.chess.pieces

import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getQueen
import edu.austral.dissis.chess.chess.rules.MoveTwoPlaces
import edu.austral.dissis.chess.chess.rules.NoSelfCheckInValidPlays
import edu.austral.dissis.chess.chess.rules.SimpleBlockManager
import edu.austral.dissis.chess.chess.rules.castling.Castling
import edu.austral.dissis.chess.chess.rules.pawn.EnPassant
import edu.austral.dissis.chess.chess.rules.updaters.HasMovedUpdater
import edu.austral.dissis.chess.chess.rules.updaters.PromotionUpdater
import edu.austral.dissis.chess.chess.rules.updaters.TwoPlacesUpdater
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.not
import edu.austral.dissis.chess.engine.rules.pieces.CombinedRules
import edu.austral.dissis.chess.engine.rules.pieces.FinalPosShouldContainPieceOfPlayer
import edu.austral.dissis.chess.engine.rules.pieces.IncrementalMovement
import edu.austral.dissis.chess.engine.rules.pieces.NoPieceAtFinalPosition
import edu.austral.dissis.chess.engine.rules.pieces.PathMovementRules
import edu.austral.dissis.chess.engine.rules.pieces.updaters.MoveInPlayUpdater
import edu.austral.dissis.chess.engine.rules.pieces.updaters.Update

object ChessRulesProvider {
    const val FRIENDLY_FIRE_MESSAGE = "Cannot move over ally piece"

    fun getPawnRules(player: Player) =
        NoSelfCheckInValidPlays(
            player,
            subRule =
                FinalPosShouldContainPieceOfPlayer(
                    player,
                    shouldContain = false,
                    onFailMessage = FRIENDLY_FIRE_MESSAGE,
                    subRule =
                        updatePlay(player),
                ),
        )

    fun getRookRules(player: Player) =
        NoSelfCheckInValidPlays(
            player = player,
            subRule =
                FinalPosShouldContainPieceOfPlayer(
                    player,
                    shouldContain = false,
                    onFailMessage = FRIENDLY_FIRE_MESSAGE,
                    subRule =
                        Update(
                            updater = MoveInPlayUpdater(HasMovedUpdater()),
                            subRule =
                                getRookCoreRules(),
                        ),
                ),
        )

    fun getRookCoreRules() =
        CombinedRules(
            PathMovementRules(
                increments = 0 to 1,
                SimpleBlockManager(limit = Int.MAX_VALUE),
            ),
            PathMovementRules(
                increments = 0 to -1,
                SimpleBlockManager(limit = Int.MAX_VALUE),
            ),
            PathMovementRules(
                increments = 1 to 0,
                SimpleBlockManager(limit = Int.MAX_VALUE),
            ),
            PathMovementRules(
                increments = -1 to 0,
                SimpleBlockManager(limit = Int.MAX_VALUE),
            ),
        )

    fun getBishopRules(player: Player) =
        NoSelfCheckInValidPlays(
            player = player,
            subRule =
                FinalPosShouldContainPieceOfPlayer(
                    player,
                    shouldContain = false,
                    onFailMessage = FRIENDLY_FIRE_MESSAGE,
                    subRule =
                        getBishopCoreRules(),
                ),
        )

    fun getBishopCoreRules() =
        CombinedRules(
            PathMovementRules(
                increments = 1 to 1,
                SimpleBlockManager(limit = Int.MAX_VALUE),
            ),
            PathMovementRules(
                increments = 1 to -1,
                SimpleBlockManager(limit = Int.MAX_VALUE),
            ),
            PathMovementRules(
                increments = -1 to 1,
                SimpleBlockManager(limit = Int.MAX_VALUE),
            ),
            PathMovementRules(
                increments = -1 to -1,
                SimpleBlockManager(limit = Int.MAX_VALUE),
            ),
        )

    fun getKnightRules(player: Player) =
        NoSelfCheckInValidPlays(
            player = player,
            subRule =
                FinalPosShouldContainPieceOfPlayer(
                    player,
                    shouldContain = false,
                    onFailMessage = FRIENDLY_FIRE_MESSAGE,
                    subRule =
                        CombinedRules(
                            IncrementalMovement(rowDelta = 2, colDelta = 1),
                            IncrementalMovement(rowDelta = 1, colDelta = 2),
                            IncrementalMovement(rowDelta = -1, colDelta = 2),
                            IncrementalMovement(rowDelta = -2, colDelta = 1),
                            IncrementalMovement(rowDelta = -2, colDelta = -1),
                            IncrementalMovement(rowDelta = -1, colDelta = -2),
                            IncrementalMovement(rowDelta = 1, colDelta = -2),
                            IncrementalMovement(rowDelta = 2, colDelta = -1),
                        ),
                ),
        )

    const val PAWN_DIAGONAL_MESSAGE = "Can only move diagonally to take an enemy piece"

    fun updatePlay(player: Player) =
        Update(
            updater = MoveInPlayUpdater(PromotionUpdater(getQueen(player))),
            subRule =
                Update(
                    updater = MoveInPlayUpdater(HasMovedUpdater()),
                    subRule =
                        Update(
                            updater = MoveInPlayUpdater(TwoPlacesUpdater()),
                            subRule =
                                getPawnCoreRules(player),
                        ),
                ),
        )

    fun getPawnCoreRules(player: Player) =
        CombinedRules(
            MoveTwoPlaces(player),
            NoPieceAtFinalPosition(
                subRule = IncrementalMovement(1, 0, player),
            ),
            FinalPosShouldContainPieceOfPlayer(
                !player,
                shouldContain = true,
                onFailMessage = PAWN_DIAGONAL_MESSAGE,
                subRule = IncrementalMovement(1, 1, player),
            ),
            FinalPosShouldContainPieceOfPlayer(
                !player,
                shouldContain = true,
                onFailMessage = PAWN_DIAGONAL_MESSAGE,
                subRule = IncrementalMovement(1, -1, player),
            ),
            EnPassant(),
        )

    fun getKingRules(player: Player) =
        NoSelfCheckInValidPlays(
            player,
            subRule =
                FinalPosShouldContainPieceOfPlayer(
                    player,
                    shouldContain = false,
                    onFailMessage = FRIENDLY_FIRE_MESSAGE,
                    subRule =
                        getKingCoreRules(),
                ),
        )

    fun getKingCoreRules() =
        Update(
            updater = MoveInPlayUpdater(HasMovedUpdater()),
            subRule =
                CombinedRules(
                    IncrementalMovement(1, 0),
                    IncrementalMovement(1, 1),
                    IncrementalMovement(0, 1),
                    IncrementalMovement(-1, 1),
                    IncrementalMovement(-1, 0),
                    IncrementalMovement(-1, -1),
                    IncrementalMovement(0, -1),
                    IncrementalMovement(1, -1),
                    Castling(),
                ),
        )
}
