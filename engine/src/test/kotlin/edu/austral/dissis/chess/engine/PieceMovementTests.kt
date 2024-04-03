package edu.austral.dissis.chess.engine

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PawnMovementTests {
    lateinit var board: HashGameBoard
    lateinit var game: Game
    lateinit var whitePawn: Piece
    lateinit var blackPawn: Piece


    @BeforeEach
    fun setUp() {
        board = HashGameBoard(RectangleBoardValidator(8, 8))
        game = Game(NoRules(), board)

        whitePawn = Piece(Player.WHITE, board, PawnPieceRules(board, Player.WHITE))
        blackPawn = Piece(Player.BLACK, board, PawnPieceRules(board, Player.BLACK))

        board.setPieceAt("d1", whitePawn)
        board.setPieceAt("c2", blackPawn)
    }

    @Test
    fun `black pawn can take diagonally`() {
        val whitePawn = Piece(Player.WHITE, board, PawnPieceRules(board, Player.WHITE))
        val blackPawn = Piece(Player.BLACK, board, PawnPieceRules(board, Player.BLACK))

        board.setPieceAt("d1", whitePawn)
        board.setPieceAt("c2", blackPawn)

        game.movePiece("c2", "d1")

        assertEquals(null, board.getPieceAt("c2"))
        assertEquals(blackPawn, board.getPieceAt("d1"))
    }

    @Test
    fun `white pawn can take diagonally`() {
        game.movePiece("d1", "c2")

        assertEquals(null, board.getPieceAt("d1"))
        assertEquals(whitePawn, board.getPieceAt("c2"))
    }

    @Test
    fun `white pawn cannot move anywhere`() {
        game.movePiece("d1", "h8")

        assertEquals(whitePawn, board.getPieceAt("d1"))
        assertEquals(null, board.getPieceAt("h8"))
        assertEquals(blackPawn, board.getPieceAt("c2"))
    }

    @Test
    fun `white pawn can move ahead`() {
        game.movePiece("d1", "d2")

        assertEquals(whitePawn, board.getPieceAt("d2"))
        assertEquals(null, board.getPieceAt("d1"))
        assertEquals(blackPawn, board.getPieceAt("c2"))
    }

    @Test
    fun `white pawn cannot move to the other diagonal`() {
        game.movePiece("d1", "e2")

        assertEquals(whitePawn, board.getPieceAt("d1"))
        assertEquals(null, board.getPieceAt("e2"))
        assertEquals(blackPawn, board.getPieceAt("c2"))
    }

    @Test
    fun `white moves two spaces but then cannot again`() {
        game.movePiece("d1", "d3")

        assertEquals(whitePawn, board.getPieceAt("d3"))
        assertEquals(null, board.getPieceAt("d1"))
        assertEquals(blackPawn, board.getPieceAt("c2"))

        game.movePiece("d3", "d5")

        assertEquals(whitePawn, board.getPieceAt("d3"))
        assertEquals(null, board.getPieceAt("d5"))
        assertEquals(blackPawn, board.getPieceAt("c2"))
    }
}

class TowerMovementTest {
    lateinit var board: HashGameBoard
    lateinit var game: Game
    lateinit var closestPawn: Piece
    lateinit var blockedPawn: Piece
    lateinit var whiteTower: Piece


    @BeforeEach
    fun setUp() {
        board = HashGameBoard(RectangleBoardValidator(8, 8))
        game = Game(NoRules(), board)

        closestPawn = Piece(Player.BLACK, board, PawnPieceRules(board, Player.BLACK))
        blockedPawn = Piece(Player.BLACK, board, PawnPieceRules(board, Player.BLACK))
        whiteTower = Piece(Player.WHITE, board, TowerPieceRules(board, Player.WHITE))

        board.setPieceAt("b4", closestPawn)
        board.setPieceAt("b6", blockedPawn)
        board.setPieceAt("b2", whiteTower)
    }

    @Test
    fun `tower takes closest pawn`() {
        game.movePiece("b2", "b4")

        assertEquals(whiteTower, board.getPieceAt("b4"))
        assertEquals(null, board.getPieceAt("b2"))
        assertEquals(blockedPawn, board.getPieceAt("b6"))
    }

    @Test
    fun `tower cannot take blocked pawn`() {
        game.movePiece("b2", "b6")

        assertEquals(whiteTower, board.getPieceAt("b2"))
        assertEquals(closestPawn, board.getPieceAt("b4"))
        assertEquals(blockedPawn, board.getPieceAt("b6"))
    }

    @Test
    fun `tower can move back`() {
        game.movePiece("b2", "b1")

        assertEquals(whiteTower, board.getPieceAt("b1"))
        assertEquals(null, board.getPieceAt("b2"))
        assertEquals(closestPawn, board.getPieceAt("b4"))
        assertEquals(blockedPawn, board.getPieceAt("b6"))
    }

    @Test
    fun `tower can move right`() {
        game.movePiece("b2", "a2")

        assertEquals(whiteTower, board.getPieceAt("a2"))
        assertEquals(null, board.getPieceAt("b2"))
        assertEquals(closestPawn, board.getPieceAt("b4"))
        assertEquals(blockedPawn, board.getPieceAt("b6"))
    }

    @Test
    fun `tower can move left`() {
        game.movePiece("b2", "h2")

        assertEquals(whiteTower, board.getPieceAt("h2"))
        assertEquals(null, board.getPieceAt("b2"))
        assertEquals(closestPawn, board.getPieceAt("b4"))
        assertEquals(blockedPawn, board.getPieceAt("b6"))
    }

    @Test
    fun `tower cannot move diagonally`() {
        game.movePiece("b2", "d4")

        assertEquals(whiteTower, board.getPieceAt("b2"))
        assertEquals(null, board.getPieceAt("d4"))
        assertEquals(closestPawn, board.getPieceAt("b4"))
        assertEquals(blockedPawn, board.getPieceAt("b6"))
    }
}

class NoRules : GameRules {
    override fun isPieceMovable(position: String): Boolean {
        return true
    }

    override fun isMoveValid(from: String, to: String): Boolean {
        return true
    }

    override fun playerReachedWinCondition(player: Player): Boolean {
        return false
    }

    override fun playerIsChecked(player: Player): Boolean {
        return false
    }

}
