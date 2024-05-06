package edu.austral.dissis.chess.engine.pieces

import edu.austral.dissis.chess.engine.ClassicPathType
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.not
import edu.austral.dissis.chess.rules.NoSelfCheckInValidPlays
import edu.austral.dissis.chess.rules.castling.Castling
import edu.austral.dissis.chess.rules.pieces.CombinedRules
import edu.austral.dissis.chess.rules.pieces.EnPassant
import edu.austral.dissis.chess.rules.pieces.FinalPositionContainsPieceOfPlayer
import edu.austral.dissis.chess.rules.pieces.HasMovedUpdater
import edu.austral.dissis.chess.rules.pieces.IncrementalMovement
import edu.austral.dissis.chess.rules.pieces.MoveTwoPlaces
import edu.austral.dissis.chess.rules.pieces.NoPieceAtFinalPosition
import edu.austral.dissis.chess.rules.pieces.PathMovementRules
import edu.austral.dissis.chess.rules.pieces.PromotionUpdater
import edu.austral.dissis.chess.rules.pieces.TwoPlacesUpdater
import edu.austral.dissis.chess.rules.pieces.Update

// TODO: Think how to provide pieces more conveniently

// TODO: Many rules are actually general, such as NoSelfCheckInValidPlays.
//  Consider whether this can be unified somewhere, or if it's better to keep it this way
//  (for instance, we may want a piece to not be affected by checks at all)

// TODO: could use let{} to avoid nesting

// TODO: How to save nested rules to a class? Simply wrapping it in a piece rule?
//  Is it really necessary?

// TODO: modularize rules

private const val FRIENDLY_FIRE_MESSAGE = "Cannot move over ally piece"

fun getRook(player: Player) =
    Piece(
        type = "rook",
        player = player,
        rules =
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
                                    PathMovementRules(0 to 1),
                                    PathMovementRules(0 to -1),
                                    PathMovementRules(1 to 0),
                                    PathMovementRules(-1 to 0),
                                )
                            ),
                    ),
            ),
    )

fun getBishop(player: Player) =
    Piece(
        type = "bishop",
        player = player,
        rules =
            NoSelfCheckInValidPlays(
                player = player,
                subRule =
                    FinalPositionContainsPieceOfPlayer(
                        player,
                        shouldContain = false,
                        onFailMessage = FRIENDLY_FIRE_MESSAGE,
                        subRule =
                            CombinedRules(
                                PathMovementRules(0 to 1),
                                PathMovementRules(0 to -1),
                                PathMovementRules(1 to 0),
                                PathMovementRules(-1 to 0),
                            )
                    ),
            ),
    )

fun getQueen(player: Player) =
    Piece(
        type = "queen",
        player = player,
        rules =
            NoSelfCheckInValidPlays(
                player = player,
                subRule =
                    FinalPositionContainsPieceOfPlayer(
                        player,
                        shouldContain = false,
                        onFailMessage = FRIENDLY_FIRE_MESSAGE,
                        subRule =
                            CombinedRules(
                                PathMovementRules(1 to 1),
                                PathMovementRules(1 to -1),
                                PathMovementRules(-1 to 1),
                                PathMovementRules(-1 to -1),
                                PathMovementRules(1 to 1),
                                PathMovementRules(1 to -1),
                                PathMovementRules(-1 to 1),
                                PathMovementRules(-1 to -1),
                            )
                    ),
            ),
    )

fun getKnight(player: Player) =
    Piece(
        "knight",
        player = player,
        rules =
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
            ),
    )

private const val PAWN_DIAGONAL_MESSAGE = "Can only move diagonally to take an enemy piece"

fun getPawn(player: Player) =
// TODO: Promotion
    // TODO: Chaining updates?
    Piece(
        "pawn",
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
                                                            subRule = IncrementalMovement(1, 0, player)
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
                            ),
                    ),
            ),
    )

fun getKing(player: Player) =
    Piece(
        "king",
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
