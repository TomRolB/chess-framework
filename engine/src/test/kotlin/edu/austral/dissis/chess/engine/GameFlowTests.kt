package edu.austral.dissis.chess.engine

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

// This class tests different checkmate and
// stalemate scenarios

class GameFlowTests {
    val validator = RectangleBoardValidator(8, 8)

    @Test
    fun `checkmate by black tower and black queen`() {
        val blackRook = Piece(Player.BLACK, RookPieceRules(Player.BLACK))
        val blackQueen = Piece(Player.BLACK, QueenPieceRules())
        val whiteKing = Piece(Player.WHITE, KingPieceRules(Player.WHITE))
        val blackKing = Piece(Player.BLACK, KingPieceRules(Player.WHITE))

        val provider = BufferedInputProvider()
        provider.addMove(Player.WHITE, "c1", "d1")
        provider.addMove(Player.BLACK,"h7", "b1")

        val pieces = listOf(
            "a2" to blackRook, "h7" to blackQueen,"c1" to whiteKing, "d8" to blackKing
        )

        val board = HashGameBoard.build(validator, pieces, "c1", "d8")
        val game = TestableGame(StandardGameRules(), board, OneToOneTurnManager(), provider)

        game.run()

        assertEquals(Player.BLACK, game.winner)
    }

    @Test
    fun `stalemate by black tower and black queen`() {
        val blackRook = Piece(Player.BLACK, RookPieceRules(Player.BLACK))
        val blackQueen = Piece(Player.BLACK, QueenPieceRules())
        val whiteKing = Piece(Player.WHITE, KingPieceRules(Player.WHITE))
        val blackKing = Piece(Player.BLACK, KingPieceRules(Player.WHITE))

        val provider = BufferedInputProvider()
        provider.addMove(Player.WHITE,"c1", "d1")
        provider.addMove(Player.BLACK,"c6", "c3")

        val pieces = listOf(
            "a2" to blackRook, "c6" to blackQueen,"c1" to whiteKing, "d8" to blackKing
        )

        val board = HashGameBoard.build(validator, pieces, "c1", "d8")
        val game = TestableGame(StandardGameRules(), board, OneToOneTurnManager(), provider)

        game.run()

        assertEquals(true, game.endedOnTie)
    }

    @Test
    fun `black bishop saves check but ends in checkmate`() {
        val blackBishop = Piece(Player.BLACK, BishopPieceRules())
        val blackPawn1 = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
        val blackPawn2 = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
        val whiteRook = Piece(Player.WHITE, RookPieceRules(Player.WHITE))
        val whiteKing = Piece(Player.WHITE, KingPieceRules(Player.WHITE))
        val blackKing = Piece(Player.BLACK, KingPieceRules(Player.WHITE))

        val provider = BufferedInputProvider()
        provider.addMove(Player.WHITE, "a7", "a8")
        provider.addMove(Player.BLACK, "h4", "d8")
        provider.addMove(Player.WHITE, "a8", "d8")

        val pieces = listOf(
            "h4" to blackBishop, "h7" to blackPawn1, "g7" to blackPawn2,
            "a7" to whiteRook, "b2" to whiteKing, "h8" to blackKing
        )

        val board = HashGameBoard.build(validator, pieces, "b2", "h8")
        val game = TestableGame(StandardGameRules(), board, OneToOneTurnManager(), provider)

        game.run()

        assertEquals(Player.WHITE, game.winner)
        assertEquals(true, provider.isEmpty()) // All moves should have been executed
    }
}

class BufferedInputProvider: PlayerInputProvider {
    val moves: Queue<Pair<String, String>> = LinkedList()
    val players: Queue<Player> = LinkedList()

    override fun requestPlayerMove(player: Player): Pair<String, String> {
        if (players.remove() != player) {
            throw IllegalStateException("The next move wasn't meant for the $player player")
        }

        return moves.remove()
    }

    fun addMove(player: Player, from: String, to: String) {
        moves.add(from to to)
        players.add(player)
    }

    fun isEmpty(): Boolean {
        return moves.isEmpty()
    }
}