package edu.austral.dissis.chess.engine.exam

import edu.austral.dissis.chess.checkers.CheckersPieceProvider.getAmericanKing
import edu.austral.dissis.chess.checkers.CheckersPieceProvider.getAmericanMan
import edu.austral.dissis.chess.checkers.CheckersPieceProvider.getMan
import edu.austral.dissis.chess.checkers.getCheckersGameRules
import edu.austral.dissis.chess.checkers.getCheckersTurnManager
import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getArchbishop
import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getBishop
import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getChancellor
import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getKing
import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getKnight
import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getPawn
import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getQueen
import edu.austral.dissis.chess.chess.pieces.ChessPieceProvider.getRook
import edu.austral.dissis.chess.chess.variants.getChessGameRules
import edu.austral.dissis.chess.engine.Player.BLACK
import edu.austral.dissis.chess.engine.Player.WHITE
import edu.austral.dissis.chess.engine.custom.AmericanCheckersGameTester
import edu.austral.dissis.chess.engine.custom.CapablancaGameTester
import edu.austral.dissis.chess.engine.custom.CheckersGameTester
import edu.austral.dissis.chess.engine.custom.ChessGameTester
import edu.austral.dissis.chess.engine.custom.IncrementalGameTester
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.turns.IncrementalTurnManager
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
            TestFrameworkGameRunner(
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
            TestFrameworkGameRunner(
                pieceAdapter = PieceAdapter(getChessTypes()),
                gameRules = getChessGameRules(),
                turnManager = OneToOneTurnManager(WHITE),
            ),
        )
            .test()
//            .debug("archbishop_can_move_to_corner.md")
    }

    @TestFactory
    fun `custom capablanca tests`(): Stream<DynamicTest> {
        return CapablancaGameTester(
            TestFrameworkGameRunner(
                pieceAdapter = PieceAdapter(getCapablancaTypes()),
                gameRules = getChessGameRules(),
                turnManager = OneToOneTurnManager(WHITE),
            ),
        )
            .test()
//            .debug("archbishop_can_move_to_corner.md")
    }

    @TestFactory
    fun `custom incremental chess tests`(): Stream<DynamicTest> {
        return IncrementalGameTester(
            TestFrameworkGameRunner(
                pieceAdapter = PieceAdapter(getCapablancaTypes()),
                gameRules = getChessGameRules(),
                turnManager = IncrementalTurnManager(WHITE, initialNumberTurns = 1),
            ),
        )
            .test()
//            .debug("archbishop_can_move_to_corner.md")
    }

    @TestFactory
    fun `custom checkers tests`(): Stream<DynamicTest> {
        return CheckersGameTester(
            TestFrameworkGameRunner(
                pieceAdapter = PieceAdapter(getCheckersTypes()),
                gameRules = getCheckersGameRules(),
                turnManager = getCheckersTurnManager(),
            ),
        )
            .test()
//            .debug("man_cannot_avoid_jump.md")
    }

    @TestFactory
    fun `custom american checkers tests`(): Stream<DynamicTest> {
        return AmericanCheckersGameTester(
            TestFrameworkGameRunner(
                pieceAdapter = PieceAdapter(getAmericanCheckersTypes()),
                gameRules = getCheckersGameRules(),
                turnManager = getCheckersTurnManager(),
            ),
        )
            .test()
//            .debug("king_jumps_backwards.md")
    }

    private fun getChessTypes(): Map<Piece, TestPiece> {
        return listOf(WHITE to 'W', BLACK to 'B')
            .flatMap {
                listOf(
                    getPawn(it.first) to TestPiece('P', it.second),
                    getRook(it.first) to TestPiece('R', it.second),
                    getBishop(it.first) to TestPiece('B', it.second),
                    getQueen(it.first) to TestPiece('Q', it.second),
                    getKnight(it.first) to TestPiece('N', it.second),
                    getKing(it.first) to TestPiece('K', it.second),
                )
            }
            .toMap()
    }

    private fun getCapablancaTypes(): Map<Piece, TestPiece> {
        return listOf(WHITE to 'W', BLACK to 'B')
            .flatMap {
                listOf(
                    getPawn(it.first) to TestPiece('P', it.second),
                    getRook(it.first) to TestPiece('R', it.second),
                    getBishop(it.first) to TestPiece('B', it.second),
                    getQueen(it.first) to TestPiece('Q', it.second),
                    getKnight(it.first) to TestPiece('N', it.second),
                    getKing(it.first) to TestPiece('K', it.second),
                    getArchbishop(it.first) to TestPiece('A', it.second),
                    getChancellor(it.first) to TestPiece('C', it.second),
                )
            }
            .toMap()
    }

    private fun getCheckersTypes(): Map<Piece, TestPiece> {
        return listOf(WHITE to 'W', BLACK to 'B')
            .flatMap {
                listOf(
                    getMan(it.first) to TestPiece('P', it.second),
                    getCheckersKing(it.first) to TestPiece('K', it.second),
                )
            }
            .toMap()
    }

    private fun getAmericanCheckersTypes(): Map<Piece, TestPiece> {
        return listOf(WHITE to 'W', BLACK to 'B')
            .flatMap {
                listOf(
                    getAmericanMan(it.first) to TestPiece('P', it.second),
                    getAmericanKing(it.first) to TestPiece('K', it.second),
                )
            }
            .toMap()
    }
}
