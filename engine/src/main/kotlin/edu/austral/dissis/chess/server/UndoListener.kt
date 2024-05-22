package edu.austral.dissis.chess.server

import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.gui.NewGameState
import edu.austral.dissis.chess.ui.GameEngineImpl
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.MessageListener
import edu.austral.ingsis.clientserver.Server

class UndoListener(
    private val gameEngine: GameEngineImpl,
    private val playerMap: Map<String, Player>,
) : MessageListener<String> {
    lateinit var server: Server

    override fun handleMessage(message: Message<String>) {
        if (isReadOnly(message)) return

        val result: NewGameState = gameEngine.undo()
        server.broadcast(Message("new game state", result))
    }

    private fun isReadOnly(message: Message<String>) =
        !playerMap.containsKey(message.payload)
}
