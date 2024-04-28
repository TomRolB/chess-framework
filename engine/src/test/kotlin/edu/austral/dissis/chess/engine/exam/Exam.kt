package edu.austral.dissis.chess.engine.exam

import CustomGameTester
import edu.austral.dissis.chess.engine.BishopPieceRules
import edu.austral.dissis.chess.engine.KingPieceRules
import edu.austral.dissis.chess.engine.KnightPieceRules
import edu.austral.dissis.chess.engine.OneToOneTurnManager
import edu.austral.dissis.chess.engine.PawnPieceRules
import edu.austral.dissis.chess.engine.Piece
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.QueenPieceRules
import edu.austral.dissis.chess.engine.RookPieceRules
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
//            .debug("short_castling.md")
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
            .test()
//            .debug("promotion.md")
    }

    private fun promotePawns(): (TestBoard) -> TestBoard {
        return {
                testBoard: TestBoard ->
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

    private fun getPieceTypes(): Map<() -> Piece, TestPiece> {
        return listOf(Player.WHITE, Player.BLACK)
            .zip(listOf('W', 'B'))
            .flatMap {
                listOf(
                    { Piece(it.first, PawnPieceRules(it.first)) } to TestPiece('P', it.second),
                    { Piece(it.first, RookPieceRules(it.first)) } to TestPiece('R', it.second),
                    { Piece(it.first, BishopPieceRules(it.first)) } to TestPiece('B', it.second),
                    { Piece(it.first, QueenPieceRules(it.first)) } to TestPiece('Q', it.second),
                    { Piece(it.first, KnightPieceRules(it.first)) } to TestPiece('N', it.second),
                    { Piece(it.first, KingPieceRules(it.first)) } to TestPiece('K', it.second),
                )
            }
            .toMap()
    }
}
