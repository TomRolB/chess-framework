package edu.austral.dissis.chess.engine

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PawnMovementTests {
    private lateinit var list: List<Pair<String, Piece>>
    private lateinit var board: HashGameBoard
    private lateinit var game: Game
    private lateinit var whitePawn: Piece
    private lateinit var blackPawn: Piece
    private lateinit var whiteKing: Piece
    private lateinit var blackKing: Piece

    @BeforeEach
    fun setUp() {
        whitePawn = Piece(Player.WHITE, PawnPieceRules(Player.WHITE))
        blackPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
        whiteKing = Piece(Player.WHITE, KingPieceRules(Player.WHITE))
        blackKing = Piece(Player.BLACK, KingPieceRules(Player.BLACK))

        list = listOf("d1" to whitePawn, "c2" to blackPawn, "h1" to whiteKing, "h8" to blackKing)
        board = HashGameBoard.build(RectangleBoardValidator(8, 8), list, "h1", "h8")
        game = Game(NoRules(), board)
    }

    @Test
    fun `black pawn can take diagonally`() {
        val whitePawn = Piece(Player.WHITE, PawnPieceRules(Player.WHITE))
        val blackPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))

        game.gameBoard = game.gameBoard.setPieceAt("d1", whitePawn)
        game.gameBoard = game.gameBoard.setPieceAt("c2", blackPawn)

        game.movePiece("c2", "d1")

        assertEquals(null, game.gameBoard.getPieceAt("c2"))
        assertEquals(blackPawn, game.gameBoard.getPieceAt("d1"))
    }

    @Test
    fun `white pawn can take diagonally`() {
        game.movePiece("d1", "c2")

        assertEquals(null, game.gameBoard.getPieceAt("d1"))
        assertEquals(whitePawn, game.gameBoard.getPieceAt("c2"))
    }

    @Test
    fun `white pawn cannot move anywhere`() {
        assertThrows<IllegalArgumentException> { game.movePiece("d1", "a8") }

        assertEquals(whitePawn, game.gameBoard.getPieceAt("d1"))
        assertEquals(null, game.gameBoard.getPieceAt("a8"))
        assertEquals(blackPawn, game.gameBoard.getPieceAt("c2"))
    }

    @Test
    fun `white pawn can move ahead`() {
        game.movePiece("d1", "d2")

        assertEquals(whitePawn, game.gameBoard.getPieceAt("d2"))
        assertEquals(null, game.gameBoard.getPieceAt("d1"))
        assertEquals(blackPawn, game.gameBoard.getPieceAt("c2"))
    }

    @Test
    fun `white pawn cannot move to the other diagonal`() {
        assertThrows<IllegalArgumentException> { game.movePiece("d1", "e2") }

        assertEquals(whitePawn, game.gameBoard.getPieceAt("d1"))
        assertEquals(null, game.gameBoard.getPieceAt("e2"))
        assertEquals(blackPawn, game.gameBoard.getPieceAt("c2"))
    }

    @Test
    fun `white moves two spaces but then cannot again`() {
        game.movePiece("d1", "d3")

        assertEquals(whitePawn.rules.javaClass, game.gameBoard.getPieceAt("d3")!!.rules.javaClass)
        assertEquals(whitePawn.player, game.gameBoard.getPieceAt("d3")!!.player)
        assertEquals(null, game.gameBoard.getPieceAt("d1"))
        assertEquals(blackPawn, game.gameBoard.getPieceAt("c2"))

        assertThrows<IllegalArgumentException> { game.movePiece("d3", "d5") }

        assertEquals(whitePawn.rules.javaClass, game.gameBoard.getPieceAt("d3")!!.rules.javaClass)
        assertEquals(whitePawn.player, game.gameBoard.getPieceAt("d3")!!.player)
        assertEquals(null, game.gameBoard.getPieceAt("d5"))
        assertEquals(blackPawn, game.gameBoard.getPieceAt("c2"))
    }
}

class RookMovementTest {
    private lateinit var list: List<Pair<String, Piece>>
    private lateinit var board: HashGameBoard
    private lateinit var game: Game
    private lateinit var closestPawn: Piece
    private lateinit var blockedPawn: Piece
    private lateinit var whiteRook: Piece
    private lateinit var whiteKing: Piece
    private lateinit var blackKing: Piece

    @BeforeEach
    fun setUp() {
        closestPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
        blockedPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
        whiteRook = Piece(Player.WHITE, RookPieceRules(Player.WHITE))
        whiteKing = Piece(Player.WHITE, KingPieceRules(Player.WHITE))
        blackKing = Piece(Player.BLACK, KingPieceRules(Player.BLACK))

        list = listOf("b4" to closestPawn, "b6" to blockedPawn, "b2" to whiteRook, "h1" to whiteKing, "h8" to blackKing)
        board = HashGameBoard.build(RectangleBoardValidator(8, 8), list, "h1", "h8")
        game = Game(NoRules(), board)
    }

    @Test
    fun `rook takes closest pawn`() {
        game.movePiece("b2", "b4")

        assertEquals(whiteRook.rules.javaClass, game.gameBoard.getPieceAt("b4")!!.rules.javaClass)
        assertEquals(whiteRook.player, game.gameBoard.getPieceAt("b4")!!.player)
        assertEquals(null, game.gameBoard.getPieceAt("b2"))
        assertEquals(blockedPawn, game.gameBoard.getPieceAt("b6"))
    }

    @Test
    fun `rook cannot take blocked pawn`() {
        assertThrows<IllegalArgumentException> { game.movePiece("b2", "b6") }

        assertEquals(whiteRook, game.gameBoard.getPieceAt("b2"))
        assertEquals(closestPawn, game.gameBoard.getPieceAt("b4"))
        assertEquals(blockedPawn, game.gameBoard.getPieceAt("b6"))
    }

    @Test
    fun `rook can move back`() {
        game.movePiece("b2", "b1")

        assertEquals(whiteRook.rules.javaClass, game.gameBoard.getPieceAt("b1")!!.rules.javaClass)
        assertEquals(whiteRook.player, game.gameBoard.getPieceAt("b1")!!.player)
        assertEquals(null, game.gameBoard.getPieceAt("b2"))
        assertEquals(closestPawn, game.gameBoard.getPieceAt("b4"))
        assertEquals(blockedPawn, game.gameBoard.getPieceAt("b6"))
    }

    @Test
    fun `rook can move right`() {
        game.movePiece("b2", "a2")

        assertEquals(whiteRook.rules.javaClass, game.gameBoard.getPieceAt("a2")!!.rules.javaClass)
        assertEquals(whiteRook.player, game.gameBoard.getPieceAt("a2")!!.player)
        assertEquals(null, game.gameBoard.getPieceAt("b2"))
        assertEquals(closestPawn, game.gameBoard.getPieceAt("b4"))
        assertEquals(blockedPawn, game.gameBoard.getPieceAt("b6"))
    }

    @Test
    fun `rook can move left`() {
        game.movePiece("b2", "h2")

        assertEquals(whiteRook.rules.javaClass, game.gameBoard.getPieceAt("h2")!!.rules.javaClass)
        assertEquals(whiteRook.player, game.gameBoard.getPieceAt("h2")!!.player)
        assertEquals(null, game.gameBoard.getPieceAt("b2"))
        assertEquals(closestPawn, game.gameBoard.getPieceAt("b4"))
        assertEquals(blockedPawn, game.gameBoard.getPieceAt("b6"))
    }

    @Test
    fun `rook cannot move diagonally`() {
        assertThrows<IllegalArgumentException> { game.movePiece("b2", "d4") }

        assertEquals(whiteRook, game.gameBoard.getPieceAt("b2"))
        assertEquals(null, game.gameBoard.getPieceAt("d4"))
        assertEquals(closestPawn, game.gameBoard.getPieceAt("b4"))
        assertEquals(blockedPawn, game.gameBoard.getPieceAt("b6"))
    }
}

class BishopMovementTest {
    private lateinit var list: List<Pair<String, Piece>>
    private lateinit var board: HashGameBoard
    private lateinit var game: Game
    private lateinit var closestPawn: Piece
    private lateinit var blockedPawn: Piece
    private lateinit var whiteBishop: Piece
    private lateinit var whiteKing: Piece
    private lateinit var blackKing: Piece

    @BeforeEach
    fun setUp() {
        closestPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
        blockedPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
        whiteBishop = Piece(Player.WHITE, BishopPieceRules())
        whiteKing = Piece(Player.WHITE, KingPieceRules(Player.WHITE))
        blackKing = Piece(Player.BLACK, KingPieceRules(Player.BLACK))

        list = listOf("d4" to closestPawn, "c3" to blockedPawn, "g7" to whiteBishop, "h1" to whiteKing, "a8" to blackKing)
        board = HashGameBoard.build(RectangleBoardValidator(8, 8), list, "h1", "a8")
        game = Game(NoRules(), board)
    }

    @Test
    fun `bishop takes closest pawn`() {
        game.movePiece("g7", "d4")

        assertEquals(whiteBishop, game.gameBoard.getPieceAt("d4"))
        assertEquals(null, game.gameBoard.getPieceAt("g7"))
        assertEquals(blockedPawn, game.gameBoard.getPieceAt("c3"))
    }

    @Test
    fun `bishop cannot take blocked pawn`() {
        assertThrows<IllegalArgumentException> { game.movePiece("g7", "c3") }

        assertEquals(whiteBishop, game.gameBoard.getPieceAt("g7"))
        assertEquals(closestPawn, game.gameBoard.getPieceAt("d4"))
        assertEquals(blockedPawn, game.gameBoard.getPieceAt("c3"))
    }

    @Test
    fun `bishop can move to corner`() {
        game.movePiece("g7", "h8")

        assertEquals(whiteBishop, game.gameBoard.getPieceAt("h8"))
        assertEquals(null, game.gameBoard.getPieceAt("g7"))
        assertEquals(closestPawn, game.gameBoard.getPieceAt("d4"))
        assertEquals(blockedPawn, game.gameBoard.getPieceAt("c3"))
    }

    @Test
    fun `bishop can move to the front and to the right`() {
        game.movePiece("g7", "f8")

        assertEquals(whiteBishop, game.gameBoard.getPieceAt("f8"))
        assertEquals(null, game.gameBoard.getPieceAt("g7"))
        assertEquals(closestPawn, game.gameBoard.getPieceAt("d4"))
        assertEquals(blockedPawn, game.gameBoard.getPieceAt("c3"))
    }

    @Test
    fun `bishop can move close to the first pawn`() {
        game.movePiece("g7", "e5")

        assertEquals(whiteBishop, game.gameBoard.getPieceAt("e5"))
        assertEquals(null, game.gameBoard.getPieceAt("g7"))
        assertEquals(closestPawn, game.gameBoard.getPieceAt("d4"))
        assertEquals(blockedPawn, game.gameBoard.getPieceAt("c3"))
    }

    @Test
    fun `bishop cannot move vertically`() {
        assertThrows<IllegalArgumentException> { game.movePiece("g7", "g2") }

        assertEquals(whiteBishop, game.gameBoard.getPieceAt("g7"))
        assertEquals(null, game.gameBoard.getPieceAt("g2"))
        assertEquals(closestPawn, game.gameBoard.getPieceAt("d4"))
        assertEquals(blockedPawn, game.gameBoard.getPieceAt("c3"))
    }
}

class QueenMovementTest {
    private lateinit var list: List<Pair<String, Piece>>
    private lateinit var board: HashGameBoard
    private lateinit var game: Game
    private lateinit var horizontalPawn: Piece
    private lateinit var unreachablePawn: Piece
    private lateinit var diagonalPawn: Piece
    private lateinit var whiteQueen: Piece
    private lateinit var whiteKing: Piece
    private lateinit var blackKing: Piece

    @BeforeEach
    fun setUp() {
        horizontalPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
        unreachablePawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
        diagonalPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
        whiteQueen = Piece(Player.WHITE, QueenPieceRules())
        whiteKing = Piece(Player.WHITE, KingPieceRules(Player.WHITE))
        blackKing = Piece(Player.BLACK, KingPieceRules(Player.BLACK))

        list =
            listOf(
                "b6" to horizontalPawn,
                "b5" to unreachablePawn,
                "b4" to diagonalPawn,
                "d6" to whiteQueen,
                "h1" to whiteKing,
                "h8" to blackKing,
            )
        board = HashGameBoard.build(RectangleBoardValidator(8, 8), list, "h1", "h8")
        game = Game(NoRules(), board)
    }

    @Test
    fun `queen takes pawn horizontally`() {
        game.movePiece("d6", "b6")

        assertEquals(whiteQueen, game.gameBoard.getPieceAt("b6"))
        assertEquals(null, game.gameBoard.getPieceAt("d6"))
        assertEquals(unreachablePawn, game.gameBoard.getPieceAt("b5"))
        assertEquals(diagonalPawn, game.gameBoard.getPieceAt("b4"))
    }

    @Test
    fun `queen cannot take unreachable pawn`() {
        assertThrows<IllegalArgumentException> { game.movePiece("d6", "b5") }

        assertEquals(whiteQueen, game.gameBoard.getPieceAt("d6"))
        assertEquals(horizontalPawn, game.gameBoard.getPieceAt("b6"))
        assertEquals(unreachablePawn, game.gameBoard.getPieceAt("b5"))
        assertEquals(diagonalPawn, game.gameBoard.getPieceAt("b4"))
    }

    @Test
    fun `queen takes pawn diagonally`() {
        game.movePiece("d6", "b4")

        assertEquals(whiteQueen, game.gameBoard.getPieceAt("b4"))
        assertEquals(null, game.gameBoard.getPieceAt("d6"))
        assertEquals(unreachablePawn, game.gameBoard.getPieceAt("b5"))
        assertEquals(horizontalPawn, game.gameBoard.getPieceAt("b6"))
    }

    @Test
    fun `queen can move back`() {
        game.movePiece("d6", "d1")

        assertEquals(whiteQueen, game.gameBoard.getPieceAt("d1"))
        assertEquals(null, game.gameBoard.getPieceAt("d6"))
        assertEquals(horizontalPawn, game.gameBoard.getPieceAt("b6"))
        assertEquals(unreachablePawn, game.gameBoard.getPieceAt("b5"))
        assertEquals(diagonalPawn, game.gameBoard.getPieceAt("b4"))
    }

    @Test
    fun `queen can move to the front and to the left`() {
        game.movePiece("d6", "e7")

        assertEquals(whiteQueen, game.gameBoard.getPieceAt("e7"))
        assertEquals(null, game.gameBoard.getPieceAt("d6"))
        assertEquals(horizontalPawn, game.gameBoard.getPieceAt("b6"))
        assertEquals(unreachablePawn, game.gameBoard.getPieceAt("b5"))
        assertEquals(diagonalPawn, game.gameBoard.getPieceAt("b4"))
    }

    @Test
    fun `queen cannot jump pawn diagonally`() {
        assertThrows<IllegalArgumentException> { game.movePiece("d6", "a3") }

        assertEquals(whiteQueen, game.gameBoard.getPieceAt("d6"))
        assertEquals(null, game.gameBoard.getPieceAt("a3"))
        assertEquals(horizontalPawn, game.gameBoard.getPieceAt("b6"))
        assertEquals(unreachablePawn, game.gameBoard.getPieceAt("b5"))
        assertEquals(diagonalPawn, game.gameBoard.getPieceAt("b4"))
    }

    @Test
    fun `queen cannot move like knight`() {
        assertThrows<IllegalArgumentException> { game.movePiece("d6", "e4") }

        assertEquals(whiteQueen, game.gameBoard.getPieceAt("d6"))
        assertEquals(null, game.gameBoard.getPieceAt("e4"))
        assertEquals(horizontalPawn, game.gameBoard.getPieceAt("b6"))
        assertEquals(unreachablePawn, game.gameBoard.getPieceAt("b5"))
        assertEquals(diagonalPawn, game.gameBoard.getPieceAt("b4"))
    }
}

class KnightMovementTest {
    private lateinit var list: List<Pair<String, Piece>>
    private lateinit var board: HashGameBoard
    private lateinit var game: Game
    private lateinit var horizontalPawn: Piece
    private lateinit var verticalPawn: Piece
    private lateinit var takeablePawn: Piece
    private lateinit var whiteKnight: Piece
    private lateinit var whiteKing: Piece
    private lateinit var blackKing: Piece

    @BeforeEach
    fun setUp() {
        horizontalPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
        takeablePawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
        verticalPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
        whiteKnight = Piece(Player.WHITE, KnightPieceRules())
        whiteKing = Piece(Player.WHITE, KingPieceRules(Player.WHITE))
        blackKing = Piece(Player.BLACK, KingPieceRules(Player.BLACK))

        list =
            listOf(
                "f3" to horizontalPawn,
                "f5" to takeablePawn,
                "g4" to verticalPawn,
                "g3" to whiteKnight,
                "a1" to whiteKing,
                "h8" to blackKing,
            )
        board = HashGameBoard.build(RectangleBoardValidator(8, 8), list, "a1", "h8")
        game = Game(NoRules(), board)
    }

    @Test
    fun `knight takes pawn moving in an L shape`() {
        game.movePiece("g3", "f5")

        assertEquals(whiteKnight, game.gameBoard.getPieceAt("f5"))
        assertEquals(null, game.gameBoard.getPieceAt("g3"))
        assertEquals(horizontalPawn, game.gameBoard.getPieceAt("f3"))
        assertEquals(verticalPawn, game.gameBoard.getPieceAt("g4"))
    }

    @Test
    fun `knight cannot take pawn horizontally`() {
        assertThrows<IllegalArgumentException> { game.movePiece("g3", "f3") }

        assertEquals(whiteKnight, game.gameBoard.getPieceAt("g3"))
        assertEquals(verticalPawn, game.gameBoard.getPieceAt("g4"))
        assertEquals(takeablePawn, game.gameBoard.getPieceAt("f5"))
        assertEquals(horizontalPawn, game.gameBoard.getPieceAt("f3"))
    }

    @Test
    fun `knight cannot take pawn vertically`() {
        assertThrows<IllegalArgumentException> { game.movePiece("g3", "g4") }

        assertEquals(whiteKnight, game.gameBoard.getPieceAt("g3"))
        assertEquals(verticalPawn, game.gameBoard.getPieceAt("g4"))
        assertEquals(takeablePawn, game.gameBoard.getPieceAt("f5"))
        assertEquals(horizontalPawn, game.gameBoard.getPieceAt("f3"))
    }

    @Test
    fun `knight can jump over pawn in L shape`() {
        game.movePiece("g3", "e4")

        assertEquals(whiteKnight, game.gameBoard.getPieceAt("e4"))
        assertEquals(null, game.gameBoard.getPieceAt("g3"))
        assertEquals(verticalPawn, game.gameBoard.getPieceAt("g4"))
        assertEquals(takeablePawn, game.gameBoard.getPieceAt("f5"))
        assertEquals(horizontalPawn, game.gameBoard.getPieceAt("f3"))
    }

    @Test
    fun `knight cannot jump over pawn horizontally`() {
        assertThrows<IllegalArgumentException> { game.movePiece("g3", "e3") }

        assertEquals(whiteKnight, game.gameBoard.getPieceAt("g3"))
        assertEquals(null, game.gameBoard.getPieceAt("e3"))
        assertEquals(verticalPawn, game.gameBoard.getPieceAt("g4"))
        assertEquals(takeablePawn, game.gameBoard.getPieceAt("f5"))
        assertEquals(horizontalPawn, game.gameBoard.getPieceAt("f3"))
    }
}

class KingMovementTest {
    private lateinit var list: List<Pair<String, Piece>>
    private lateinit var board: HashGameBoard
    private lateinit var game: Game
    private lateinit var blackRook: Piece
    private lateinit var whiteKing: Piece
    private lateinit var blackKing: Piece

    @BeforeEach
    fun setUp() {
        blackRook = Piece(Player.BLACK, RookPieceRules(Player.BLACK))
        whiteKing = Piece(Player.WHITE, KingPieceRules(Player.WHITE))
        blackKing = Piece(Player.BLACK, KingPieceRules(Player.BLACK))

        list = listOf("b8" to blackRook, "c3" to whiteKing, "f6" to blackKing)
        board = HashGameBoard.build(RectangleBoardValidator(8, 8), list, "c3", "f6")
        game = Game(NoRules(), board)
    }

    @Test
    fun `black king cannot move two squares`() {
        assertThrows<IllegalArgumentException> { game.movePiece("f6", "f4") }

        assertEquals(blackKing, game.gameBoard.getPieceAt("f6"))
        assertEquals(null, game.gameBoard.getPieceAt("f4"))
        assertEquals(whiteKing, game.gameBoard.getPieceAt("c3"))
        assertEquals(blackRook, game.gameBoard.getPieceAt("b8"))
    }

    @Test
    fun `black king can move left`() {
        game.movePiece("f6", "e6")

        assertEquals(blackKing, game.gameBoard.getPieceAt("e6"))
        assertEquals(null, game.gameBoard.getPieceAt("f6"))
        assertEquals(whiteKing, game.gameBoard.getPieceAt("c3"))
        assertEquals(blackRook, game.gameBoard.getPieceAt("b8"))
    }

    @Test
    fun `white king can move up and to the right`() {
        game.movePiece("c3", "d2")

        assertEquals(whiteKing, game.gameBoard.getPieceAt("d2"))
        assertEquals(null, game.gameBoard.getPieceAt("c3"))
        assertEquals(blackKing, game.gameBoard.getPieceAt("f6"))
        assertEquals(blackRook, game.gameBoard.getPieceAt("b8"))
    }

    @Test
    fun `black king is not checked by ally rook`() {
        game.movePiece("b8", "b6")

        assertEquals(blackRook.rules.javaClass, game.gameBoard.getPieceAt("b6")!!.rules.javaClass)
        assertEquals(blackRook.player, game.gameBoard.getPieceAt("b6")!!.player)
        assertEquals(blackKing, game.gameBoard.getPieceAt("f6"))
        assertEquals(false, KingPieceRules.isChecked(game.gameBoard, Player.BLACK))
    }

    @Test
    fun `white king is checked by enemy rook`() {
        game.movePiece("b8", "b3")

        assertEquals(blackRook.rules.javaClass, game.gameBoard.getPieceAt("b3")!!.rules.javaClass)
        assertEquals(blackRook.player, game.gameBoard.getPieceAt("b3")!!.player)
        assertEquals(whiteKing, game.gameBoard.getPieceAt("c3"))
        assertEquals(true, KingPieceRules.isChecked(game.gameBoard, Player.WHITE))
    }

    @Test
    fun `white king cannot move if it would become checked`() {
        assertThrows<IllegalArgumentException> { game.movePiece("c3", "b3") }

        assertEquals(whiteKing, game.gameBoard.getPieceAt("c3"))
        assertEquals(null, game.gameBoard.getPieceAt("b3"))
    }
}

class NoRules : GameRules {
    override fun isPieceMovable(position: String): Boolean {
        return true
    }

    override fun isMoveValid(
        from: String,
        to: String,
    ): Boolean {
        return true
    }

    override fun playerReachedWinCondition(player: Player): Boolean {
        return false
    }

    override fun playerIsChecked(player: Player): Boolean {
        return false
    }
}
