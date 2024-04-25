//package edu.austral.dissis.chess.adapter
//
//import edu.austral.dissis.chess.engine.*
//import edu.austral.dissis.chess.engine.EngineResult.*
//import edu.austral.dissis.chess.gui.*
//import edu.austral.dissis.chess.gui.Move
//import edu.austral.dissis.chess.gui.PlayerColor.BLACK
//import edu.austral.dissis.chess.gui.PlayerColor.WHITE
//
//class MyGameEngine(val game: Game) : GameEngine {
//    val currentPlayer = WHITE
//
//    private var pieces = listOf(
//        ChessPiece("a1", WHITE, Position(1, 1), "rook_white"),
//        ChessPiece("b1", WHITE, Position(1, 2), "knight_white"),
//        ChessPiece("c1", WHITE, Position(1, 3), "bishop_white"),
//        ChessPiece("d1", WHITE, Position(1, 4), "queen_white"),
//        ChessPiece("e1", WHITE, Position(1, 5), "king_white"),
//        ChessPiece("f1", WHITE, Position(1, 6), "bishop_white"),
//        ChessPiece("g1", WHITE, Position(1, 7), "knight_white"),
//        ChessPiece("h1", WHITE, Position(1, 8), "rook_white"),
//
//        ChessPiece("a8", BLACK, Position(8, 1), "rook_black"),
//        ChessPiece("b8", BLACK, Position(8, 2), "knight_black"),
//        ChessPiece("c8", BLACK, Position(8, 3), "bishop_black"),
//        ChessPiece("d8", BLACK, Position(8, 4), "queen_black"),
//        ChessPiece("e8", BLACK, Position(8, 5), "king_black"),
//        ChessPiece("f8", BLACK, Position(8, 6), "bishop_black"),
//        ChessPiece("g8", BLACK, Position(8, 7), "knight_black"),
//        ChessPiece("h8", BLACK, Position(8, 8), "rook_black"),
//    ) + "abcdefgh"
//        .toList()
//        .map {
//            ChessPiece(it + "2", WHITE, Position(2, it.charToCol()), "pawn_white")
//        } + "abcdefgh"
//            .toList()
//            .map { ChessPiece(it + "7", BLACK, Position(7, it.charToCol()), "pawn_black") }
//
//    override fun applyMove(move: Move): MoveResult {
//        val stringifiedFrom = getStringPosition(move.from.row, move.from.column)
//        val stringifiedTo = getStringPosition(move.to.row, move.to.column)
//
//        val (play, result) = game.movePiece(stringifiedFrom, stringifiedTo)
//
//        return when (result) {
//            GENERAL_MOVE_VIOLATION -> InvalidMove("Invalid move")
//            PIECE_VIOLATION -> InvalidMove("Piece cannot move that way")
//            POST_PLAY_VIOLATION -> InvalidMove("Your move would cause an invalid state")
//            WHITE_WINS -> GameOver(WHITE)
//            BLACK_WINS -> GameOver(BLACK)
//            TIE -> TODO()
//            VALID_MOVE -> {
//                pieces = getChessPieces(play)
//                NewGameState(pieces, currentPlayer)
//            }
//        }
//    }
//
//    private fun getChessPieces(play: Play): List<ChessPiece> {
//        play.actions.forEach {
//            when (it) {
//                is edu.austral.dissis.chess.engine.Move -> {
//                    applyMoveAction(it)
//                }
//                is Take -> {
//                    applyTakeAction(it)
//                }
//            }
//        }
//    }
//
//    private fun applyMoveAction(moveAction: edu.austral.dissis.chess.engine.Move) {
//    //TODO: IDEA: hacer un adapter para move, ya que Action.execute() devuelve un board y en nuestro caso queremos devolver una lista de piezas, que es parecido
//        var pieceId: String
//
//        for (piece in pieces) {
//            val (row, col) = moveAction.board.unpackPosition(moveAction.from)
//            if (piece.position.row == row && piece.position.column == col) pieceId = piece.pieceId
//        }
//    }
//
//    override fun init(): InitialState {
//        return InitialState(BoardSize(8, 8), pieces, currentPlayer)
//    }
//}
//
