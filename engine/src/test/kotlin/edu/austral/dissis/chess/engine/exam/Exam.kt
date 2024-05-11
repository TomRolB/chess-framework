package edu.austral.dissis.chess.engine.exam

import edu.austral.dissis.chess.checkers.CheckersPieceProvider.getMan
import edu.austral.dissis.chess.checkers.getCheckersGameRules
import edu.austral.dissis.chess.checkers.getCheckersTurnManager
import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getBishop
import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getKing
import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getKnight
import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getPawn
import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getQueen
import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getRook
import edu.austral.dissis.chess.chess.variants.getChessGameRules
import edu.austral.dissis.chess.engine.Player.BLACK
import edu.austral.dissis.chess.engine.Player.WHITE
import edu.austral.dissis.chess.engine.custom.CheckersGameTester
import edu.austral.dissis.chess.engine.custom.ChessGameTester
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.turns.OneToOneTurnManager
import edu.austral.dissis.chess.test.TestPiece
import edu.austral.dissis.chess.test.game.GameTester
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.util.stream.Stream
import edu.austral.dissis.chess.checkers.CheckersPieceProvider.getKing as getCheckersKing

class Exam {
    @TestFactory
    fun `required exam tests`(): Stream<DynamicTest> {
        return GameTester(
            AdapterTestGameRunner(
                pieceAdapter = PieceAdapter(getChessTypes()),
                gameRules = getChessGameRules(),
                turnManager = OneToOneTurnManager(WHITE),
            ),
        )
            .test()
//            .debug("mate_pawn.md")
    }

    @TestFactory
    fun `custom chess tests`(): Stream<DynamicTest> {
        return ChessGameTester(
            AdapterTestGameRunner(
                pieceAdapter = PieceAdapter(getChessTypes()),
                gameRules = getChessGameRules(),
                turnManager = OneToOneTurnManager(WHITE),
            ),
        )
            .test()
//            .debug("bishop_can_move_to_corner.md")
    }

    @TestFactory
    fun `custom checkers tests`(): Stream<DynamicTest> {
        return CheckersGameTester(
            AdapterTestGameRunner(
                pieceAdapter = PieceAdapter(getCheckersTypes()),
                gameRules = getCheckersGameRules(),
                turnManager = getCheckersTurnManager(),
            ),
        )
            .test()
//            .debug("king_double_jump_wins.md")
    }

    //TODO: should actually use clone() instead of passing a lambda
    private fun getChessTypes(): Map<() -> Piece, TestPiece> {
        return listOf(WHITE to 'W', BLACK to 'B')
            .flatMap {
                listOf(
                    { getPawn(it.first) } to TestPiece('P', it.second),
                    { getRook(it.first) } to TestPiece('R', it.second),
                    { getBishop(it.first) } to TestPiece('B', it.second),
                    { getQueen(it.first) } to TestPiece('Q', it.second),
                    { getKnight(it.first) } to TestPiece('N', it.second),
                    { getKing(it.first) } to TestPiece('K', it.second),
                )
            }
            .toMap()
    }

    private fun getCheckersTypes(): Map<() -> Piece, TestPiece> {
        return listOf(WHITE to 'W', BLACK to 'B')
            .flatMap {
                listOf(
                    { getMan(it.first) } to TestPiece('P', it.second),
                    { getCheckersKing(it.first) } to TestPiece('K', it.second),
                )
            }
            .toMap()
    }
}
