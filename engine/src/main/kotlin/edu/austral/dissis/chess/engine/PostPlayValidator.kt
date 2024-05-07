package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.engine.board.ChessBoard

interface PostPlayValidator {
    fun isStateValid(board: ChessBoard, player: Player): Boolean
}