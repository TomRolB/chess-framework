package edu.austral.dissis.chess.server

import edu.austral.dissis.chess.engine.EngineResult.VALID_MOVE
import edu.austral.dissis.chess.engine.Game
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.MessageListener
import edu.austral.dissis.chess.gui.Position as UiPosition

class MoveListener(
    private val gameContainer: GameContainer,
    private val responseContainer: ResponseContainer,
    private val playerMap: Map<String, Player?>
    ): MessageListener<MovePayload> {
    override fun handleMessage(message: Message<MovePayload>) {
        val result = gameContainer.game.movePiece(
            from = getPosition(message.payload.move.from),
            to = getPosition(message.payload.move.to)
        )

        createResponse(result, message)
    }

    private fun createResponse(result: Pair<RuleResult, Game>, message: Message<MovePayload>) {
        if (notThisPlayersTurn(message)) return

        if (result.first.engineResult != VALID_MOVE) {
            responseContainer.response = Unicast(
                clientId = message.payload.clientId,
                message = Message("invalid result", result.first.engineResult)
            )
        }
        else {
            gameContainer.game = result.second
            responseContainer.response = Broadcast(
                //TODO: probably need to change the way the payload is sent
                message = Message("valid result", getMove(message))
            )
        }
    }

    private fun getMove(message: Message<MovePayload>) =
        getPosition(message.payload.move.from) to getPosition(message.payload.move.to)

    private fun getPosition(position: UiPosition): Position =
        Position(position.row, position.column)

    private fun notThisPlayersTurn(message: Message<MovePayload>) =
        gameContainer.game.turnManager.getTurn() != playerMap[message.payload.clientId]
}
