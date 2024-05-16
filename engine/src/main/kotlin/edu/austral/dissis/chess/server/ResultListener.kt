package edu.austral.dissis.chess.server

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.MessageListener

class ResultListener: MessageListener<EngineResult> {
    override fun handleMessage(message: Message<EngineResult>) {

    }
}