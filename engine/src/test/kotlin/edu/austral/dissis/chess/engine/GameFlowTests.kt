package edu.austral.dissis.chess.engine
import org.junit.jupiter.api.Assertions.assertEquals

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

// This class tests different checkmate and
// stalemate scenarios

class GameFlowTests {
    val validator = RectangleBoardValidator(8, 8)

    @BeforeEach
    fun setUp() {

    }

    @Test
    fun `checkmate by black tower and black queen`() {
        val blackRook = Piece(Player.BLACK, RookPieceRules(Player.BLACK))
        val blackQueen = Piece(Player.BLACK, QueenPieceRules())
        val whiteKing = Piece(Player.WHITE, KingPieceRules(Player.WHITE))
        val blackKing = Piece(Player.BLACK, KingPieceRules(Player.WHITE))

        val provider = BufferedInputProvider()
        provider.addMove("c1", "d1")
        provider.addMove("h7", "b1")

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
        provider.addMove("c1", "d1")
        provider.addMove("c6", "c3")

        val pieces = listOf(
            "a2" to blackRook, "c6" to blackQueen,"c1" to whiteKing, "d8" to blackKing
        )

        val board = HashGameBoard.build(validator, pieces, "c1", "d8")
        val game = TestableGame(StandardGameRules(), board, OneToOneTurnManager(), provider)

        game.run()

        assertEquals(true, game.endedOnTie)
    }
}

class BufferedInputProvider: PlayerInputProvider {
    val moves: Queue<Pair<String, String>> = LinkedList()

    override fun requestPlayerMove(player: Player): Pair<String, String> {
        return moves.remove()
    }

    fun addMove(from: String, to: String) {
        moves.add(from to to)
    }
}