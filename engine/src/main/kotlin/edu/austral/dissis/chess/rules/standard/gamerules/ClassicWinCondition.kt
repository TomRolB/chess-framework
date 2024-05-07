package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.PlayResult
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.PlayerState
import edu.austral.dissis.chess.engine.WinCondition
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.getPlayerState
import edu.austral.dissis.chess.engine.not

class ClassicWinCondition : WinCondition {
    override fun getGameResult(
        board: GameBoard,
        play: Play,
        player: Player,
    ): PlayResult {
        val enemyState = getPlayerState(board, !player)

        val (engineResult, message) =
            when (enemyState) {
                PlayerState.CHECKMATE -> winResult(player)
                PlayerState.STALEMATE -> tieResult(player)
                else -> EngineResult.VALID_MOVE to "Successfully moved"
            }

        val finalResult = PlayResult(board, play, engineResult, message)
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
