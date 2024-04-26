package edu.austral.dissis.chess.engine.exam

import edu.austral.dissis.chess.engine.*
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
                pieceTypes = getPieceTypes(),
                gameRules = TestableStandardGameRules(),
                turnManager = OneToOneTurnManager()
            )
        ).test()
    }

    private fun getPieceTypes(): Map<Piece, TestPiece> {
        return listOf(Player.WHITE, Player.BLACK)
            .zip(listOf('W', 'B'))
            .flatMap {
                listOf(
                    Piece(it.first, PawnPieceRules(it.first)) to TestPiece('P', it.second),
                    Piece(it.first, RookPieceRules(it.first)) to TestPiece('R', it.second),
                    Piece(it.first, BishopPieceRules(it.first)) to TestPiece('B', it.second),
                    Piece(it.first, QueenPieceRules(it.first)) to TestPiece('Q', it.second),
                    Piece(it.first, KnightPieceRules(it.first)) to TestPiece('N', it.second),
                    Piece(it.first, KingPieceRules(it.first)) to TestPiece('K', it.second)
                )
            }
            .toMap()
    }
}