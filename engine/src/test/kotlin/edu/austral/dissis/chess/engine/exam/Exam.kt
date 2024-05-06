package edu.austral.dissis.chess.engine.exam

import edu.austral.dissis.chess.engine.OneToOneTurnManager
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.custom.CustomGameTester
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.pieces.getBishop
import edu.austral.dissis.chess.engine.pieces.getKing
import edu.austral.dissis.chess.engine.pieces.getKnight
import edu.austral.dissis.chess.engine.pieces.getPawn
import edu.austral.dissis.chess.engine.pieces.getQueen
import edu.austral.dissis.chess.engine.pieces.getRook
import edu.austral.dissis.chess.rules.standard.gamerules.StandardGameRules
import edu.austral.dissis.chess.test.TestBoard
import edu.austral.dissis.chess.test.TestPiece
import edu.austral.dissis.chess.test.game.GameTester
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.util.stream.Stream

class Exam {
    @TestFactory
    fun `required exam tests`(): Stream<DynamicTest> {
//        return GameTester(DummyTestGameRunner()).test()

        return GameTester(
            AdapterTestGameRunner(
                pieceAdapter = PieceAdapter(getPieceTypes()),
                postPlayProcedures = promotePawns(),
                gameRules = StandardGameRules(),
                turnManager = OneToOneTurnManager(),
            ),
        )
            .test()
//            .debug("mate_pawn.md")
    }

    @TestFactory
    fun `custom tests`(): Stream<DynamicTest> {
//        return GameTester(DummyTestGameRunner()).test()

        return CustomGameTester(
            AdapterTestGameRunner(
                pieceAdapter = PieceAdapter(getPieceTypes()),
                gameRules = StandardGameRules(),
                turnManager = OneToOneTurnManager(),
                postPlayProcedures = promotePawns(),
            ),
        )
//            .test()
            .debug("en_passant.md")
    }

    private fun promotePawns(): (TestBoard) -> TestBoard {
        return { testBoard: TestBoard ->
            TestBoard(
                testBoard.size,
                testBoard.pieces.map {
                    val (pos, piece) = it
                    if (
                        pos.row == testBoard.size.rows &&
                        piece.pieceTypeSymbol == 'P' &&
                        piece.playerColorSymbol == 'W'
                    ) {
                        pos to TestPiece('Q', 'W')
                    } else if (
                        pos.row == 0 &&
                        piece.pieceTypeSymbol == 'P' &&
                        piece.playerColorSymbol == 'B'
                    ) {
                        pos to TestPiece('Q', 'B')
                    } else {
                        pos to piece
                    }
                }.toMap(),
            )
        }
    }

    // TODO: This is horrible. Should create a Factory?
    private fun getPieceTypes(): Map<() -> Piece, TestPiece> {
        return listOf(Player.WHITE, Player.BLACK)
            .zip(listOf('W', 'B'))
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
}
