package edu.austral.dissis.chess.rules.standard.gamerules

import edu.austral.dissis.chess.engine.PlayResult
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.WinCondition
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.rules.RuleChain

class GameOverRules(
    val player: Player,
    val winCondition: WinCondition
) : RuleChain<Pair<Play, ChessBoard>, PlayResult> {
    override fun verify(arg: Pair<Play, ChessBoard>): PlayResult {
        val (play, board) = arg

        val finalResult = winCondition.getGameResult(board, play, player)
        return finalResult
    }
}
