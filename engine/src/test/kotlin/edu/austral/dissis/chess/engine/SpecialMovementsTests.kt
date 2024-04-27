//package edu.austral.dissis.chess.engine
//
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.assertThrows
//import java.util.*
//
//// This class tests movements like
//// stalemate scenarios
//
//class SpecialMovementsTests {
//    val validator = RectangleBoardValidator(8, 8)
//
//    @Test
//    fun `pawn can perform en passant`() {
//        val whitePawn = Piece(Player.WHITE, PawnPieceRules(Player.WHITE))
//        val blackPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
//        val whiteKing = Piece(Player.WHITE, KingPieceRules(Player.WHITE, hasEverMoved = true))
//        val blackKing = Piece(Player.BLACK, KingPieceRules(Player.BLACK, hasEverMoved = true))
//
//        val pieces = listOf(
//            Position(2, 3) to whitePawn, Position(4, 4) to blackPawn, Position(1, 1) to whiteKing, Position(8, 8) to blackKing
//        )
//
//        val provider = BufferedInputProvider()
//        provider.addMove(Player.WHITE, Position(2, 3), Position(4, 3))
//        provider.addMove(Player.BLACK, Position(4, 4), Position(3, 3))
//
//        val board = HashGameBoard.build(validator, pieces, Position(1, 1), Position(8, 8))
//        val game = TestableGame(TestableStandardGameRules(), board, OneToOneTurnManager(), provider)
//
//        assertThrows<NoSuchElementException> { game.run() }
//
//        assertEquals(true, game.board.getPieceAt(Position(3, 3))!!.rules is PawnPieceRules)
//        assertEquals(Player.BLACK, game.board.getPieceAt(Position(3, 3))!!.player)
//        assertEquals(null, game.board.getPieceAt(Position(2, 3)))
//        assertEquals(null, game.board.getPieceAt(Position(4, 3)))
//        assertEquals(null, game.board.getPieceAt(Position(4, 4)))
//    }
//
//    @Test
//    fun `pawn promotes to queen`() {
//        val whitePawn = Piece(Player.WHITE, PawnPieceRules(Player.WHITE))
//        val whiteKing = Piece(Player.WHITE, KingPieceRules(Player.WHITE, hasEverMoved = true))
//        val blackKing = Piece(Player.BLACK, KingPieceRules(Player.BLACK, hasEverMoved = true))
//
//        val pieces = listOf(
//            Position(7, 1) to whitePawn, Position(1, 1) to whiteKing, Position(8, 8) to blackKing
//        )
//
//        val provider = BufferedInputProvider()
//        provider.addMove(Player.WHITE, Position(7, 1), Position(8, 1))
//
//        val board = HashGameBoard.build(validator, pieces, Position(1, 1), Position(8, 8))
//        val game = TestableGame(TestableStandardGameRules(), board, OneToOneTurnManager(), provider)
//
//        assertThrows<NoSuchElementException> { game.run() }
//
//        assertEquals(true, game.board.getPieceAt(Position(8, 1))!!.rules is QueenPieceRules)
//        assertEquals(Player.WHITE, game.board.getPieceAt(Position(8, 1))!!.player)
//        assertEquals(null, game.board.getPieceAt(Position(7, 1)))
//    }
//}