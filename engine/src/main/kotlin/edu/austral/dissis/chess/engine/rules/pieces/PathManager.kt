package edu.austral.dissis.chess.engine.rules.pieces

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position

interface PathManager {
    fun processPosition(
        board: GameBoard,
        from: Position,
        to: Position,
        player: Player,
    ): Pair<PathManager, Play?>

    val isBlocked: Boolean
}
