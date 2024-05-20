package edu.austral.dissis.chess.chess.rules.pawn

import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.PAWN
import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.Take
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.not
import edu.austral.dissis.chess.engine.pieces.InvalidPlay
import edu.austral.dissis.chess.engine.pieces.PlayResult
import edu.austral.dissis.chess.engine.pieces.ValidPlay
import edu.austral.dissis.chess.engine.rules.ContainsPieceOfPlayer
import edu.austral.dissis.chess.engine.rules.IsPieceOfType
import edu.austral.dissis.chess.engine.rules.pieces.CombinedRules
import edu.austral.dissis.chess.engine.rules.pieces.IncrementalMovement
import edu.austral.dissis.chess.engine.rules.pieces.PieceRule

class EnPassant : PieceRule {
    override fun getValidPlays(
        board: GameBoard,
        position: Position,
    ): Iterable<Play> {
        val rowDelta = getRowDelta(board, position)

        val upAndLeft = Position(position.row + rowDelta, position.col - 1)
        val upAndRight = Position(position.row + rowDelta, position.col + 1)

        return listOfNotNull(
            getPlayResult(board, position, upAndLeft),
            getPlayResult(board, position, upAndRight),
        )
            .filterIsInstance<ValidPlay>()
            .map { it.play }
    }

    private fun getRowDelta(
        board: GameBoard,
        position: Position,
    ) = if (board.containsPieceOfPlayer(position, Player.WHITE)) 1 else -1

    override fun getPlayResult(
        board: GameBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val possibleEnemyPawnPosition = Position(from.row, to.col)
        val player = board.getPieceAt(from)!!.player

        return if (
            areSubRulesValid(board, possibleEnemyPawnPosition, player) &&
            movingDiagonally(player, board, from, to)
        ) {
            ValidPlay(
                Play(
                    listOf(
                        Move(from, to, board),
                        Take(possibleEnemyPawnPosition, board),
                    ),
                ),
            )
        } else {
            InvalidPlay("Piece cannot move this way")
        }
    }

    private fun areSubRulesValid(
        board: GameBoard,
        possibleEnemyPawnPosition: Position,
        player: Player,
    ): Boolean {
        val subRules =
            ContainsPieceOfPlayer(
                board,
                possibleEnemyPawnPosition,
                !player,
                next =
                    IsPieceOfType(
                        pieceType = PAWN,
                        next = EnemyMovedTwoPlaces(),
                    ),
            )
        return subRules.verify()
    }

    private fun movingDiagonally(
        player: Player,
        board: GameBoard,
        from: Position,
        to: Position,
    ): Boolean {
        val validMovements =
            CombinedRules(
                IncrementalMovement(1, 1, player),
                IncrementalMovement(1, -1, player),
            )
        return validMovements.getPlayResult(board, from, to) is ValidPlay
    }
}
