package edu.austral.dissis.chess.server

import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.gui.MoveResult
import edu.austral.dissis.chess.gui.NewGameState
import edu.austral.dissis.chess.ui.StandardGameEngine
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.MessageListener

class MoveListener(
    private val gameEngine: StandardGameEngine,
    private val responseContainer: ResponseContainer,
    private val playerMap: Map<String, Player?>
    ): MessageListener<MovePayload> {
    override fun handleMessage(message: Message<MovePayload>) {
        if (notThisPlayersTurn(message)) return

        val result = gameEngine.applyMove(message.payload.move)
        createResponse(result, message)
    }

    private fun createResponse(result: MoveResult, message: Message<MovePayload>) {
        // TODO: may find a way of replacing all this Unicast, Broadcast thing by
        //  directly using the server. The question is HOW to pass the server to
        //  this class, since the server is created after the listener. Maybe by
        //  using a container class, initially empty
        if (result is NewGameState) {
            responseContainer.response = Unicast(
                clientId = message.payload.clientId,
                message = Message("move result", result)
            )
        }
        else {
            responseContainer.response = Broadcast(
                message = Message("move result", result)
            )
        }
    }

//    private fun getMove(message: Message<MovePayload>) =
//        getPosition(message.payload.move.from) to getPosition(message.payload.move.to)
//
//    private fun getPosition(position: UiPosition): Position =
//        Position(position.row, position.column)

    private fun notThisPlayersTurn(message: Message<MovePayload>) =
        gameEngine.game.turnManager.getTurn() != playerMap[message.payload.clientId]
}
