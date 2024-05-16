package edu.austral.dissis.chess.client

import com.fasterxml.jackson.core.type.TypeReference
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.gui.CachedImageResolver
import edu.austral.dissis.chess.gui.DefaultImageResolver
import edu.austral.dissis.chess.gui.GameEngine
import edu.austral.dissis.chess.gui.GameEventListener
import edu.austral.dissis.chess.gui.GameView
import edu.austral.dissis.chess.gui.ImageResolver
import edu.austral.dissis.chess.gui.Move
import edu.austral.dissis.chess.server.AckPayload
import edu.austral.dissis.chess.server.AcknowledgeListener
import edu.austral.dissis.chess.server.MovePayload
import edu.austral.ingsis.clientserver.Client
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.netty.client.NettyClientBuilder
import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Scene
import javafx.stage.Stage
import java.net.InetSocketAddress

fun main() {
    launch(OnlineChessApplication::class.java)
}

//    while (true) {
//        if (messageToBeSent.message != null)
//            client.send(messageToBeSent.message)
//    }
//
//    client.send(new Message<>("hello", new HelloPayload("Server")));
}

class OnlineChessApplication : Application() {
    val engine : GameEngine //TODO: the 'engine' should not contain an engine per se
    val idContainer = IdContainer() //TODO: may reduce all containers to a Context or sth of the sort

    val client = NettyClientBuilder.Companion.createDefault()
        .withAddress(InetSocketAddress("localhost", 8095))
        .withConnectionListener(MyClientConnectionListener())
        .addMessageListener(
            messageType = "ack",
            messageTypeReference = object : TypeReference<Message<AckPayload>>() {},
            messageListener = AcknowledgeListener(idContainer)
        )
        .addMessageListener(
            "valid move",
            messageTypeReference = object : TypeReference<Message<Pair<Position, Position>>>() {},
            ValidMoveListener(engine))
        .build()

    client.connect();

    private val imageResolver = CachedImageResolver(DefaultImageResolver())

    companion object {
        const val GameTitle = "Online Chess"
    }

    override fun start(primaryStage: Stage) {
        primaryStage.title = GameTitle

        val root = createGameView(imageResolver, client)

        primaryStage.scene = Scene(root)

        primaryStage.show()
    }

    private fun createGameView(
        imageResolver: ImageResolver,
        client: Client,
        idContainer: IdContainer
    ): GameView {
        val gameView = GameView(imageResolver)

        val uiOnlyEventListener = object : GameEventListener {
            override fun handleMove(move: Move) {
                // here we should send the move to the server
                client.send(Message("move", MovePayload(idContainer.clientId, move)))
            }

            override fun handleUndo() {
                TODO("Yet too implement")
            }

            override fun handleRedo() {
                TODO("Yet too implement")
            }
        }

        gameView.addListener(uiOnlyEventListener)
        gameView.handleInitialState(gameEngine.init())

        return gameView
    }
}

class IdContainer {
    var clientId = ""
        get() = field.ifEmpty {
            throw IllegalStateException(
                "The clientId was not yet assigned. The server must first" +
                        " acknowledge the user for it to send any other message."
            )
        }
}