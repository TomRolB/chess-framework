package edu.austral.dissis.chess.server

import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.gui.GameOver
import edu.austral.dissis.chess.gui.InvalidMove
import edu.austral.dissis.chess.gui.MoveResult
import edu.austral.dissis.chess.gui.NewGameState
import edu.austral.dissis.chess.ui.AdapterGameEngine
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.MessageListener
import edu.austral.ingsis.clientserver.Server

class MoveListener(
    private val gameEngine: AdapterGameEngine,
    private val playerMap: Map<String, Player>,
) : MessageListener<MovePayload> {
    lateinit var server: Server

    override fun handleMessage(message: Message<MovePayload>) {
        if (notThisPlayersTurn(message)) return

        val result = gameEngine.applyMove(message.payload.move)
        createResponse(result, message)
    }

    private fun createResponse(
        result: MoveResult,
        message: Message<MovePayload>,
    ) {
        if (result is InvalidMove) {
            returnErrorToSender(message, result)
        } else {
            broadcastBasedOnResultType(result)
        }
    }

    private fun returnErrorToSender(
        message: Message<MovePayload>,
        result: MoveResult,
    ) {
        server.sendMessage(
            clientId = message.payload.clientId,
            message = Message("invalid move", result),
        )
    }

    private fun broadcastBasedOnResultType(result: MoveResult) {
        server.broadcast(
            message =
                when (result) {
                    is GameOver -> Message<GameOver>("game over", result)
                    is InvalidMove -> Message<InvalidMove>("invalid move", result)
                    is NewGameState -> Message<NewGameState>("new game state", result)
                },
        )
    }

    private fun notThisPlayersTurn(message: Message<MovePayload>) =
        gameEngine.game.turnManager.getTurn() != playerMap[message.payload.clientId]
}
