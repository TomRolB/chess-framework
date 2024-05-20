package edu.austral.dissis.chess.server

import edu.austral.dissis.chess.gui.Move

data class MovePayload(val clientId: String, val move: Move)
