package edu.austral.dissis.chess.engine

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

// This class tests movements like
// stalemate scenarios

class SpecialMovementsTests {
    val validator = RectangleBoardValidator(8, 8)

    @Test
    fun `pawn can perform en passant`() {
        val whitePawn = Piece(Player.WHITE, PawnPieceRules(Player.WHITE))
        val blackPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
        val whiteKing = Piece(Player.WHITE, KingPieceRules(Player.WHITE))
        val blackKing = Piece(Player.BLACK, KingPieceRules(Player.BLACK))

        val pieces = listOf(
            "c2" to whitePawn, "d4" to blackPawn, "a1" to whiteKing, "h8" to blackKing
        )

        val provider = BufferedInputProvider()
        provider.addMove(Player.WHITE, "c2", "c4")
        provider.addMove(Player.BLACK, "d4", "c3")

        val board = HashGameBoard.build(validator, pieces, "a1", "h8")
        val game = TurnManagingGame(StandardGameRules(), board, OneToOneTurnManager(), provider)

        assertThrows<NoSuchElementException> { game.run() }

        assertEquals(true, game.board.getPieceAt("c3")!!.rules is PawnPieceRules)
        assertEquals(Player.BLACK, game.board.getPieceAt("c3")!!.player)
        assertEquals(null, game.board.getPieceAt("c2"))
        assertEquals(null, game.board.getPieceAt("c4"))
        assertEquals(null, game.board.getPieceAt("d4"))
    }

    @Test
    fun `pawn promotes to queen`() {
        val whitePawn = Piece(Player.WHITE, PawnPieceRules(Player.WHITE))
        val whiteKing = Piece(Player.WHITE, KingPieceRules(Player.WHITE))
        val blackKing = Piece(Player.BLACK, KingPieceRules(Player.BLACK))

        val pieces = listOf(
            "a7" to whitePawn, "a1" to whiteKing, "h8" to blackKing
        )

        val provider = BufferedInputProvider()
        provider.addMove(Player.WHITE, "a7", "a8")
        provider.addPromotion(Player.WHITE, QueenPieceRules(Player.WHITE))

        val board = HashGameBoard.build(validator, pieces, "a1", "h8")
        val game = TurnManagingGame(StandardGameRules(), board, OneToOneTurnManager(), provider)

        assertThrows<NoSuchElementException> { game.run() }

        assertEquals(true, game.board.getPieceAt("a8")!!.rules is QueenPieceRules)
        assertEquals(Player.WHITE, game.board.getPieceAt("a8")!!.player)
        assertEquals(null, game.board.getPieceAt("a7"))
    }
}