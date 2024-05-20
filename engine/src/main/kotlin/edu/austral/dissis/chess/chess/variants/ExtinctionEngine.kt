package edu.austral.dissis.chess.chess.variants

import edu.austral.dissis.chess.engine.Game
import edu.austral.dissis.chess.engine.Player.WHITE
import edu.austral.dissis.chess.engine.board.RectangularBoardValidator
import edu.austral.dissis.chess.engine.rules.gameflow.StandardGameRules
import edu.austral.dissis.chess.engine.rules.gameflow.postplay.NoPostPlayValidator
import edu.austral.dissis.chess.engine.rules.gameflow.preplay.CannotStayStill
import edu.austral.dissis.chess.engine.rules.gameflow.preplay.CompoundPrePlayValidator
import edu.austral.dissis.chess.engine.rules.gameflow.preplay.PieceOfPlayer
import edu.austral.dissis.chess.engine.rules.gameflow.wincondition.ExtinctionWinCondition
import edu.austral.dissis.chess.engine.turns.OneToOneTurnManager
import edu.austral.dissis.chess.ui.GameEngineImpl
import edu.austral.dissis.chess.ui.UiPieceAdapter

fun getExtinctionEngine(): GameEngineImpl {
    val board = getClassicChessBoard()

    val pieceAdapter = UiPieceAdapter(getPieceIdMap())

    val gameRules =
        StandardGameRules(
            CompoundPrePlayValidator(
                CannotStayStill(),
                PieceOfPlayer(),
            ),
            NoPostPlayValidator(),
            ExtinctionWinCondition(),
        )

    val game = Game(gameRules, board, OneToOneTurnManager(WHITE))

    return GameEngineImpl(game, board.validator as RectangularBoardValidator, pieceAdapter)
}
