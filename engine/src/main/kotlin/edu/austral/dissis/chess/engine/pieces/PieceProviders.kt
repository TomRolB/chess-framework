package edu.austral.dissis.chess.engine.pieces

import edu.austral.dissis.chess.engine.ClassicMoveType
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.rules.NoSelfCheck
import edu.austral.dissis.chess.rules.pieces.Combined
import edu.austral.dissis.chess.rules.pieces.IncrementalMovement
import edu.austral.dissis.chess.rules.pieces.MovedUpdater
import edu.austral.dissis.chess.rules.pieces.PathMovementRules

//TODO: Think how to provide pieces more conveniently

fun getRook(player: Player) =
    Piece(
        type = "rook",
        player = player,
        rules =
        NoSelfCheck(
            player = player,
            subRule =
            MovedUpdater(
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
        NoSelfCheck(
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
        NoSelfCheck(
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
        NoSelfCheck(
            player = player,
            subRule =
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
