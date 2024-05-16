package edu.austral.dissis.chess.server

import edu.austral.dissis.chess.engine.EngineResult.VALID_MOVE
import edu.austral.dissis.chess.engine.Game
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.MessageListener

class MoveListener(
    private val gameContainer: GameContainer,
    private val responseContainer: ResponseContainer,
    private val playerMap: Map<String, Player?>
    ): MessageListener<MovePayload> {
    override fun handleMessage(message: Message<MovePayload>) {
        val result = gameContainer.game.movePiece(
            from = getFrom(message),
            to = getTo(message)
        )

        createResponse(result, message)
    }

    private fun createResponse(result: Pair<RuleResult, Game>, message: Message<MovePayload>) {
        if (notThisPlayersTurn(message)) return

        if (result.first.engineResult != VALID_MOVE) {
            responseContainer.response = Unicast(
                clientId = message.payload.clientId,
                message = Message("result", result.first.engineResult)
            )
        }
        else {
            gameContainer.game = result.second
            responseContainer.response = Broadcast(
                message = Message("result", result.first.engineResult)
            )
        }
    }

    private fun notThisPlayersTurn(message: Message<MovePayload>) =
        gameContainer.game.turnManager.getTurn() != playerMap[message.payload.clientId]

    private fun getTo(message: Message<MovePayload>) =
        Position(message.payload.to.first, message.payload.to.second)

    private fun getFrom(message: Message<MovePayload>) =
        Position(message.payload.from.first, message.payload.from.second)
}
