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

class RookMovementTest {
    lateinit var board: HashGameBoard
    lateinit var game: Game
    lateinit var closestPawn: Piece
    lateinit var blockedPawn: Piece
    lateinit var whiteRook: Piece


    @BeforeEach
    fun setUp() {
        board = HashGameBoard(RectangleBoardValidator(8, 8))
        game = Game(NoRules(), board)

        closestPawn = Piece(Player.BLACK, board, PawnPieceRules(board, Player.BLACK))
        blockedPawn = Piece(Player.BLACK, board, PawnPieceRules(board, Player.BLACK))
        whiteRook = Piece(Player.WHITE, board, RookPieceRules(board, Player.WHITE))

        board.setPieceAt("b4", closestPawn)
        board.setPieceAt("b6", blockedPawn)
        board.setPieceAt("b2", whiteRook)
    }

    @Test
    fun `rook takes closest pawn`() {
        game.movePiece("b2", "b4")

        assertEquals(whiteRook, board.getPieceAt("b4"))
        assertEquals(null, board.getPieceAt("b2"))
        assertEquals(blockedPawn, board.getPieceAt("b6"))
    }

    @Test
    fun `rook cannot take blocked pawn`() {
        game.movePiece("b2", "b6")

        assertEquals(whiteRook, board.getPieceAt("b2"))
        assertEquals(closestPawn, board.getPieceAt("b4"))
        assertEquals(blockedPawn, board.getPieceAt("b6"))
    }

    @Test
    fun `rook can move back`() {
        game.movePiece("b2", "b1")

        assertEquals(whiteRook, board.getPieceAt("b1"))
        assertEquals(null, board.getPieceAt("b2"))
        assertEquals(closestPawn, board.getPieceAt("b4"))
        assertEquals(blockedPawn, board.getPieceAt("b6"))
    }

    @Test
    fun `rook can move right`() {
        game.movePiece("b2", "a2")

        assertEquals(whiteRook, board.getPieceAt("a2"))
        assertEquals(null, board.getPieceAt("b2"))
        assertEquals(closestPawn, board.getPieceAt("b4"))
        assertEquals(blockedPawn, board.getPieceAt("b6"))
    }

    @Test
    fun `rook can move left`() {
        game.movePiece("b2", "h2")

        assertEquals(whiteRook, board.getPieceAt("h2"))
        assertEquals(null, board.getPieceAt("b2"))
        assertEquals(closestPawn, board.getPieceAt("b4"))
        assertEquals(blockedPawn, board.getPieceAt("b6"))
    }

    @Test
    fun `rook cannot move diagonally`() {
        game.movePiece("b2", "d4")

        assertEquals(whiteRook, board.getPieceAt("b2"))
        assertEquals(null, board.getPieceAt("d4"))
        assertEquals(closestPawn, board.getPieceAt("b4"))
        assertEquals(blockedPawn, board.getPieceAt("b6"))
    }
}

class BishopMovementTest {
    lateinit var board: HashGameBoard
    lateinit var game: Game
    lateinit var closestPawn: Piece
    lateinit var blockedPawn: Piece
    lateinit var whiteBishop: Piece


    @BeforeEach
    fun setUp() {
        board = HashGameBoard(RectangleBoardValidator(8, 8))
        game = Game(NoRules(), board)

        closestPawn = Piece(Player.BLACK, board, PawnPieceRules(board, Player.BLACK))
        blockedPawn = Piece(Player.BLACK, board, PawnPieceRules(board, Player.BLACK))
        whiteBishop = Piece(Player.WHITE, board, BishopPieceRules(board, Player.WHITE))

        board.setPieceAt("d4", closestPawn)
        board.setPieceAt("c3", blockedPawn)
        board.setPieceAt("g7", whiteBishop)
    }

    @Test
    fun `bishop takes closest pawn`() {
        game.movePiece("g7", "d4")

        assertEquals(whiteBishop, board.getPieceAt("d4"))
        assertEquals(null, board.getPieceAt("g7"))
        assertEquals(blockedPawn, board.getPieceAt("c3"))
    }

    @Test
    fun `bishop cannot take blocked pawn`() {
        game.movePiece("g7", "c3")

        assertEquals(whiteBishop, board.getPieceAt("g7"))
        assertEquals(closestPawn, board.getPieceAt("d4"))
        assertEquals(blockedPawn, board.getPieceAt("c3"))
    }

    @Test
    fun `bishop can move to corner`() {
        game.movePiece("g7", "h8")

        assertEquals(whiteBishop, board.getPieceAt("h8"))
        assertEquals(null, board.getPieceAt("g7"))
        assertEquals(closestPawn, board.getPieceAt("d4"))
        assertEquals(blockedPawn, board.getPieceAt("c3"))
    }

    @Test
    fun `bishop can move to the front and to the right`() {
        game.movePiece("g7", "f8")

        assertEquals(whiteBishop, board.getPieceAt("f8"))
        assertEquals(null, board.getPieceAt("g7"))
        assertEquals(closestPawn, board.getPieceAt("d4"))
        assertEquals(blockedPawn, board.getPieceAt("c3"))
    }

    @Test
    fun `bishop can move close to the first pawn`() {
        game.movePiece("g7", "e5")

        assertEquals(whiteBishop, board.getPieceAt("e5"))
        assertEquals(null, board.getPieceAt("g7"))
        assertEquals(closestPawn, board.getPieceAt("d4"))
        assertEquals(blockedPawn, board.getPieceAt("c3"))
    }

    @Test
    fun `bishop cannot move vertically`() {
        game.movePiece("g7", "g2")

        assertEquals(whiteBishop, board.getPieceAt("g7"))
        assertEquals(null, board.getPieceAt("g2"))
        assertEquals(closestPawn, board.getPieceAt("d4"))
        assertEquals(blockedPawn, board.getPieceAt("c3"))
    }
}

class QueenMovementTest {
    lateinit var board: HashGameBoard
    lateinit var game: Game
    lateinit var horizontalPawn: Piece
    lateinit var unreachablePawn: Piece
    lateinit var diagonalPawn: Piece
    lateinit var whiteQueen: Piece


    @BeforeEach
    fun setUp() {
        board = HashGameBoard(RectangleBoardValidator(8, 8))
        game = Game(NoRules(), board)

        horizontalPawn = Piece(Player.BLACK, board, PawnPieceRules(board, Player.BLACK))
        unreachablePawn = Piece(Player.BLACK, board, PawnPieceRules(board, Player.BLACK))
        diagonalPawn = Piece(Player.BLACK, board, PawnPieceRules(board, Player.BLACK))
        whiteQueen = Piece(Player.WHITE, board, QueenPieceRules(board, Player.WHITE))

        board.setPieceAt("b6", horizontalPawn)
        board.setPieceAt("b5", unreachablePawn)
        board.setPieceAt("b4", diagonalPawn)
        board.setPieceAt("d6", whiteQueen)
    }

    @Test
    fun `queen takes pawn horizontally`() {
        game.movePiece("d6", "b6")

        assertEquals(whiteQueen, board.getPieceAt("b6"))
        assertEquals(null, board.getPieceAt("d6"))
        assertEquals(unreachablePawn, board.getPieceAt("b5"))
        assertEquals(diagonalPawn, board.getPieceAt("b4"))
    }

    @Test
    fun `queen cannot take unreachable pawn`() {
        game.movePiece("d6", "b5")

        assertEquals(whiteQueen, board.getPieceAt("d6"))
        assertEquals(horizontalPawn, board.getPieceAt("b6"))
        assertEquals(unreachablePawn, board.getPieceAt("b5"))
        assertEquals(diagonalPawn, board.getPieceAt("b4"))
    }

    @Test
    fun `queen takes pawn diagonally`() {
        game.movePiece("d6", "b4")

        assertEquals(whiteQueen, board.getPieceAt("b4"))
        assertEquals(null, board.getPieceAt("d6"))
        assertEquals(unreachablePawn, board.getPieceAt("b5"))
        assertEquals(horizontalPawn, board.getPieceAt("b6"))
    }

    @Test
    fun `queen can move back`() {
        game.movePiece("d6", "d1")

        assertEquals(whiteQueen, board.getPieceAt("d1"))
        assertEquals(null, board.getPieceAt("d6"))
        assertEquals(horizontalPawn, board.getPieceAt("b6"))
        assertEquals(unreachablePawn, board.getPieceAt("b5"))
        assertEquals(diagonalPawn, board.getPieceAt("b4"))
    }

    @Test
    fun `queen can move to the front and to the left`() {
        game.movePiece("d6", "e7")

        assertEquals(whiteQueen, board.getPieceAt("e7"))
        assertEquals(null, board.getPieceAt("d6"))
        assertEquals(horizontalPawn, board.getPieceAt("b6"))
        assertEquals(unreachablePawn, board.getPieceAt("b5"))
        assertEquals(diagonalPawn, board.getPieceAt("b4"))
    }

    @Test
    fun `queen cannot bypass pawn diagonally`() {
        game.movePiece("d6", "a3")

        assertEquals(whiteQueen, board.getPieceAt("d6"))
        assertEquals(null, board.getPieceAt("a3"))
        assertEquals(horizontalPawn, board.getPieceAt("b6"))
        assertEquals(unreachablePawn, board.getPieceAt("b5"))
        assertEquals(diagonalPawn, board.getPieceAt("b4"))
    }

    @Test
    fun `queen cannot move like knight`() {
        game.movePiece("d6", "e4")

        assertEquals(whiteQueen, board.getPieceAt("d6"))
        assertEquals(null, board.getPieceAt("e4"))
        assertEquals(horizontalPawn, board.getPieceAt("b6"))
        assertEquals(unreachablePawn, board.getPieceAt("b5"))
        assertEquals(diagonalPawn, board.getPieceAt("b4"))
    }
}

class KnightMovementTest {
    lateinit var board: HashGameBoard
    lateinit var game: Game
    lateinit var horizontalPawn: Piece
    lateinit var verticalPawn: Piece
    lateinit var takeablePawn: Piece
    lateinit var whiteKnight: Piece


    @BeforeEach
    fun setUp() {
        board = HashGameBoard(RectangleBoardValidator(8, 8))
        game = Game(NoRules(), board)

        horizontalPawn = Piece(Player.BLACK, board, PawnPieceRules(board, Player.BLACK))
        takeablePawn = Piece(Player.BLACK, board, PawnPieceRules(board, Player.BLACK))
        verticalPawn = Piece(Player.BLACK, board, PawnPieceRules(board, Player.BLACK))
        whiteKnight = Piece(Player.WHITE, board, KnightPieceRules(board, Player.WHITE))

        board.setPieceAt("f3", horizontalPawn)
        board.setPieceAt("f5", takeablePawn)
        board.setPieceAt("g4", verticalPawn)
        board.setPieceAt("g3", whiteKnight)
    }

    @Test
    fun `knight takes pawn moving in an L shape`() {
        game.movePiece("g3", "f5")

        assertEquals(whiteKnight, board.getPieceAt("f5"))
        assertEquals(null, board.getPieceAt("g3"))
        assertEquals(horizontalPawn, board.getPieceAt("f3"))
        assertEquals(verticalPawn, board.getPieceAt("g4"))
    }

    @Test
    fun `knight cannot take pawn horizontally`() {
        game.movePiece("g3", "f3")

        assertEquals(whiteKnight, board.getPieceAt("g3"))
        assertEquals(verticalPawn, board.getPieceAt("g4"))
        assertEquals(takeablePawn, board.getPieceAt("f5"))
        assertEquals(horizontalPawn, board.getPieceAt("f3"))
    }

    @Test
    fun `knight cannot take pawn vertically`() {
        game.movePiece("g3", "g4")

        assertEquals(whiteKnight, board.getPieceAt("g3"))
        assertEquals(verticalPawn, board.getPieceAt("g4"))
        assertEquals(takeablePawn, board.getPieceAt("f5"))
        assertEquals(horizontalPawn, board.getPieceAt("f3"))
    }

    @Test
    fun `knight can jump over pawn in L shape`() {
        game.movePiece("g3", "e4")

        assertEquals(whiteKnight, board.getPieceAt("e4"))
        assertEquals(null, board.getPieceAt("g3"))
        assertEquals(verticalPawn, board.getPieceAt("g4"))
        assertEquals(takeablePawn, board.getPieceAt("f5"))
        assertEquals(horizontalPawn, board.getPieceAt("f3"))
    }

    @Test
    fun `knight cannot jump over pawn horizontally`() {
        game.movePiece("g3", "e3")

        assertEquals(whiteKnight, board.getPieceAt("g3"))
        assertEquals(null, board.getPieceAt("e3"))
        assertEquals(verticalPawn, board.getPieceAt("g4"))
        assertEquals(takeablePawn, board.getPieceAt("f5"))
        assertEquals(horizontalPawn, board.getPieceAt("f3"))
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
