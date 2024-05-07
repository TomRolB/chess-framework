package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.engine.board.GameBoard

interface WinCondition {
    fun getGameResult(
        board: GameBoard,
        play: Play,
        player: Player,
    ): PlayResult
}
