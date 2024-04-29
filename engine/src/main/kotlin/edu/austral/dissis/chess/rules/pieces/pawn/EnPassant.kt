package edu.austral.dissis.chess.rules.pieces.pawn

import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.MovementData
import edu.austral.dissis.chess.engine.pieces.Pawn
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.Take
import edu.austral.dissis.chess.rules.ContainsPieceOfPlayer
import edu.austral.dissis.chess.rules.Rule
import edu.austral.dissis.chess.rules.pieces.IsPieceOfType

class EnPassant(
    val board: ChessBoard,
    val moveData: MovementData,
    val enemyPlayer: Player,
) : Rule<Play?> {
    val possibleEnemyPawnPosition = Position(moveData.fromRow, moveData.toCol)

    override fun verify(): Play? {
        val subRules =
            ContainsPieceOfPlayer(
                board,
                possibleEnemyPawnPosition,
                enemyPlayer,
                next =
                    IsPieceOfType(
                        Pawn::class.java,
                        next = EnemyMovedTwoPlaces(),
                    ),
            )

        return Play(
            listOf(
                Move(moveData.from, moveData.to, board),
                Take(possibleEnemyPawnPosition, board),
            ),
        ).takeIf { subRules.verify(null) }
    }
}
