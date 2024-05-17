package edu.austral.dissis.chess.server

import edu.austral.dissis.chess.client.InitialContext
import edu.austral.dissis.chess.gui.GameView
import edu.austral.dissis.chess.gui.InitialState
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.MessageListener
import javafx.application.Platform

// Listener used by the client
class AcknowledgeListener(
    val initialContext: InitialContext,
) : MessageListener<AckPayload> {
    lateinit var gameView: GameView

    override fun handleMessage(message: Message<AckPayload>) {
        initialContext.clientId = message.payload.clientId

        Platform.runLater { gameView.handleInitialState(message.payload.initialState) }
    }
}

data class AckPayload(val clientId: String, val initialState: InitialState)
