package edu.austral.dissis.chess.engine

data class Piece(val player: Player, val board: GameBoard, val rules: PieceRules)

interface PieceRules {
    fun isPlayValid(from:String, to: String): Boolean
    fun getValidPlays(): Set<Play>
}

class PathValidator {
    fun isPathClear(player: Player, from: String, to: String) {
        // Checks whether something is blocking the path between a piece and a position.
        // Will probably hold all of the standard paths (for instance, up, down, left, right, each diagonal,etc.), since many are repeated across pieces.
        // TODO: Should this actually be an interface? A singleton class?
    }
}
