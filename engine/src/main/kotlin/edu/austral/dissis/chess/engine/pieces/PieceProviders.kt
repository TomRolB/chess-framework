package edu.austral.dissis.chess.engine.pieces

import edu.austral.dissis.chess.engine.ClassicMoveType
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.not
import edu.austral.dissis.chess.rules.NoSelfCheckInValidPlays
import edu.austral.dissis.chess.rules.castling.Castling
import edu.austral.dissis.chess.rules.pieces.CombinedRules
import edu.austral.dissis.chess.rules.pieces.FinalPositionContainsPieceOfPlayer
import edu.austral.dissis.chess.rules.pieces.HasMovedUpdater
import edu.austral.dissis.chess.rules.pieces.IncrementalMovement
import edu.austral.dissis.chess.rules.pieces.MoveTwoPlaces
import edu.austral.dissis.chess.rules.pieces.NoPieceAtFinalPosition
import edu.austral.dissis.chess.rules.pieces.PathMovementRules
import edu.austral.dissis.chess.rules.pieces.TwoPlacesUpdater
import edu.austral.dissis.chess.rules.pieces.Update

//TODO: Think how to provide pieces more conveniently
//TODO: Many rules are actually general, such as NoSelfCheckInValidPlays.
// Consider whether this can be unified somewhere, or if it's better to keep it this way
// (for instance, we may want a piece to not be affected by checks at all)

//TODO: could use let{} to avoid nesting

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
                subRule =
                Update(
                    updater = HasMovedUpdater(),
                    subRule =
                    PathMovementRules(ClassicMoveType.VERTICAL_AND_HORIZONTAL)
                )
            )
        )
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
                subRule =
                PathMovementRules(ClassicMoveType.DIAGONAL)
            )
        )
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
                subRule =
                PathMovementRules(ClassicMoveType.ANY_STRAIGHT_LINE)
            )
        )
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
                subRule =
                //TODO: How to save this to a class? Simply wrapping it in a piece rule?
                CombinedRules(
                    IncrementalMovement(2, 1),
                    IncrementalMovement(1, 2),
                    IncrementalMovement(-1, 2),
                    IncrementalMovement(-2, 1),
                    IncrementalMovement(-2, -1),
                    IncrementalMovement(-1, -2),
                    IncrementalMovement(1, -2),
                    IncrementalMovement(2, -1),
                )
            )
        )
    )

fun getPawn(player: Player) =
    //TODO: Promotion
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
                subRule =
                Update(
                    updater = HasMovedUpdater(),
                    subRule =
                    Update(
                        updater = TwoPlacesUpdater(),
                        subRule =
                        CombinedRules(
                            NoPieceAtFinalPosition(IncrementalMovement(1, 0, player)),
                            FinalPositionContainsPieceOfPlayer(!player, true, IncrementalMovement(1, 1, player)),
                            FinalPositionContainsPieceOfPlayer(!player, true, IncrementalMovement(1, -1, player)),
                            //TODO: En Passant
                            MoveTwoPlaces(player)
                        )
                    )
                )
            )
        )
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
                        Castling()
                    )
                )
            )
        )
    )
