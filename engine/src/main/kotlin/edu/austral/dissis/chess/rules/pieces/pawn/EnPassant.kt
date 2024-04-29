package edu.austral.dissis.chess.rules.pieces.pawn

import edu.austral.dissis.chess.engine.GameBoard
import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.PawnPieceRules
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.Position
import edu.austral.dissis.chess.engine.Take
import edu.austral.dissis.chess.rules.ContainsPieceOfPlayer
import edu.austral.dissis.chess.rules.Rule
import edu.austral.dissis.chess.rules.pieces.IsPieceOfType

class EnPassant(
    val board: GameBoard,
    val moveData: MovementData,
    val enemyPlayer: Player
): Rule<Play?> {
    val possibleEnemyPawnPosition = Position(moveData.fromRow, moveData.toCol)

    override fun verify(): Play? {
        val subRules =
            ContainsPieceOfPlayer(
                board,
                possibleEnemyPawnPosition,
                enemyPlayer,
                next = IsPieceOfType(
                    PawnPieceRules::class.java,
                    next = MovedTwoPlaces()
                )
            )

        return Play(
            listOf(
                Move(moveData.from, moveData.to, board),
                Take(possibleEnemyPawnPosition, board),
            ),
        ).takeIf { subRules.verify(null) }
    }
}