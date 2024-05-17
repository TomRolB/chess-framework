package edu.austral.dissis.chess.client

import edu.austral.dissis.chess.gui.GameView
import edu.austral.dissis.chess.gui.MoveResult
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.MessageListener

class MoveResultListener : MessageListener<MoveResult> {

    lateinit var gameView: GameView

// TODO: all the logic below is actually for sending messages. This class
//  actually receives messages.

//    lateinit var client: Client
//
//    override fun handleMessage(message: Message<Pair<Position, Position>>) {
//        check (::client.isInitialized) {
//            "You must set the client before using this listener"
//        }
//
//        client.send(Message())
//    }
    override fun handleMessage(message: Message<MoveResult>) {
        gameView.handleMoveResult(message.payload)
    }

//    private fun getMove(message: Message<Pair<Position, Position>>) =
//        Move(
//            adaptPosition(message.payload.first),
//            adaptPosition(message.payload.second)
//        )
//
//    private fun adaptPosition(enginePosition: Position): UiPosition =
//        UiPosition(enginePosition.row, enginePosition.col)

}
