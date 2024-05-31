package edu.austral.dissis.chess.server

import com.fasterxml.jackson.core.type.TypeReference
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.server.ServerConfig.ENGINE
import edu.austral.dissis.chess.ui.AdapterGameEngine
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.Server
import edu.austral.ingsis.clientserver.netty.server.NettyServerBuilder

fun main() {
    val engine = ENGINE

    val playerMap: MutableMap<String, Player> = mutableMapOf()
    val server: Server = buildServer(engine, playerMap)

    server.start()
}

private fun buildServer(
    engine: AdapterGameEngine,
    playerMap: MutableMap<String, Player>,
): Server {
    val connListener = ServerConnectionListener(playerMap, engine)
    val moveListener = MoveListener(engine, playerMap)
    val undoListener = UndoListener(engine)
    val redoListener = RedoListener(engine)

    val server =
        NettyServerBuilder.createDefault()
            .withPort(port = 8095)
            .withConnectionListener(connListener)
            .addMessageListener(
                messageType = "move",
                messageTypeReference = object : TypeReference<Message<MovePayload>>() {},
                messageListener = moveListener,
            )
            .addMessageListener(
                messageType = "undo",
                messageTypeReference = object : TypeReference<Message<String>>() {},
                messageListener = undoListener,
            )
            .addMessageListener(
                messageType = "redo",
                messageTypeReference = object : TypeReference<Message<String>>() {},
                messageListener = redoListener,
            )
            .build()

    connListener.server = server
    moveListener.server = server
    undoListener.server = server
    redoListener.server = server

    return server
}
