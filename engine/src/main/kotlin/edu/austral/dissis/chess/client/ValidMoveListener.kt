package edu.austral.dissis.chess.client

import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.gui.GameEngine
import edu.austral.dissis.chess.gui.Move
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.MessageListener
import edu.austral.dissis.chess.gui.Position as UiPosition

class ValidMoveListener(val engine: GameEngine) : MessageListener<Pair<Position, Position>> {
// TODO: all the lateinit logic is actually for sending messages. This class
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
    override fun handleMessage(message: Message<Pair<Position, Position>>) {
        engine.applyMove(getMove(message))
    }

    private fun getMove(message: Message<Pair<Position, Position>>) =
        Move(
            adaptPosition(message.payload.first),
            adaptPosition(message.payload.second)
        )

    private fun adaptPosition(enginePosition: Position): UiPosition =
        UiPosition(enginePosition.row, enginePosition.col)

}
