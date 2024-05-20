package edu.austral.dissis.chess.server

import edu.austral.dissis.chess.checkers.getCheckersEngine

object ServerConfig {
    val ENGINE = getCheckersEngine()
}
