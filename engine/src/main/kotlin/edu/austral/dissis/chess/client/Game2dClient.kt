package edu.austral.dissis.chess.client

import com.fasterxml.jackson.core.type.TypeReference
import edu.austral.dissis.chess.gui.CachedImageResolver
import edu.austral.dissis.chess.gui.DefaultImageResolver
import edu.austral.dissis.chess.gui.GameEventListener
import edu.austral.dissis.chess.gui.GameView
import edu.austral.dissis.chess.gui.ImageResolver
import edu.austral.dissis.chess.gui.InitialState
import edu.austral.dissis.chess.gui.Move
import edu.austral.dissis.chess.gui.MoveResult
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
//}

class OnlineChessApplication : Application() {
    private val imageResolver = CachedImageResolver(DefaultImageResolver())

    companion object {
        const val GameTitle = "Online Chess"
    }

    //TODO: modularize
    override fun start(primaryStage: Stage) {
        val initialContext = InitialContext() //TODO: may reduce all containers to a Context or sth of the sort

        val moveResultListener = MoveResultListener()
        val client = buildClient(initialContext, moveResultListener)

        val root = createGameView(imageResolver, client, initialContext)

        moveResultListener.gameView = root
        client.connect();

        primaryStage.title = GameTitle
        primaryStage.scene = Scene(root)
        primaryStage.show()
    }

    private fun buildClient(
        initialContext: InitialContext,
        moveResultListener: MoveResultListener,
    ) = NettyClientBuilder.createDefault()
        .withAddress(InetSocketAddress("localhost", 8095))
        .withConnectionListener(MyClientConnectionListener())
        .addMessageListener(
            messageType = "ack",
            messageTypeReference = object : TypeReference<Message<AckPayload>>() {},
            messageListener = AcknowledgeListener(initialContext)
        )
        .addMessageListener(
            messageType = "move result",
            messageTypeReference = object : TypeReference<Message<MoveResult>>() {},
            messageListener = moveResultListener
        )
        .build()

    private fun createGameView(
        imageResolver: ImageResolver,
        client: Client,
        initialContext: InitialContext
    ): GameView {
        val gameView = GameView(imageResolver)

        val uiOnlyEventListener = object : GameEventListener {
            override fun handleMove(move: Move) {
                // here we should send the move to the server
                client.send(Message("move", MovePayload(initialContext.clientId, move)))
//                gameView.handleMoveResult(result)
            }

            override fun handleUndo() {
                TODO("Yet too implement")
            }

            override fun handleRedo() {
                TODO("Yet too implement")
            }
        }

        gameView.addListener(uiOnlyEventListener)
        gameView.handleInitialState(initialContext.initialState)

        return gameView
    }
}

class InitialContext {
    var clientId = ""
        get() = field.ifEmpty {
            throw IllegalStateException(
                "The clientId has not been assigned yet. The server must first" +
                        " acknowledge the user for it to send any other message."
            )
        }
    
    lateinit var initialState: InitialState 
}