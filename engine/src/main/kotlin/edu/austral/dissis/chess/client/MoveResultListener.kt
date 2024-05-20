package edu.austral.dissis.chess.client

import edu.austral.dissis.chess.gui.GameView
import edu.austral.dissis.chess.gui.MoveResult
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.MessageListener
import javafx.application.Platform

class MoveResultListener<T: MoveResult>(private val gameView: GameView) : MessageListener<T> {
    override fun handleMessage(message: Message<T>) {
        Platform.runLater { gameView.handleMoveResult(message.payload) }
    }
}
