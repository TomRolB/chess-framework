package edu.austral.dissis.chess.rules.pieces

import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.board.ChessBoard

interface PlayUpdater {
    fun update(play: Play, board: ChessBoard): Play
}

