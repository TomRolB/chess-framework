package edu.austral.dissis.chess.server

import com.fasterxml.jackson.core.type.TypeReference
import edu.austral.dissis.chess.chess.variants.getChessGameRules
import edu.austral.dissis.chess.chess.variants.getClassicChessBoard
import edu.austral.dissis.chess.engine.Game
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.turns.OneToOneTurnManager
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.Server
import edu.austral.ingsis.clientserver.netty.server.NettyServerBuilder

fun main() {
    val responseToSend = ResponseContainer(Awaiting)
    val gameContainer = GameContainer(
        Game(
            getChessGameRules(),
            getClassicChessBoard(),
            OneToOneTurnManager(Player.WHITE)
        )
    )
    val playerMap: MutableMap<String, Player> = mutableMapOf()

    val server: Server = buildServer(playerMap, gameContainer, responseToSend)

    server.start()

    // TODO: the first approach is for the client to have sth like
    //  StandardGameEngine, but without Game inside it. It would simply
    //  receive a RuleResult from the server.

    while (true) {
        when (val response = responseToSend.response) {
            is Broadcast<*> -> {
                server.broadcast(response.message)
                responseToSend.response = Awaiting
            }
            is Unicast<*> -> {
                server.sendMessage(response.clientId, response.message)
                responseToSend.response = Awaiting
            }
            Awaiting -> {
                // Do nothing. Keep waiting for messages.
            }
        }
    }
}

private fun buildServer(
    playerMap: MutableMap<String, Player>,
    gameContainer: GameContainer,
    responseContainer: ResponseContainer,
): Server {
    return NettyServerBuilder.createDefault()
        .withPort(8095)
        .withConnectionListener(ServerConnectionListener(playerMap))
        .addMessageListener(
            messageType = "move",
            messageTypeReference = object : TypeReference<Message<MovePayload>>() {},
            messageListener = MoveListener(gameContainer, responseContainer, playerMap)
        )
        .build()
}

data class ResponseContainer(var response: Response)

data class GameContainer(var game: Game)