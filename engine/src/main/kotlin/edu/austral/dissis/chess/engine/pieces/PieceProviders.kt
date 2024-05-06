package edu.austral.dissis.chess.engine.pieces

import edu.austral.dissis.chess.engine.ClassicMoveType
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.not
import edu.austral.dissis.chess.rules.NoSelfCheckInValidPlays
import edu.austral.dissis.chess.rules.pieces.Combined
import edu.austral.dissis.chess.rules.pieces.FinalPositionContainsPieceOfPlayer
import edu.austral.dissis.chess.rules.pieces.IncrementalMovement
import edu.austral.dissis.chess.rules.pieces.MoveTwoPlaces
import edu.austral.dissis.chess.rules.pieces.NoPieceAtFinalPosition
import edu.austral.dissis.chess.rules.pieces.UpdateMoveState
import edu.austral.dissis.chess.rules.pieces.PathMovementRules
import edu.austral.dissis.chess.rules.pieces.UpdateTwoPlacesState

//TODO: Think how to provide pieces more conveniently

fun getRook(player: Player) =
    Piece(
        type = "rook",
        player = player,
        rules =
        NoSelfCheckInValidPlays(
            player = player,
            subRule =
            UpdateMoveState(
                subRule =
                PathMovementRules(ClassicMoveType.VERTICAL_AND_HORIZONTAL)
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
            PathMovementRules(ClassicMoveType.DIAGONAL)
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
            PathMovementRules(ClassicMoveType.ANY_STRAIGHT_LINE)
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
            //TODO: How to save this to a class? Simply wrapping it in a piece rule?
            Combined(
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

fun getPawn(player: Player) =
    //TODO: Promotion
    Piece(
        "pawn",
        player,
        rules =
        NoSelfCheckInValidPlays(
            player,
            subRule =
            UpdateMoveState(UpdateTwoPlacesState(
                subRule =
                Combined(
                    NoPieceAtFinalPosition(IncrementalMovement(1, 0, player)),
                    FinalPositionContainsPieceOfPlayer(!player, IncrementalMovement(1, 1, player)),
                    FinalPositionContainsPieceOfPlayer(!player, IncrementalMovement(1, -1, player)),
                    //TODO: En Passant
                    MoveTwoPlaces(player)
                )
            ))
        )
    )
