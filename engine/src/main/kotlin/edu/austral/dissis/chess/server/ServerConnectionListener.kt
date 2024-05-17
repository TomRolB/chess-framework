package edu.austral.dissis.chess.server

import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.gui.GameEngine
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.Server
import edu.austral.ingsis.clientserver.ServerConnectionListener

class ServerConnectionListener(
    private val playerMap: MutableMap<String, Player>,
    private val responseContainer: ResponseContainer,
    private val engine: GameEngine
): ServerConnectionListener {
    lateinit var server: Server

    override fun handleClientConnection(clientId: String) {
        assignPlayerOrReadOnly(clientId)

        println("Client with id $clientId has connected")
    }

    private fun assignPlayerOrReadOnly(clientId: String) {
        if (Player.WHITE in playerMap.values) playerMap[clientId] = Player.BLACK
        else playerMap[clientId] = Player.WHITE
        server.sendMessage(
            clientId,
            Message("ack", AckPayload(clientId, engine.init()))
        )
    }

    override fun handleClientConnectionClosed(clientId: String) {
        // TODO: Have to do sth else?
        println("Client with id $clientId has disconnected")
    }
}