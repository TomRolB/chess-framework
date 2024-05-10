package edu.austral.dissis.chess.chess.variants

import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.BISHOP
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.KING
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.KNIGHT
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.PAWN
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.QUEEN
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.ROOK
import edu.austral.dissis.chess.chess.rules.gamerules.ClassicPrePlayValidator
import edu.austral.dissis.chess.engine.Game
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.PostPlayValidator
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.RectangleBoardValidator
import edu.austral.dissis.chess.engine.pieces.PieceType
import edu.austral.dissis.chess.engine.rules.standard.gamerules.StandardGameRules
import edu.austral.dissis.chess.engine.rules.winconditions.ExtinctionWinCondition
import edu.austral.dissis.chess.engine.turns.OneToOneTurnManager
import edu.austral.dissis.chess.ui.StandardGameEngine
import edu.austral.dissis.chess.ui.UiPieceAdapter

fun getExtinctionEngine(): StandardGameEngine {
    val validator = RectangleBoardValidator(8, 8)
    val board = getClassicInitialBoard(validator)

    val pieceAdapter = UiPieceAdapter(getPieceIdMap())

    val gameRules =
        StandardGameRules(
            ClassicPrePlayValidator(),
            NoPostPlayValidator(),
            ExtinctionWinCondition(),
        )

    val game = Game(gameRules, board, OneToOneTurnManager())

    return StandardGameEngine(game, validator, pieceAdapter)
}

private fun getPieceIdMap(): Map<PieceType, String> {
    return listOf(
        PAWN to "pawn",
        ROOK to "rook",
        BISHOP to "bishop",
        KNIGHT to "knight",
        QUEEN to "queen",
        KING to "king",
    ).toMap()
}

class NoPostPlayValidator : PostPlayValidator {
    override fun getPostPlayResult(
        play: Play,
        board: GameBoard,
        player: Player,
    ): edu.austral.dissis.chess.engine.pieces.PlayResult {
        return edu.austral.dissis.chess.engine.pieces.PlayResult(play, "Valid play")
    }
}

