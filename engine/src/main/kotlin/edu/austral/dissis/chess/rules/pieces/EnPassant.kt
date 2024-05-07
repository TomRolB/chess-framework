package edu.austral.dissis.chess.rules.pieces

import edu.austral.dissis.chess.engine.Move
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.Take
import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.not
import edu.austral.dissis.chess.engine.pieces.ClassicPieceType.PAWN
import edu.austral.dissis.chess.engine.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult
import edu.austral.dissis.chess.rules.ContainsPieceOfPlayer
import edu.austral.dissis.chess.rules.pieces.pawn.EnemyMovedTwoPlaces

class EnPassant : PieceRule {
    override fun getValidPlays(
        board: ChessBoard,
        position: Position,
    ): Iterable<Play> {
        val rowDelta = getRowDelta(board, position)

        val upAndLeft = Position(position.row + rowDelta, position.col - 1)
        val upAndRight = Position(position.row + rowDelta, position.col + 1)

        return listOfNotNull(
            getPlayResult(board, position, upAndLeft).play,
            getPlayResult(board, position, upAndRight).play,
        )
    }

    private fun getRowDelta(
        board: ChessBoard,
        position: Position,
    ) = if (board.containsPieceOfPlayer(position, Player.WHITE)) 1 else -1

    override fun getPlayResult(
        board: ChessBoard,
        from: Position,
        to: Position,
    ): PlayResult {
        val possibleEnemyPawnPosition = Position(from.row, to.col)
        val player = board.getPieceAt(from)!!.player

        return if (
            areSubRulesValid(board, possibleEnemyPawnPosition, player) &&
            movingDiagonally(player, board, from, to)
        ) {
            PlayResult(
                Play(
                    listOf(
                        Move(from, to, board),
                        Take(possibleEnemyPawnPosition, board),
                    ),
                ),
                "valid play",
            )
        } else {
            PlayResult(null, "Piece cannot move this way")
        }
    }

    private fun areSubRulesValid(
        board: ChessBoard,
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
        board: ChessBoard,
        from: Position,
        to: Position,
    ): Boolean {
        val validMovements =
            CombinedRules(
                IncrementalMovement(1, 1, player),
                IncrementalMovement(1, -1, player),
            )
        return validMovements.getPlayResult(board, from, to).play != null
    }
}
