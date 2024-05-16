package edu.austral.dissis.chess.client

import edu.austral.ingsis.clientserver.ClientConnectionListener

class MyClientConnectionListener : ClientConnectionListener {
    override fun handleConnection() {
        println("Client connected to server")
    }

    override fun handleConnectionClosed() {
        println("Client disconnected from server")
    }
}
