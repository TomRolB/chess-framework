package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.rules.pieces.king.IsKingChecked
import edu.austral.dissis.chess.rules.pieces.king.WillKingBeChecked

enum class Player {
    BLACK,
    WHITE,
}

enum class PlayerState {
    NORMAL,
    STALEMATE,
    CHECKED,
    CHECKMATE,
}

fun getPlayerState(
    board: GameBoard,
    player: Player,
): PlayerState {
    val isChecked: Int = if (IsKingChecked(board, player).verify()) 1 else 0
    val willBeChecked: Int = if (WillKingBeChecked(board, player).verify()) 1 else 0
    val combinedStatus: Int = isChecked * 2 + willBeChecked

    return PlayerState.entries[combinedStatus]
}

operator fun Player.not(): Player {
    return when (this) {
        Player.BLACK -> Player.WHITE
        Player.WHITE -> Player.BLACK
    }
}
