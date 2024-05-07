package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.engine.board.ChessBoard

interface WinCondition {
    fun getGameResult(
        board: ChessBoard,
        play: Play,
        player: Player
    ): GameResult
}