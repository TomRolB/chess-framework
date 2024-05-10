package edu.austral.dissis.chess.engine.exam

import edu.austral.dissis.chess.checkers.MultiTurnManager
import edu.austral.dissis.chess.checkers.getMan
import edu.austral.dissis.chess.checkers.rules.CompulsoryJumpsValidator
import edu.austral.dissis.chess.chess.pieces.getBishop
import edu.austral.dissis.chess.chess.pieces.getKing
import edu.austral.dissis.chess.chess.pieces.getKnight
import edu.austral.dissis.chess.chess.pieces.getPawn
import edu.austral.dissis.chess.chess.pieces.getQueen
import edu.austral.dissis.chess.chess.pieces.getRook
import edu.austral.dissis.chess.chess.rules.gamerules.ClassicPostPlayValidator
import edu.austral.dissis.chess.chess.rules.gamerules.ClassicPrePlayValidator
import edu.austral.dissis.chess.chess.rules.gamerules.ClassicWinCondition
import edu.austral.dissis.chess.engine.Play
import edu.austral.dissis.chess.engine.rules.winconditions.ExtinctionWinCondition
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.Player.WHITE
import edu.austral.dissis.chess.engine.Player.BLACK
import edu.austral.dissis.chess.engine.PostPlayValidator
import edu.austral.dissis.chess.engine.board.GameBoard
import edu.austral.dissis.chess.engine.custom.CheckersGameTester
import edu.austral.dissis.chess.engine.custom.ChessGameTester
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.pieces.PlayResult
import edu.austral.dissis.chess.engine.rules.standard.gamerules.StandardGameRules
import edu.austral.dissis.chess.engine.turns.OneToOneTurnManager
import edu.austral.dissis.chess.test.TestPiece
import edu.austral.dissis.chess.test.game.GameTester
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.util.stream.Stream
import edu.austral.dissis.chess.checkers.getKing as getCheckersKing

class Exam {
    @TestFactory
    fun `required exam tests`(): Stream<DynamicTest> {
//        return GameTester(DummyTestGameRunner()).test()

        return GameTester(
            AdapterTestGameRunner(
                pieceAdapter = PieceAdapter(getChessTypes()),
                gameRules =
                    StandardGameRules(
                        ClassicPrePlayValidator(),
                        ClassicPostPlayValidator(),
                        ClassicWinCondition(),
                    ),
                turnManager = OneToOneTurnManager(WHITE),
            ),
        )
            .test()
//            .debug("mate_pawn.md")
    }

    @TestFactory
    fun `custom chess tests`(): Stream<DynamicTest> {
//        return GameTester(DummyTestGameRunner()).test()

        return ChessGameTester(
            AdapterTestGameRunner(
                pieceAdapter = PieceAdapter(getChessTypes()),
                gameRules =
                    StandardGameRules(
                        ClassicPrePlayValidator(),
                        ClassicPostPlayValidator(),
                        ClassicWinCondition(),
                    ),
                turnManager = OneToOneTurnManager(WHITE),
            ),
        )
            .test()
//            .debug("bishop_can_move_to_corner.md")
    }

    @TestFactory
    fun `custom checkers tests`(): Stream<DynamicTest> {
//        return GameTester(DummyTestGameRunner()).test()

        return CheckersGameTester(
            AdapterTestGameRunner(
                pieceAdapter = PieceAdapter(getCheckersTypes()),
                gameRules =
                StandardGameRules(
                    CompulsoryJumpsValidator(),
                    object : PostPlayValidator {
                        override fun getPostPlayResult(play: Play, board: GameBoard, player: Player): PlayResult {
                            return PlayResult(play, "Valid move")
                        }
                    },
                    ExtinctionWinCondition(),
                ),
                turnManager = MultiTurnManager(WHITE),
            ),
        )
            .test()
//            .debug("king_jumps_from_distance.md")
    }

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
