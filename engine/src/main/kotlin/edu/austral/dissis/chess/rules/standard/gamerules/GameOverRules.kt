package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.EngineResult
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.PlayerState
import edu.austral.dissis.chess.engine.GameResult
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.getPlayerState
import edu.austral.dissis.chess.engine.not
import edu.austral.dissis.chess.rules.RuleChain

class GameOverRules(val player: Player) : RuleChain<Pair<Play, ChessBoard>, GameResult> {
    override fun verify(arg: Pair<Play, ChessBoard>): GameResult {
        val (play, board) = arg

        val enemyState = getPlayerState(board, !player)

        val (engineResult, message) =
            when (enemyState) {
                PlayerState.CHECKMATE -> winResult(player)
                PlayerState.STALEMATE -> tieResult(player)
                else -> EngineResult.VALID_MOVE to "Successfully moved"
            }

        return GameResult(board, play, engineResult, message)
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
