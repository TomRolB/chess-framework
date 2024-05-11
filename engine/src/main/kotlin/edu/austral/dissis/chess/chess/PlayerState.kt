package edu.austral.dissis.chess.chess

import edu.austral.dissis.chess.chess.rules.king.IsKingChecked
import edu.austral.dissis.chess.chess.rules.king.WillKingBeChecked
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.GameBoard

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
