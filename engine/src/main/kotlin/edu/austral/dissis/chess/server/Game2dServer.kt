package edu.austral.dissis.chess.server

import com.fasterxml.jackson.core.type.TypeReference
import edu.austral.dissis.chess.checkers.getCheckersEngine
import edu.austral.dissis.chess.chess.variants.getChessEngine
import edu.austral.dissis.chess.engine.Player
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.Server
import edu.austral.ingsis.clientserver.netty.server.NettyServerBuilder

//TODO: rename file

// TODO: there's a strange behaviour where a movement from a read-only client causes many
//  pieces to disappear from the game board.
//  SOLUTION: This might be happening because all clients are being provided the initial state
//  on connection, so connecting after the game started puts them in an inconsistent state.
//  Nevertheless, read-only clients shouldn't actually be able to perform any move.
fun main() {
    val engine = getCheckersEngine()

    //TODO: should we consider synchronization?
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
            messageListener = moveListener
        )
        .build()
}
