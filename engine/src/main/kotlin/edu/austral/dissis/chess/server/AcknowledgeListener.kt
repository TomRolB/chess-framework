package edu.austral.dissis.chess.server

import edu.austral.dissis.chess.client.InitialContext
import edu.austral.dissis.chess.gui.InitialState
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.MessageListener

// Listener used by the client
class AcknowledgeListener(val initialContext: InitialContext) : MessageListener<AckPayload> {
    override fun handleMessage(message: Message<AckPayload>) {
        initialContext.clientId = message.payload.clientId
        initialContext.initialState = message.payload.initialState
    }
}

data class AckPayload(val clientId: String, var initialState: InitialState)
