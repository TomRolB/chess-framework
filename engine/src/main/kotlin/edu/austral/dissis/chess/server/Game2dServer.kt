package edu.austral.dissis.chess.server

import com.fasterxml.jackson.core.type.TypeReference
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.server.ServerConfig.ENGINE
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.Server
import edu.austral.ingsis.clientserver.netty.server.NettyServerBuilder

fun main() {
    val engine = ENGINE

    // TODO: should we consider synchronization?
//    val playerMap: MutableMap<String, Player> = Collections.synchronizedMap(emptyMap())
    val playerMap: MutableMap<String, Player> = mutableMapOf()

    val connListener = ServerConnectionListener(playerMap, engine)
    val moveListener = MoveListener(engine, playerMap)
    val server: Server = buildServer(connListener, moveListener)

    connListener.server = server
    moveListener.server = server
    server.start()
}

private fun buildServer(
    connListener: ServerConnectionListener,
    moveListener: MoveListener,
): Server {
    return NettyServerBuilder.createDefault()
        .withPort(8095)
        .withConnectionListener(connListener)
        .addMessageListener(
            messageType = "move",
            messageTypeReference = object : TypeReference<Message<MovePayload>>() {},
            messageListener = moveListener,
        )
        .build()
}
