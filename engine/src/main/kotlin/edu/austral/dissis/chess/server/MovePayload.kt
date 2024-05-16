package edu.austral.dissis.chess.server

import edu.austral.dissis.chess.gui.Move

// TODO: For now, we'll use an int tuple to keep it simple,
//  since I don't know if it has much sense to use engine's
//  Position, but I guess this should change
data class MovePayload(val clientId: String, val move: Move)
