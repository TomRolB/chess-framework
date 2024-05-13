package edu.austral.dissis.chess.engine.rules.gameflow.wincondition

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.EngineResult.BLACK_WINS
import edu.austral.dissis.chess.engine.EngineResult.VALID_MOVE
import edu.austral.dissis.chess.engine.EngineResult.WHITE_WINS
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.not

class DeadlockWinCondition : WinCondition {
    override fun getResult(
        board: GameBoard,
        play: Play,
        player: Player,
    ): RuleResult {
        val enemyDeadlock = isEnemyOnDeadlock(board, player)

        return RuleResult(
            board = board,
            play = play,
            engineResult = if (enemyDeadlock) winResult(player) else VALID_MOVE,
            message = if (enemyDeadlock) winMessage(player) else "Valid move",
        )
    }

    private fun winMessage(player: Player) = "${!player} reached a deadlock. $player wins!"

    private fun winResult(player: Player): EngineResult {
        return when (player) {
            Player.BLACK -> BLACK_WINS
            Player.WHITE -> WHITE_WINS
        }
    }

    private fun isEnemyOnDeadlock(
        board: GameBoard,
        player: Player,
    ) = board
        .getAllPositionsOfPlayer(!player)
        .flatMap { board.getPieceAt(it)!!.getValidPlays(board, it) }
        .none()
}
