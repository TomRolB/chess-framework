package edu.austral.dissis.chess.server

import edu.austral.dissis.chess.client.AckPayload
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.Player.BLACK
import edu.austral.dissis.chess.engine.Player.WHITE
import edu.austral.dissis.chess.gui.GameEngine
import edu.austral.dissis.chess.gui.GameOver
import edu.austral.dissis.chess.gui.PlayerColor
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.Server
import edu.austral.ingsis.clientserver.ServerConnectionListener

class ServerConnectionListener(
    private val playerMap: MutableMap<String, Player>,
    private val engine: GameEngine,
) : ServerConnectionListener {
    lateinit var server: Server

    override fun handleClientConnection(clientId: String) {
        assignPlayerOrReadOnly(clientId)
        println("Client with id $clientId has connected")
    }

    private fun assignPlayerOrReadOnly(clientId: String) {
        if (WHITE !in playerMap.values) {
            playerMap[clientId] = WHITE
        } else if (BLACK !in playerMap.values) {
            playerMap[clientId] = BLACK
        }
        // else no player is assigned, since it would be a read-only client

        server.sendMessage(
            clientId,
            Message("ack", AckPayload(clientId, engine.init())),
        )
    }

    override fun handleClientConnectionClosed(clientId: String) {
        println("Client with id $clientId has disconnected")
        playerLosesIfDisconnected(clientId)
    }

    private fun playerLosesIfDisconnected(clientId: String) {
        when (playerMap[clientId]) {
            BLACK -> server.broadcast(Message("game over", GameOver(PlayerColor.WHITE)))
            WHITE -> server.broadcast(Message("game over", GameOver(PlayerColor.BLACK)))
            null -> {
                // It is a read-only connection. Do nothing.
            }
        }
    }
}
