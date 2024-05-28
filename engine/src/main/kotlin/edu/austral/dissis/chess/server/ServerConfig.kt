package edu.austral.dissis.chess.server

import edu.austral.dissis.chess.chess.variants.getChessEngine

object ServerConfig {
    val ENGINE = getChessEngine()
}
