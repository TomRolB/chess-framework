package edu.austral.dissis.chess.chess.variants

import edu.austral.dissis.chess.chess.rules.gamerules.ClassicPostPlayValidator
import edu.austral.dissis.chess.chess.rules.gamerules.ClassicWinCondition
import edu.austral.dissis.chess.engine.rules.gameflow.StandardGameRules
import edu.austral.dissis.chess.engine.rules.gameflow.preplay.CannotStayStill
import edu.austral.dissis.chess.engine.rules.gameflow.preplay.CompoundPrePlayValidator
import edu.austral.dissis.chess.engine.rules.gameflow.preplay.PieceOfPlayer

class IncrementalEngine {
    fun getIncrementalChessGameRules() =
        StandardGameRules(
            CompoundPrePlayValidator(
                CannotStayStill(),
                PieceOfPlayer(),
            ),
            ClassicPostPlayValidator(),
            ClassicWinCondition(),
        )
}