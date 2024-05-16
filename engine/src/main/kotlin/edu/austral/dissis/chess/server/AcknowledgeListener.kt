package edu.austral.dissis.chess.server

import edu.austral.dissis.chess.client.IdContainer
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.MessageListener

// Listener used by the client
class AcknowledgeListener(val idContainer: IdContainer) : MessageListener<AckPayload> {
    override fun handleMessage(message: Message<AckPayload>) {
        idContainer.clientId = message.payload.clientId
    }
}

data class AckPayload(val clientId: String)
