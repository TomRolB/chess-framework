//package edu.austral.dissis.chess.checkers.rules
//
//import edu.austral.dissis.chess.engine.EngineResult
//import edu.austral.dissis.chess.engine.Player
//import edu.austral.dissis.chess.engine.rules.gameflow.preplay.PrePlayValidator
//import edu.austral.dissis.chess.engine.RuleResult
//import edu.austral.dissis.chess.engine.board.GameBoard
//import edu.austral.dissis.chess.engine.board.Position
//
//class CompulsoryJumpsValidator : PrePlayValidator {
//    override fun getResult(
//        board: GameBoard,
//        from: Position,
//        to: Position,
//        player: Player,
//    ): RuleResult {
//        return when {
//            // TODO: could compose these rules. Idea: use IndependentRuleChain.
//            //  Problem: IndependentRuleChain deals with booleans, so it needs no
//            //  constructor arguments for each rule. Here, we do need them.
//            !board.containsPieceOfPlayer(from, player) -> {
//                getViolationResult(board, "This tile does not contain a piece of yours")
//            }
//            violatesPendingJumps(board, from, player) -> {
//                getViolationResult(board, "There's another piece with a pending jump")
//            }
//            violatesCompulsoryJumps(board, from, player) -> {
//                getViolationResult(board, "One of your pieces has a compulsory jump")
//            }
//            (from == to) -> {
//                getViolationResult(board, "Cannot stay in the same place")
//            }
//            else -> null
//        }
//    }
//
//    // TODO: should compose
//    private fun violatesCompulsoryJumps(
//        board: GameBoard,
//        from: Position,
//        player: Player,
//    ): Boolean {
//        val currentPiece = board.getPieceAt(from)!!
//        return !HasAvailableJumps(currentPiece, board, from).verify() &&
//            piecesHaveAvailableJumps(board, player)
//    }
//
//    // TODO: should compose
//    private fun violatesPendingJumps(
//        board: GameBoard,
//        from: Position,
//        player: Player,
//    ): Boolean {
//        return !HasPendingJumps(board, from).verify() &&
//            AnyAllyPieceHasPendingJumps(board, player).verify()
//    }
//
//    // TODO: may convert to Rule
//    private fun piecesHaveAvailableJumps(
//        board: GameBoard,
//        player: Player,
//    ): Boolean {
//        return board
//            .getAllPositionsOfPlayer(player)
//            .any {
//                val piece = board.getPieceAt(it)!!
//                HasAvailableJumps(piece, board, it).verify()
//            }
//    }
//
//    private fun getViolationResult(
//        board: GameBoard,
//        message: String,
//    ): RuleResult {
//        return RuleResult(
//            board,
//            null,
//            EngineResult.GENERAL_MOVE_VIOLATION,
//            message,
//        )
//    }
//}
