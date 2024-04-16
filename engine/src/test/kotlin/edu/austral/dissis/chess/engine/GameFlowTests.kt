package edu.austral.dissis.chess.engine

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

// This class tests different checkmate and
// stalemate scenarios

class GameFlowTests {
    val validator = RectangleBoardValidator(8, 8)

    @Test
    fun `checkmate by black tower and black queen`() {
        val blackRook = Piece(Player.BLACK, RookPieceRules(Player.BLACK))
        val blackQueen = Piece(Player.BLACK, QueenPieceRules(Player.BLACK))
        val whiteKing = Piece(Player.WHITE, KingPieceRules(Player.WHITE))
        val blackKing = Piece(Player.BLACK, KingPieceRules(Player.WHITE))

        val provider = BufferedInputProvider()
        provider.addMove(Player.WHITE, "c1", "d1")
        provider.addMove(Player.BLACK,"h7", "b1")

        val pieces = listOf(
            "a2" to blackRook, "h7" to blackQueen,"c1" to whiteKing, "d8" to blackKing
        )

        val board = HashGameBoard.build(validator, pieces, "c1", "d8")
        val game = TurnManagingGame(StandardGameRules(), board, OneToOneTurnManager(), provider)

        game.run()

        assertEquals(Player.BLACK, game.winner)
    }

    @Test
    fun `stalemate by black tower and black queen`() {
        val blackRook = Piece(Player.BLACK, RookPieceRules(Player.BLACK))
        val blackQueen = Piece(Player.BLACK, QueenPieceRules(Player.BLACK))
        val whiteKing = Piece(Player.WHITE, KingPieceRules(Player.WHITE))
        val blackKing = Piece(Player.BLACK, KingPieceRules(Player.WHITE))

        val provider = BufferedInputProvider()
        provider.addMove(Player.WHITE,"c1", "d1")
        provider.addMove(Player.BLACK,"c6", "c3")

        val pieces = listOf(
            "a2" to blackRook, "c6" to blackQueen,"c1" to whiteKing, "d8" to blackKing
        )

        val board = HashGameBoard.build(validator, pieces, "c1", "d8")
        val game = TurnManagingGame(StandardGameRules(), board, OneToOneTurnManager(), provider)

        game.run()

        assertEquals(true, game.endedOnTie)
    }

    @Test
    fun `black bishop saves check but ends in checkmate`() {
        val blackBishop = Piece(Player.BLACK, BishopPieceRules(Player.BLACK))
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
        val game = TurnManagingGame(StandardGameRules(), board, OneToOneTurnManager(), provider)

        game.run()

        assertEquals(Player.WHITE, game.winner)
        assertEquals(true, provider.isEmpty()) // All moves should have been executed
    }

    @Test
    fun `white bishop cannot expose its king`() {
        val blackRook = Piece(Player.BLACK, RookPieceRules(Player.BLACK))
        val whiteBishop = Piece(Player.WHITE, BishopPieceRules(Player.WHITE))
        val whiteKing = Piece(Player.WHITE, KingPieceRules(Player.WHITE))
        val blackKing = Piece(Player.BLACK, KingPieceRules(Player.WHITE))

        val provider = BufferedInputProvider()
        provider.addMove(Player.WHITE,"c2", "g6")

        val pieces = listOf(
            "g2" to blackRook, "c2" to whiteBishop, "b2" to whiteKing, "g7" to blackKing
        )

        val board = HashGameBoard.build(validator, pieces, "b2", "g7")
        val game = TurnManagingGame(StandardGameRules(), board, OneToOneTurnManager(), provider)

        assertThrows<NoSuchElementException> { game.run() }

        assertEquals(blackRook, game.board.getPieceAt("g2"))
        assertEquals(whiteBishop, game.board.getPieceAt("c2"))
        assertEquals(whiteKing, game.board.getPieceAt("b2"))
        assertEquals(blackKing, game.board.getPieceAt("g7"))
        assertEquals(true, provider.isEmpty()) // All moves should have been executed
    }

    //TODO: Test you cannot move a piece if that would leave your king checked
    //TODO: Test a complex situation, where a king appears to be on checkmate,
    // but actually there's an enemy piece which is not blocking a possible
    // move from the enemy king, since that would leave its own king checked

}

class BufferedInputProvider: PlayerInputProvider {
    val moves: Queue<Pair<String, String>> = LinkedList()
    val players: Queue<Player> = LinkedList()
    val promotions: Queue<PieceRules> = LinkedList()

    override fun requestPlayerMove(player: Player): Pair<String, String> {
        if (players.remove() != player) {
            throw IllegalStateException("The next move wasn't meant for the $player player")
        }

        return moves.remove()
    }

    override fun requestPromotionPiece(player: Player): PieceRules {
        if (players.remove() != player) {
            throw IllegalStateException("The next move wasn't meant for the $player player")
        }

        return promotions.remove()
    }

    fun addMove(player: Player, from: String, to: String) {
        moves.add(from to to)
        players.add(player)
    }

    fun isEmpty(): Boolean {
        return moves.isEmpty()
    }

    fun addPromotion(player: Player, pieceRules: PieceRules) {
        promotions.add(pieceRules)
        players.add(player)
    }
}