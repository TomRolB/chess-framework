package edu.austral.dissis.chess.client

import com.fasterxml.jackson.core.type.TypeReference
import edu.austral.dissis.chess.gui.CachedImageResolver
import edu.austral.dissis.chess.gui.DefaultImageResolver
import edu.austral.dissis.chess.gui.GameEventListener
import edu.austral.dissis.chess.gui.GameOver
import edu.austral.dissis.chess.gui.GameView
import edu.austral.dissis.chess.gui.InvalidMove
import edu.austral.dissis.chess.gui.Move
import edu.austral.dissis.chess.gui.NewGameState
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

class OnlineChessApplication : Application() {
    private val imageResolver = CachedImageResolver(DefaultImageResolver())

    companion object {
        const val GAME_TITLE = "Online Chess"
    }

    override fun start(primaryStage: Stage) {
        val initialContext = InitialContext()

        val gameView = GameView(imageResolver)
        val client = buildClient(gameView, initialContext)
        val root = addListener(client, initialContext, gameView)

        client.connect()

        primaryStage.title = GAME_TITLE
        primaryStage.scene = Scene(root)
        primaryStage.show()
    }

    private fun buildClient(
        gameView: GameView,
        initialContext: InitialContext
    ): Client {
        return NettyClientBuilder.createDefault()
            .withAddress(InetSocketAddress("localhost", 8095))
            .withConnectionListener(MyClientConnectionListener())
            .addMessageListener(
                messageType = "ack",
                messageTypeReference = object : TypeReference<Message<AckPayload>>() {},
                messageListener = AcknowledgeListener(gameView, initialContext)
            )
            .addMessageListener(
                messageType = "invalid move",
                messageTypeReference = object : TypeReference<Message<InvalidMove>>() {},
                messageListener = MoveResultListener<InvalidMove>(gameView)
            )
            .addMessageListener(
                messageType = "new game state",
                messageTypeReference = object : TypeReference<Message<NewGameState>>() {},
                messageListener = MoveResultListener<NewGameState>(gameView)
            )
            .addMessageListener(
                messageType = "game over",
                messageTypeReference = object : TypeReference<Message<GameOver>>() {},
                messageListener = MoveResultListener<GameOver>(gameView)
            )
            .build()
    }

    private fun addListener(
        client: Client,
        initialContext: InitialContext,
        gameView: GameView
    ): GameView {
        val uiOnlyEventListener = object : GameEventListener {
            override fun handleMove(move: Move) {
                client.send(Message("move", MovePayload(initialContext.clientId, move)))
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
