package edu.austral.dissis.chess.engine.pieces

import edu.austral.dissis.chess.engine.ClassicMoveType
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.rules.NoSelfCheck
import edu.austral.dissis.chess.rules.pieces.MovedUpdater
import edu.austral.dissis.chess.rules.pieces.PathMovementRules

//TODO: Think how to provide pieces more conveniently

fun getRook(player: Player) =
    Piece(
        player = player,
        type =
//        NoSelfCheck(
//            player = player,
//            subRule =
            MovedUpdater(
                subRule =
                PathMovementRules(ClassicMoveType.VERTICAL_AND_HORIZONTAL)
            )
//        )
    )

fun getBishop(player: Player) =
    Piece(
        player = player,
        type =
        NoSelfCheck(
            player = player,
            subRule =
            PathMovementRules(ClassicMoveType.DIAGONAL)
        )
    )