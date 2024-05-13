package edu.austral.dissis.chess.chess.pieces

import edu.austral.dissis.chess.chess.pieces.CapablancaPieceTypes.ARCHBISHOP
import edu.austral.dissis.chess.chess.pieces.CapablancaPieceTypes.CHANCELLOR
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.BISHOP
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.KING
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.KNIGHT
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.PAWN
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.QUEEN
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.ROOK
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
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.rules.pieces.CombinedRules
import edu.austral.dissis.chess.engine.rules.pieces.FinalPosShouldContainPieceOfPlayer
import edu.austral.dissis.chess.engine.rules.pieces.IncrementalMovement
import edu.austral.dissis.chess.engine.rules.pieces.NoPieceAtFinalPosition
import edu.austral.dissis.chess.engine.rules.pieces.PathMovementRules
import edu.austral.dissis.chess.engine.rules.pieces.updaters.MoveInPlayUpdater
import edu.austral.dissis.chess.engine.rules.pieces.updaters.Update

// TODO: Many rules are actually general, such as NoSelfCheckInValidPlays.
//  Consider whether this can be unified somewhere, or if it's better to keep it this way
//  (for instance, we may want a piece to not be affected by checks at all)

// TODO: modularize rules

private const val FRIENDLY_FIRE_MESSAGE = "Cannot move over ally piece"

object ChessPieceProvider {
    fun getRook(player: Player) =
        Piece(
            type = ROOK,
            player = player,
            rules =
                getRookRules(player),
        )

    fun getBishop(player: Player) =
        Piece(
            type = BISHOP,
            player = player,
            rules =
            getBishopRules(player),
        )

    fun getQueen(player: Player) =
        Piece(
            type = QUEEN,
            player = player,
            rules =
            CombinedRules(
                getRookRules(player),
                getBishopRules(player),
            ),
        )

    fun getKnight(player: Player) =
        Piece(
            KNIGHT,
            player = player,
            rules =
            getKnightRules(player),
        )

    fun getPawn(player: Player) =
        // TODO: Chaining updates?
        Piece(
            PAWN,
            player,
            rules =
            getPawnRules(player),
        )

    fun getKing(player: Player) =
        Piece(
            KING,
            player,
            rules =
            getKingRules(player),
        )

    fun getChancellor(player: Player) =
        Piece(
            CHANCELLOR,
            player,
            rules =
            CombinedRules(
                getRookRules(player),
                getKnightRules(player),
            ),
        )

    fun getArchbishop(player: Player) =
        Piece(
            ARCHBISHOP,
            player,
            rules =
            CombinedRules(
                getBishopRules(player),
                getKnightRules(player),
            ),
        )

    private fun getPawnRules(player: Player) = NoSelfCheckInValidPlays(
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

    private fun getRookRules(player: Player) =
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

    private fun getRookCoreRules() = CombinedRules(
        PathMovementRules(
            increments = 0 to 1,
            SimpleBlockManager(limit = Int.MAX_VALUE)
        ),
        PathMovementRules(
            increments = 0 to -1,
            SimpleBlockManager(limit = Int.MAX_VALUE)
        ),
        PathMovementRules(
            increments = 1 to 0,
            SimpleBlockManager(limit = Int.MAX_VALUE)
        ),
        PathMovementRules(
            increments = -1 to 0,
            SimpleBlockManager(limit = Int.MAX_VALUE)
        ),
    )

    private fun getBishopRules(player: Player) =
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

    private fun getBishopCoreRules() = CombinedRules(
        PathMovementRules(
            increments = 1 to 1,
            SimpleBlockManager(limit = Int.MAX_VALUE)
        ),
        PathMovementRules(
            increments = 1 to -1,
            SimpleBlockManager(limit = Int.MAX_VALUE)
        ),
        PathMovementRules(
            increments = -1 to 1,
            SimpleBlockManager(limit = Int.MAX_VALUE)
        ),
        PathMovementRules(
            increments = -1 to -1,
            SimpleBlockManager(limit = Int.MAX_VALUE)
        ),
    )

    private fun getKnightRules(player: Player) =
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

    private const val PAWN_DIAGONAL_MESSAGE = "Can only move diagonally to take an enemy piece"

    private fun updatePlay(player: Player) =
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

    private fun getPawnCoreRules(player: Player) = CombinedRules(
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

    private fun getKingRules(player: Player) = NoSelfCheckInValidPlays(
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

    private fun getKingCoreRules() = Update(
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
