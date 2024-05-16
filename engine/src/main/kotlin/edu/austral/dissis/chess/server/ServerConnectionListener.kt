package edu.austral.dissis.chess.server

import edu.austral.dissis.chess.engine.Player
import edu.austral.ingsis.clientserver.ServerConnectionListener

class ServerConnectionListener(val playerMapper: MutableMap<String, Player>):
    ServerConnectionListener {
    override fun handleClientConnection(clientId: String) {
        assignPlayerOrReadOnly(clientId)

        println("Client $clientId has connected")
    }

    private fun assignPlayerOrReadOnly(clientId: String) {
        if (Player.WHITE in playerMapper.values) playerMapper[clientId] = Player.BLACK
        else playerMapper[clientId] = Player.WHITE
    }

    override fun handleClientConnectionClosed(clientId: String) {
        // TODO: Have to do sth else?
        println("Client $clientId has disconnected")
    }
}