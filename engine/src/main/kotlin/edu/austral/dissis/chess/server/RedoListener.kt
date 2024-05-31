package edu.austral.dissis.chess.server

import edu.austral.dissis.chess.gui.NewGameState
import edu.austral.dissis.chess.ui.AdapterGameEngine
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.MessageListener
import edu.austral.ingsis.clientserver.Server

class RedoListener(
    private val gameEngine: AdapterGameEngine,
) : MessageListener<String> {
    lateinit var server: Server

    override fun handleMessage(message: Message<String>) {
        val result: NewGameState = gameEngine.redo()
        server.broadcast(Message("new game state", result))
    }
}
