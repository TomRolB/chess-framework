package edu.austral.dissis.chess.chess.pieces

import edu.austral.dissis.chess.chess.engine.rules.pawn.EnPassant
import edu.austral.dissis.chess.chess.pieces.CapablancaPieceTypes.ARCHBISHOP
import edu.austral.dissis.chess.chess.pieces.CapablancaPieceTypes.CHANCELLOR
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.PAWN
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.ROOK
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.BISHOP
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.QUEEN
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.KNIGHT
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.KING
import edu.austral.dissis.chess.chess.rules.updaters.HasMovedUpdater
import edu.austral.dissis.chess.chess.rules.MoveTwoPlaces
import edu.austral.dissis.chess.chess.rules.NoSelfCheckInValidPlays
import edu.austral.dissis.chess.chess.rules.castling.Castling
import edu.austral.dissis.chess.chess.rules.updaters.PromotionUpdater
import edu.austral.dissis.chess.chess.rules.updaters.TwoPlacesUpdater
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.not
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.rules.pieces.CombinedRules
import edu.austral.dissis.chess.engine.rules.pieces.FinalPositionContainsPieceOfPlayer
import edu.austral.dissis.chess.engine.rules.pieces.IncrementalMovement
import edu.austral.dissis.chess.engine.rules.pieces.NoPieceAtFinalPosition
import edu.austral.dissis.chess.engine.rules.pieces.PathMovementRules
import edu.austral.dissis.chess.chess.rules.SimpleBlockManager
import edu.austral.dissis.chess.engine.rules.pieces.Update

// TODO: Think how to provide pieces more conveniently

// TODO: Many rules are actually general, such as NoSelfCheckInValidPlays.
//  Consider whether this can be unified somewhere, or if it's better to keep it this way
//  (for instance, we may want a piece to not be affected by checks at all)

// TODO: How to save nested rules to a class? Simply wrapping it in a piece rule?
//  Is it really necessary?

// TODO: modularize rules

private const val FRIENDLY_FIRE_MESSAGE = "Cannot move over ally piece"

fun getRook(player: Player) =
    Piece(
        type = ROOK,
        player = player,
        rules =
            getRookRules(player),
    )

private fun getRookRules(player: Player) =
    NoSelfCheckInValidPlays(
        player = player,
        subRule =
            FinalPositionContainsPieceOfPlayer(
                player,
                shouldContain = false,
                onFailMessage = FRIENDLY_FIRE_MESSAGE,
                subRule =
                    Update(
                        updater = HasMovedUpdater(),
                        subRule =
                            CombinedRules(
                                PathMovementRules(0 to 1, SimpleBlockManager(Int.MAX_VALUE)),
                                PathMovementRules(0 to -1, SimpleBlockManager(Int.MAX_VALUE)),
                                PathMovementRules(1 to 0, SimpleBlockManager(Int.MAX_VALUE)),
                                PathMovementRules(-1 to 0, SimpleBlockManager(Int.MAX_VALUE)),
                            ),
                    ),
            ),
    )

fun getBishop(player: Player) =
    Piece(
        type = BISHOP,
        player = player,
        rules =
            getBishopRules(player),
    )

private fun getBishopRules(player: Player) =
    NoSelfCheckInValidPlays(
        player = player,
        subRule =
            FinalPositionContainsPieceOfPlayer(
                player,
                shouldContain = false,
                onFailMessage = FRIENDLY_FIRE_MESSAGE,
                subRule =
                    CombinedRules(
                        PathMovementRules(1 to 1, SimpleBlockManager(Int.MAX_VALUE)),
                        PathMovementRules(1 to -1, SimpleBlockManager(Int.MAX_VALUE)),
                        PathMovementRules(-1 to 1, SimpleBlockManager(Int.MAX_VALUE)),
                        PathMovementRules(-1 to -1, SimpleBlockManager(Int.MAX_VALUE)),
                    ),
            ),
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

private fun getKnightRules(player: Player) =
    NoSelfCheckInValidPlays(
        player = player,
        subRule =
            FinalPositionContainsPieceOfPlayer(
                player,
                shouldContain = false,
                onFailMessage = FRIENDLY_FIRE_MESSAGE,
                subRule =
                    CombinedRules(
                        IncrementalMovement(2, 1),
                        IncrementalMovement(1, 2),
                        IncrementalMovement(-1, 2),
                        IncrementalMovement(-2, 1),
                        IncrementalMovement(-2, -1),
                        IncrementalMovement(-1, -2),
                        IncrementalMovement(1, -2),
                        IncrementalMovement(2, -1),
                    ),
            ),
    )

private const val PAWN_DIAGONAL_MESSAGE = "Can only move diagonally to take an enemy piece"

fun getPawn(player: Player) =
    // TODO: Chaining updates?
    Piece(
        PAWN,
        player,
        rules =
            NoSelfCheckInValidPlays(
                player,
                subRule =
                    FinalPositionContainsPieceOfPlayer(
                        player,
                        shouldContain = false,
                        onFailMessage = FRIENDLY_FIRE_MESSAGE,
                        subRule =
                        getPawnCoreRules(player),
                    ),
            ),
    )

private fun getPawnCoreRules(player: Player) = Update(
    updater = PromotionUpdater(),
    subRule =
    Update(
        updater = HasMovedUpdater(),
        subRule =
        Update(
            updater = TwoPlacesUpdater(),
            subRule =
            CombinedRules(
                NoPieceAtFinalPosition(
                    subRule = IncrementalMovement(1, 0, player),
                ),
                FinalPositionContainsPieceOfPlayer(
                    !player,
                    shouldContain = true,
                    onFailMessage = PAWN_DIAGONAL_MESSAGE,
                    subRule = IncrementalMovement(1, 1, player),
                ),
                FinalPositionContainsPieceOfPlayer(
                    !player,
                    shouldContain = true,
                    onFailMessage = PAWN_DIAGONAL_MESSAGE,
                    subRule = IncrementalMovement(1, -1, player),
                ),
                EnPassant(),
                MoveTwoPlaces(player),
            ),
        ),
    ),
)

fun getKing(player: Player) =
    Piece(
        KING,
        player,
        rules =
            NoSelfCheckInValidPlays(
                player,
                subRule =
                    FinalPositionContainsPieceOfPlayer(
                        player,
                        shouldContain = false,
                        onFailMessage = FRIENDLY_FIRE_MESSAGE,
                        subRule =
                            Update(
                                updater = HasMovedUpdater(),
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
                            ),
                    ),
            ),
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
