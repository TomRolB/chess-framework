package edu.austral.dissis.chess.chess.rules.gamerules

import edu.austral.dissis.chess.chess.PlayerState
import edu.austral.dissis.chess.chess.getPlayerState
import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.RuleResult
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.not
import edu.austral.dissis.chess.engine.rules.gameflow.wincondition.WinCondition

class ClassicWinCondition : WinCondition {
    override fun getResult(
        board: GameBoard,
        play: Play,
        player: Player,
    ): RuleResult {
        val enemyState = getPlayerState(board, !player)

        val (engineResult, message) =
            when (enemyState) {
                PlayerState.CHECKMATE -> winResult(player)
                PlayerState.STALEMATE -> tieResult(player)
                else -> EngineResult.VALID_MOVE to "Successfully moved"
            }

        val finalResult = RuleResult(board, play, engineResult, message)
        return finalResult
    }

    private fun tieResult(player: Player): Pair<EngineResult, String> {
        return when (player) {
            Player.WHITE -> EngineResult.TIE_BY_WHITE to "White caused a tie"
            Player.BLACK -> EngineResult.TIE_BY_BLACK to "Black caused a tie"
        }
    }

    private fun winResult(player: Player): Pair<EngineResult, String> {
        return when (player) {
            Player.WHITE -> EngineResult.WHITE_WINS to "White wins!"
            Player.BLACK -> EngineResult.BLACK_WINS to "Black wins!"
        }
    }
}
