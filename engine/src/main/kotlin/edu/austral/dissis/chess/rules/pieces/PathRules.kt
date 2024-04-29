//package edu.austral.dissis.chess.rules.pieces
//
//import edu.austral.dissis.chess.engine.GameBoard
//import edu.austral.dissis.chess.engine.Move
//import edu.austral.dissis.chess.engine.MoveType
//import edu.austral.dissis.chess.engine.MovementData
//import edu.austral.dissis.chess.engine.Piece
//import edu.austral.dissis.chess.engine.Play
//import edu.austral.dissis.chess.engine.PlayResult
//import edu.austral.dissis.chess.engine.Player
//import edu.austral.dissis.chess.engine.Position
//import edu.austral.dissis.chess.engine.RookPieceRules
//import edu.austral.dissis.chess.rules.All
//import edu.austral.dissis.chess.rules.FirstFailOrNull
//import edu.austral.dissis.chess.rules.Rule
//
//class PathRules(
//    val moveType: MoveType,
//    val board: GameBoard,
//    val from: Position,
//    val to: Position,
//    val player: Player,
//): Rule<PlayResult> {
//    override fun verify(): PlayResult {
//        val moveData = MovementData(from, to, board)
//
//        FirstFailOrNull(
//            moveType.isViolated(moveData) to "Piece cannot move this way",
//            moveType.isPathBlocked(moveData, board) to "Cannot move there: the path is blocked"
//        )
//
//        return when {
//            moveType.isViolated(moveData) -> {
//                println("A tower cannot move this way")
//                null
//            }
//            moveType.isPathBlocked(moveData, board) -> {
//                println("Cannot move there: the path is blocked")
//                null
//            }
//            !hasEverMoved -> {
//                val rulesNextTurn = RookPieceRules(player, true)
//                val pieceNextTurn = Piece(player, rulesNextTurn)
//                Move(from, to, board, pieceNextTurn).asPlay()
//            }
//            else -> {
//                Move(from, to, board).asPlay()
//            }
//        }
//    }
//}