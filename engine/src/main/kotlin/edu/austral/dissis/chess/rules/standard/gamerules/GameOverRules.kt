package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.GameResult
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.WinCondition
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.rules.RuleChain

class GameOverRules(
    val player: Player,
    val winCondition: WinCondition
) : RuleChain<Pair<Play, ChessBoard>, GameResult> {
    override fun verify(arg: Pair<Play, ChessBoard>): GameResult {
        val (play, board) = arg

        val finalResult = winCondition.getGameResult(board, play, player)
        return finalResult
    }
}
