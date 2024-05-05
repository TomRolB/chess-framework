package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.engine.board.ChessBoard
import edu.austral.dissis.chess.engine.board.Position
import edu.austral.dissis.chess.engine.pieces.PieceRule
import edu.austral.dissis.chess.engine.pieces.PlayResult
import edu.austral.dissis.chess.rules.pieces.pawn.PawnDiagonal
import edu.austral.dissis.chess.rules.pieces.pawn.PawnFront
import edu.austral.dissis.chess.rules.pieces.pawn.PawnTwoPlaces

class PieceRuleMap : PieceRule {
    val ruleMap: Map<Pair<Int, Int>, PieceRule>
    val player: Player?

    constructor(vararg rules: Pair<Pair<Int, Int>, PieceRule>) {
        this.ruleMap = rules.toMap()
        this.player = null
    }

    private constructor(ruleMap: Map<Pair<Int, Int>, PieceRule>, player: Player) {
        this.ruleMap = ruleMap
        this.player = player
    }

    override fun getValidPlays(board: ChessBoard, position: Position): Iterable<Play> {
        return ruleMap.values.flatMap { it.getValidPlays(board, position) }
    }

    override fun getPlayIfValid(board: ChessBoard, from: Position, to: Position): PlayResult {
        val moveData = mirroredMoveDataIfNeeded(from, to, board)
        val movementDelta = moveData.colDelta to moveData.rowDelta

        return ruleMap[movementDelta]
            ?.getPlayIfValid(board, from, to)
            ?: PlayResult(null, "Piece, cannot move this way")
    }

    private fun PieceRuleMap.mirroredMoveDataIfNeeded(
        from: Position,
        to: Position,
        board: ChessBoard,
    ): MovementData {
        return if (player == null) MovementData(from, to)
        else MovementData(from, to, board, player)
    }

    fun withMirroredMovements(player: Player): PieceRuleMap {
        return PieceRuleMap(ruleMap, player)
    }

}