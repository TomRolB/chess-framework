package edu.austral.dissis.chess.client

import com.fasterxml.jackson.core.type.TypeReference
import edu.austral.dissis.chess.gui.CachedImageResolver
import edu.austral.dissis.chess.gui.DefaultImageResolver
import edu.austral.dissis.chess.gui.GameEventListener
import edu.austral.dissis.chess.gui.GameOver
import edu.austral.dissis.chess.gui.GameView
import edu.austral.dissis.chess.gui.ImageResolver
import edu.austral.dissis.chess.gui.InvalidMove
import edu.austral.dissis.chess.gui.Move
import edu.austral.dissis.chess.gui.NewGameState
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
        const val GAME_TITLE = "Online Chess"
    }

    //TODO: modularize
    //TODO: no messages displaying in any client
    override fun start(primaryStage: Stage) {
        val initialContext = InitialContext() //TODO: may reduce all containers to a Context or sth of the sort

        //TODO: may put all listeners into an object to make the code more readable
        val invalidMoveListener = MoveResultListener<InvalidMove>()
        val newGameStateListener = MoveResultListener<NewGameState>()
        val gameOverListener = MoveResultListener<GameOver>() //TODO: exception when receiving a GameOver result: There's a null pointer at the piece adapter
        val ackListener = AcknowledgeListener(initialContext)
        val client = buildClient(invalidMoveListener, newGameStateListener, gameOverListener, ackListener)

        val root = createGameView(imageResolver, client, initialContext)
        invalidMoveListener.gameView = root
        newGameStateListener.gameView = root
        gameOverListener.gameView = root
        ackListener.gameView = root
        client.connect()

        primaryStage.title = GAME_TITLE
        primaryStage.scene = Scene(root)
        primaryStage.show()
    }

    private fun buildClient(
        invalidMoveListener: MoveResultListener<InvalidMove>,
        newGameStateListener: MoveResultListener<NewGameState>,
        gameOverListener: MoveResultListener<GameOver>,
        ackListener: AcknowledgeListener,
    ): Client {
        return NettyClientBuilder.createDefault()
            .withAddress(InetSocketAddress("localhost", 8095))
            .withConnectionListener(MyClientConnectionListener())
            .addMessageListener(
                messageType = "ack",
                messageTypeReference = object : TypeReference<Message<AckPayload>>() {},
                messageListener = ackListener
            )
            .addMessageListener(
                messageType = "invalid move",
                messageTypeReference = object : TypeReference<Message<InvalidMove>>() {},
                messageListener = invalidMoveListener
            )
            .addMessageListener(
                messageType = "new game state",
                messageTypeReference = object : TypeReference<Message<NewGameState>>() {},
                messageListener = newGameStateListener
            )
            .addMessageListener(
                messageType = "game over",
                messageTypeReference = object : TypeReference<Message<GameOver>>() {},
                messageListener = gameOverListener
            )
            .build()
    }

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
}