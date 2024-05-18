package edu.austral.dissis.chess.server

import com.fasterxml.jackson.core.type.TypeReference
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.gui.MoveResult
import edu.austral.dissis.chess.server.ServerConfig.ENGINE
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.Server
import edu.austral.ingsis.clientserver.netty.server.NettyServerBuilder

// TODO: there's a strange behaviour where a movement from a read-only client causes many
//  pieces to disappear from the game board.
//  SOLUTION: This might be happening because all clients are being provided the initial state
//  on connection, so connecting after the game started puts them in an inconsistent state.
//  Nevertheless, read-only clients shouldn't actually be able to perform any move.
fun main() {
    val engine = ENGINE

    //TODO: should we consider synchronization?
//    val playerMap: MutableMap<String, Player> = Collections.synchronizedMap(emptyMap())
    val playerMap: MutableMap<String, Player> = mutableMapOf()
    val resultContainer = ResultContainer(engine.init())

    val connListener = ServerConnectionListener(playerMap, engine)
    val moveListener = MoveListener(engine, playerMap)
    val server: Server = buildServer(connListener, moveListener)

    connListener.server = server
    moveListener.server = server
    server.start()
}

class ResultContainer(result: MoveResult) {

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
            messageListener = moveListener
        )
        .build()
}
