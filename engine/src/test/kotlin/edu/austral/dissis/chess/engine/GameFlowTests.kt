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
        provider.addMove(Player.WHITE, Position(1, 3), Position(1, 4))
        provider.addMove(Player.BLACK,Position(7, 8), Position(1, 2))

        val pieces = listOf(
            Position(2, 1) to blackRook, Position(7, 8) to blackQueen,Position(1, 3) to whiteKing, Position(8, 4) to blackKing
        )

        val board = HashGameBoard.build(validator, pieces, Position(1, 3), Position(8, 4))
        val game = TestableGame(TestableStandardGameRules(), board, OneToOneTurnManager(), provider)

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
        provider.addMove(Player.WHITE,Position(1, 3), Position(1, 4))
        provider.addMove(Player.BLACK,Position(6, 3), Position(3, 3))

        val pieces = listOf(
            Position(2, 1) to blackRook, Position(6, 3) to blackQueen,Position(1, 3) to whiteKing, Position(8, 4) to blackKing
        )

        val board = HashGameBoard.build(validator, pieces, Position(1, 3), Position(8, 4))
        val game = TestableGame(TestableStandardGameRules(), board, OneToOneTurnManager(), provider)

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
        provider.addMove(Player.WHITE, Position(7, 1), Position(8, 1))
        provider.addMove(Player.BLACK, Position(4, 8), Position(8, 4))
        provider.addMove(Player.WHITE, Position(8, 1), Position(8, 4))

        val pieces = listOf(
            Position(4, 8) to blackBishop, Position(7, 8) to blackPawn1, Position(7, 7) to blackPawn2,
            Position(7, 1) to whiteRook, Position(2, 2) to whiteKing, Position(8, 8) to blackKing
        )

        val board = HashGameBoard.build(validator, pieces, Position(2, 2), Position(8, 8))
        val game = TestableGame(TestableStandardGameRules(), board, OneToOneTurnManager(), provider)

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
        provider.addMove(Player.WHITE,Position(2, 3), Position(6, 7))

        val pieces = listOf(
            Position(2, 7) to blackRook, Position(2, 3) to whiteBishop, Position(2, 2) to whiteKing, Position(7, 7) to blackKing
        )

        val board = HashGameBoard.build(validator, pieces, Position(2, 2), Position(7, 7))
        val game = TestableGame(TestableStandardGameRules(), board, OneToOneTurnManager(), provider)

        assertThrows<NoSuchElementException> { game.run() }

        assertEquals(blackRook, game.board.getPieceAt(Position(2, 7)))
        assertEquals(whiteBishop, game.board.getPieceAt(Position(2, 3)))
        assertEquals(whiteKing, game.board.getPieceAt(Position(2, 2)))
        assertEquals(blackKing, game.board.getPieceAt(Position(7, 7)))
        assertEquals(true, provider.isEmpty()) // All moves should have been executed
    }

    //TODO: Test you cannot move a piece if that would leave your king checked
    //TODO: Test a complex situation, where a king appears to be on checkmate,
    // but actually there's an enemy piece which is not blocking a possible
    // move from the enemy king, since that would leave its own king checked

}

class BufferedInputProvider: PlayerInputProvider {
    val moves: Queue<Pair<Position, Position>> = LinkedList()
    val players: Queue<Player> = LinkedList()
    val promotions: Queue<PieceRules> = LinkedList()

    override fun requestPlayerMove(player: Player): Pair<Position, Position> {
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

    fun addMove(player: Player, from: Position, to: Position) {
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