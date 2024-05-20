package edu.austral.dissis.chess.engine.pieces

import edu.austral.dissis.chess.engine.Play

sealed interface PlayResult

data class InvalidPlay(val message: String) : PlayResult

data class ValidPlay(val play: Play) : PlayResult
