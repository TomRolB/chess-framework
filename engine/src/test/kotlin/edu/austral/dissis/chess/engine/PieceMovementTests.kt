//package edu.austral.dissis.chess.engine
//
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.assertThrows
//
//class PawnMovementTests {
//    // This class tests movements per se.
//    // We are not interested on the game,
//    // so we define classes such as NoRules.
//
//    private lateinit var list: List<Pair<Position, Piece>>
//    private lateinit var board: HashGameBoard
//    private lateinit var game: TestableGame
//    private lateinit var whitePawn: Piece
//    private lateinit var blackPawn: Piece
//    private lateinit var whiteKing: Piece
//    private lateinit var blackKing: Piece
//
//    @BeforeEach
//    fun setUp() {
//        whitePawn = Piece(Player.WHITE, PawnPieceRules(Player.WHITE))
//        blackPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
//        whiteKing = Piece(Player.WHITE, KingPieceRules(Player.WHITE, hasEverMoved = true))
//        blackKing = Piece(Player.BLACK, KingPieceRules(Player.BLACK, hasEverMoved = true))
//
//        list = listOf(Position(1, 4) to whitePawn, Position(2, 3) to blackPawn, Position(1, 8) to whiteKing, Position(8, 8) to blackKing)
//        board = HashGameBoard.build(RectangleBoardValidator(8, 8), list, Position(1, 8), Position(8, 8))
//        game = TestableGame(NoRules(), board, NoManager(), NoProvider())
//    }
//
//    @Test
//    fun `black pawn can take diagonally`() {
//        val whitePawn = Piece(Player.WHITE, PawnPieceRules(Player.WHITE))
//        val blackPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
//
//        game.board = game.board.setPieceAt(Position(1, 4), whitePawn)
//        game.board = game.board.setPieceAt(Position(2, 3), blackPawn)
//
//        game.movePiece(Position(2, 3), Position(1, 4))
//
//        assertEquals(null, game.board.getPieceAt(Position(2, 3)))
//        assertEquals(true, game.board.getPieceAt(Position(1, 4))!!.rules is PawnPieceRules)
//        assertEquals(Player.BLACK, game.board.getPieceAt(Position(1, 4))!!.player)
//    }
//
//    @Test
//    fun `white pawn can take diagonally`() {
//        game.movePiece(Position(1, 4), Position(2, 3))
//
//        assertEquals(null, game.board.getPieceAt(Position(1, 4)))
//        assertEquals(true, game.board.getPieceAt(Position(2, 3))!!.rules is PawnPieceRules)
//        assertEquals(Player.WHITE, game.board.getPieceAt(Position(2, 3))!!.player)
//    }
//
//    @Test
//    fun `white pawn cannot move anywhere`() {
//        assertThrows<IllegalArgumentException> { game.movePiece(Position(1, 4), Position(8, 1)) }
//
//        assertEquals(whitePawn, game.board.getPieceAt(Position(1, 4)))
//        assertEquals(null, game.board.getPieceAt(Position(8, 1)))
//        assertEquals(blackPawn, game.board.getPieceAt(Position(2, 3)))
//    }
//
//    @Test
//    fun `white pawn can move ahead`() {
//        game.movePiece(Position(1, 4), Position(2, 4))
//
//        assertEquals(true, game.board.getPieceAt(Position(2, 4))!!.rules is PawnPieceRules)
//        assertEquals(Player.WHITE, game.board.getPieceAt(Position(2, 4))!!.player)
//        assertEquals(null, game.board.getPieceAt(Position(1, 4)))
//        assertEquals(blackPawn, game.board.getPieceAt(Position(2, 3)))
//    }
//
//    @Test
//    fun `white pawn cannot move to the other diagonal`() {
//        assertThrows<IllegalArgumentException> { game.movePiece(Position(1, 4), Position(2, 5)) }
//
//        assertEquals(whitePawn, game.board.getPieceAt(Position(1, 4)))
//        assertEquals(null, game.board.getPieceAt(Position(2, 5)))
//        assertEquals(blackPawn, game.board.getPieceAt(Position(2, 3)))
//    }
//
//    @Test
//    fun `white moves two spaces but then cannot again`() {
//        game.movePiece(Position(1, 4), Position(3, 4))
//
//        assertEquals(whitePawn.rules.javaClass, game.board.getPieceAt(Position(3, 4))!!.rules.javaClass)
//        assertEquals(whitePawn.player, game.board.getPieceAt(Position(3, 4))!!.player)
//        assertEquals(null, game.board.getPieceAt(Position(1, 4)))
//        assertEquals(blackPawn, game.board.getPieceAt(Position(2, 3)))
//
//        assertThrows<IllegalArgumentException> { game.movePiece(Position(3, 4), Position(5, 4)) }
//
//        assertEquals(whitePawn.rules.javaClass, game.board.getPieceAt(Position(3, 4))!!.rules.javaClass)
//        assertEquals(whitePawn.player, game.board.getPieceAt(Position(3, 4))!!.player)
//        assertEquals(null, game.board.getPieceAt(Position(5, 4)))
//        assertEquals(blackPawn, game.board.getPieceAt(Position(2, 3)))
//    }
//}
//
//class RookMovementTest {
//    private lateinit var list: List<Pair<Position, Piece>>
//    private lateinit var board: HashGameBoard
//    private lateinit var game: TestableGame
//    private lateinit var closestPawn: Piece
//    private lateinit var blockedPawn: Piece
//    private lateinit var whiteRook: Piece
//    private lateinit var whiteKing: Piece
//    private lateinit var blackKing: Piece
//
//    @BeforeEach
//    fun setUp() {
//        closestPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
//        blockedPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
//        whiteRook = Piece(Player.WHITE, RookPieceRules(Player.WHITE))
//        whiteKing = Piece(Player.WHITE, KingPieceRules(Player.WHITE, hasEverMoved = true))
//        blackKing = Piece(Player.BLACK, KingPieceRules(Player.BLACK, hasEverMoved = true))
//
//        list = listOf(Position(4, 2) to closestPawn, Position(6, 2) to blockedPawn, Position(2, 2) to whiteRook, Position(1, 8) to whiteKing, Position(8, 8) to blackKing)
//        board = HashGameBoard.build(RectangleBoardValidator(8, 8), list, Position(1, 8), Position(8, 8))
//        game = TestableGame(NoRules(), board, NoManager(), NoProvider())
//    }
//
//    @Test
//    fun `rook takes closest pawn`() {
//        game.movePiece(Position(2, 2), Position(4, 2))
//
//        assertEquals(whiteRook.rules.javaClass, game.board.getPieceAt(Position(4, 2))!!.rules.javaClass)
//        assertEquals(whiteRook.player, game.board.getPieceAt(Position(4, 2))!!.player)
//        assertEquals(null, game.board.getPieceAt(Position(2, 2)))
//        assertEquals(blockedPawn, game.board.getPieceAt(Position(6, 2)))
//    }
//
//    @Test
//    fun `rook cannot take blocked pawn`() {
//        assertThrows<IllegalArgumentException> { game.movePiece(Position(2, 2), Position(6, 2)) }
//
//        assertEquals(whiteRook, game.board.getPieceAt(Position(2, 2)))
//        assertEquals(closestPawn, game.board.getPieceAt(Position(4, 2)))
//        assertEquals(blockedPawn, game.board.getPieceAt(Position(6, 2)))
//    }
//
//    @Test
//    fun `rook can move back`() {
//        game.movePiece(Position(2, 2), Position(1, 2))
//
//        assertEquals(whiteRook.rules.javaClass, game.board.getPieceAt(Position(1, 2))!!.rules.javaClass)
//        assertEquals(whiteRook.player, game.board.getPieceAt(Position(1, 2))!!.player)
//        assertEquals(null, game.board.getPieceAt(Position(2, 2)))
//        assertEquals(closestPawn, game.board.getPieceAt(Position(4, 2)))
//        assertEquals(blockedPawn, game.board.getPieceAt(Position(6, 2)))
//    }
//
//    @Test
//    fun `rook can move right`() {
//        game.movePiece(Position(2, 2), Position(2, 1))
//
//        assertEquals(whiteRook.rules.javaClass, game.board.getPieceAt(Position(2, 1))!!.rules.javaClass)
//        assertEquals(whiteRook.player, game.board.getPieceAt(Position(2, 1))!!.player)
//        assertEquals(null, game.board.getPieceAt(Position(2, 2)))
//        assertEquals(closestPawn, game.board.getPieceAt(Position(4, 2)))
//        assertEquals(blockedPawn, game.board.getPieceAt(Position(6, 2)))
//    }
//
//    @Test
//    fun `rook can move left`() {
//        game.movePiece(Position(2, 2), Position(2, 8))
//
//        assertEquals(whiteRook.rules.javaClass, game.board.getPieceAt(Position(2, 8))!!.rules.javaClass)
//        assertEquals(whiteRook.player, game.board.getPieceAt(Position(2, 8))!!.player)
//        assertEquals(null, game.board.getPieceAt(Position(2, 2)))
//        assertEquals(closestPawn, game.board.getPieceAt(Position(4, 2)))
//        assertEquals(blockedPawn, game.board.getPieceAt(Position(6, 2)))
//    }
//
//    @Test
//    fun `rook cannot move diagonally`() {
//        assertThrows<IllegalArgumentException> { game.movePiece(Position(2, 2), Position(4, 4)) }
//
//        assertEquals(whiteRook, game.board.getPieceAt(Position(2, 2)))
//        assertEquals(null, game.board.getPieceAt(Position(4, 4)))
//        assertEquals(closestPawn, game.board.getPieceAt(Position(4, 2)))
//        assertEquals(blockedPawn, game.board.getPieceAt(Position(6, 2)))
//    }
//}
//
//class BishopMovementTest {
//    private lateinit var list: List<Pair<Position, Piece>>
//    private lateinit var board: HashGameBoard
//    private lateinit var game: TestableGame
//    private lateinit var closestPawn: Piece
//    private lateinit var blockedPawn: Piece
//    private lateinit var whiteBishop: Piece
//    private lateinit var whiteKing: Piece
//    private lateinit var blackKing: Piece
//
//    @BeforeEach
//    fun setUp() {
//        closestPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
//        blockedPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
//        whiteBishop = Piece(Player.WHITE, BishopPieceRules(Player.WHITE))
//        whiteKing = Piece(Player.WHITE, KingPieceRules(Player.WHITE, hasEverMoved = true))
//        blackKing = Piece(Player.BLACK, KingPieceRules(Player.BLACK, hasEverMoved = true))
//
//        list = listOf(Position(4, 4) to closestPawn, Position(3, 3) to blockedPawn, Position(7, 7) to whiteBishop, Position(1, 8) to whiteKing, Position(8, 1) to blackKing)
//        board = HashGameBoard.build(RectangleBoardValidator(8, 8), list, Position(1, 8), Position(8, 1))
//        game = TestableGame(NoRules(), board, NoManager(), NoProvider())
//    }
//
//    @Test
//    fun `bishop takes closest pawn`() {
//        game.movePiece(Position(7, 7), Position(4, 4))
//
//        assertEquals(whiteBishop, game.board.getPieceAt(Position(4, 4)))
//        assertEquals(null, game.board.getPieceAt(Position(7, 7)))
//        assertEquals(blockedPawn, game.board.getPieceAt(Position(3, 3)))
//    }
//
//    @Test
//    fun `bishop cannot take blocked pawn`() {
//        assertThrows<IllegalArgumentException> { game.movePiece(Position(7, 7), Position(3, 3)) }
//
//        assertEquals(whiteBishop, game.board.getPieceAt(Position(7, 7)))
//        assertEquals(closestPawn, game.board.getPieceAt(Position(4, 4)))
//        assertEquals(blockedPawn, game.board.getPieceAt(Position(3, 3)))
//    }
//
//    @Test
//    fun `bishop can move to corner`() {
//        game.movePiece(Position(7, 7), Position(8, 8))
//
//        assertEquals(whiteBishop, game.board.getPieceAt(Position(8, 8)))
//        assertEquals(null, game.board.getPieceAt(Position(7, 7)))
//        assertEquals(closestPawn, game.board.getPieceAt(Position(4, 4)))
//        assertEquals(blockedPawn, game.board.getPieceAt(Position(3, 3)))
//    }
//
//    @Test
//    fun `bishop can move to the front and to the right`() {
//        game.movePiece(Position(7, 7), Position(8, 6))
//
//        assertEquals(whiteBishop, game.board.getPieceAt(Position(8, 6)))
//        assertEquals(null, game.board.getPieceAt(Position(7, 7)))
//        assertEquals(closestPawn, game.board.getPieceAt(Position(4, 4)))
//        assertEquals(blockedPawn, game.board.getPieceAt(Position(3, 3)))
//    }
//
//    @Test
//    fun `bishop can move close to the first pawn`() {
//        game.movePiece(Position(7, 7), Position(5, 5))
//
//        assertEquals(whiteBishop, game.board.getPieceAt(Position(5, 5)))
//        assertEquals(null, game.board.getPieceAt(Position(7, 7)))
//        assertEquals(closestPawn, game.board.getPieceAt(Position(4, 4)))
//        assertEquals(blockedPawn, game.board.getPieceAt(Position(3, 3)))
//    }
//
//    @Test
//    fun `bishop cannot move vertically`() {
//        assertThrows<IllegalArgumentException> { game.movePiece(Position(7, 7), Position(2, 7)) }
//
//        assertEquals(whiteBishop, game.board.getPieceAt(Position(7, 7)))
//        assertEquals(null, game.board.getPieceAt(Position(2, 7)))
//        assertEquals(closestPawn, game.board.getPieceAt(Position(4, 4)))
//        assertEquals(blockedPawn, game.board.getPieceAt(Position(3, 3)))
//    }
//}
//
//class QueenMovementTest {
//    private lateinit var list: List<Pair<Position, Piece>>
//    private lateinit var board: HashGameBoard
//    private lateinit var game: TestableGame
//    private lateinit var horizontalPawn: Piece
//    private lateinit var unreachablePawn: Piece
//    private lateinit var diagonalPawn: Piece
//    private lateinit var whiteQueen: Piece
//    private lateinit var whiteKing: Piece
//    private lateinit var blackKing: Piece
//
//    @BeforeEach
//    fun setUp() {
//        horizontalPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
//        unreachablePawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
//        diagonalPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
//        whiteQueen = Piece(Player.WHITE, QueenPieceRules(Player.WHITE))
//        whiteKing = Piece(Player.WHITE, KingPieceRules(Player.WHITE, hasEverMoved = true))
//        blackKing = Piece(Player.BLACK, KingPieceRules(Player.BLACK, hasEverMoved = true))
//
//        list =
//            listOf(
//                Position(6, 2) to horizontalPawn,
//                Position(5, 2) to unreachablePawn,
//                Position(4, 2) to diagonalPawn,
//                Position(6, 4) to whiteQueen,
//                Position(1, 8) to whiteKing,
//                Position(8, 8) to blackKing,
//            )
//        board = HashGameBoard.build(RectangleBoardValidator(8, 8), list, Position(1, 8), Position(8, 8))
//        game = TestableGame(NoRules(), board, NoManager(), NoProvider())
//    }
//
//    @Test
//    fun `queen takes pawn horizontally`() {
//        game.movePiece(Position(6, 4), Position(6, 2))
//
//        assertEquals(whiteQueen, game.board.getPieceAt(Position(6, 2)))
//        assertEquals(null, game.board.getPieceAt(Position(6, 4)))
//        assertEquals(unreachablePawn, game.board.getPieceAt(Position(5, 2)))
//        assertEquals(diagonalPawn, game.board.getPieceAt(Position(4, 2)))
//    }
//
//    @Test
//    fun `queen cannot take unreachable pawn`() {
//        assertThrows<IllegalArgumentException> { game.movePiece(Position(6, 4), Position(5, 2)) }
//
//        assertEquals(whiteQueen, game.board.getPieceAt(Position(6, 4)))
//        assertEquals(horizontalPawn, game.board.getPieceAt(Position(6, 2)))
//        assertEquals(unreachablePawn, game.board.getPieceAt(Position(5, 2)))
//        assertEquals(diagonalPawn, game.board.getPieceAt(Position(4, 2)))
//    }
//
//    @Test
//    fun `queen takes pawn diagonally`() {
//        game.movePiece(Position(6, 4), Position(4, 2))
//
//        assertEquals(whiteQueen, game.board.getPieceAt(Position(4, 2)))
//        assertEquals(null, game.board.getPieceAt(Position(6, 4)))
//        assertEquals(unreachablePawn, game.board.getPieceAt(Position(5, 2)))
//        assertEquals(horizontalPawn, game.board.getPieceAt(Position(6, 2)))
//    }
//
//    @Test
//    fun `queen can move back`() {
//        game.movePiece(Position(6, 4), Position(1, 4))
//
//        assertEquals(whiteQueen, game.board.getPieceAt(Position(1, 4)))
//        assertEquals(null, game.board.getPieceAt(Position(6, 4)))
//        assertEquals(horizontalPawn, game.board.getPieceAt(Position(6, 2)))
//        assertEquals(unreachablePawn, game.board.getPieceAt(Position(5, 2)))
//        assertEquals(diagonalPawn, game.board.getPieceAt(Position(4, 2)))
//    }
//
//    @Test
//    fun `queen can move to the front and to the left`() {
//        game.movePiece(Position(6, 4), Position(7, 5))
//
//        assertEquals(whiteQueen, game.board.getPieceAt(Position(7, 5)))
//        assertEquals(null, game.board.getPieceAt(Position(6, 4)))
//        assertEquals(horizontalPawn, game.board.getPieceAt(Position(6, 2)))
//        assertEquals(unreachablePawn, game.board.getPieceAt(Position(5, 2)))
//        assertEquals(diagonalPawn, game.board.getPieceAt(Position(4, 2)))
//    }
//
//    @Test
//    fun `queen cannot jump pawn diagonally`() {
//        assertThrows<IllegalArgumentException> { game.movePiece(Position(6, 4), Position(3, 1)) }
//
//        assertEquals(whiteQueen, game.board.getPieceAt(Position(6, 4)))
//        assertEquals(null, game.board.getPieceAt(Position(3, 1)))
//        assertEquals(horizontalPawn, game.board.getPieceAt(Position(6, 2)))
//        assertEquals(unreachablePawn, game.board.getPieceAt(Position(5, 2)))
//        assertEquals(diagonalPawn, game.board.getPieceAt(Position(4, 2)))
//    }
//
//    @Test
//    fun `queen cannot move like knight`() {
//        assertThrows<IllegalArgumentException> { game.movePiece(Position(6, 4), Position(4, 5)) }
//
//        assertEquals(whiteQueen, game.board.getPieceAt(Position(6, 4)))
//        assertEquals(null, game.board.getPieceAt(Position(4, 5)))
//        assertEquals(horizontalPawn, game.board.getPieceAt(Position(6, 2)))
//        assertEquals(unreachablePawn, game.board.getPieceAt(Position(5, 2)))
//        assertEquals(diagonalPawn, game.board.getPieceAt(Position(4, 2)))
//    }
//}
//
//class KnightMovementTest {
//    private lateinit var list: List<Pair<Position, Piece>>
//    private lateinit var board: HashGameBoard
//    private lateinit var game: TestableGame
//    private lateinit var horizontalPawn: Piece
//    private lateinit var verticalPawn: Piece
//    private lateinit var takeablePawn: Piece
//    private lateinit var whiteKnight: Piece
//    private lateinit var whiteKing: Piece
//    private lateinit var blackKing: Piece
//
//    @BeforeEach
//    fun setUp() {
//        horizontalPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
//        takeablePawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
//        verticalPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
//        whiteKnight = Piece(Player.WHITE, KnightPieceRules(Player.WHITE))
//        whiteKing = Piece(Player.WHITE, KingPieceRules(Player.WHITE, hasEverMoved = true))
//        blackKing = Piece(Player.BLACK, KingPieceRules(Player.BLACK, hasEverMoved = true))
//
//        list =
//            listOf(
//                Position(3, 6) to horizontalPawn,
//                Position(5, 6) to takeablePawn,
//                Position(4, 7) to verticalPawn,
//                Position(3, 7) to whiteKnight,
//                Position(1, 1) to whiteKing,
//                Position(8, 8) to blackKing,
//            )
//        board = HashGameBoard.build(RectangleBoardValidator(8, 8), list, Position(1, 1), Position(8, 8))
//        game = TestableGame(NoRules(), board, NoManager(), NoProvider())
//    }
//
//    @Test
//    fun `knight takes pawn moving in an L shape`() {
//        game.movePiece(Position(3, 7), Position(5, 6))
//
//        assertEquals(whiteKnight, game.board.getPieceAt(Position(5, 6)))
//        assertEquals(null, game.board.getPieceAt(Position(3, 7)))
//        assertEquals(horizontalPawn, game.board.getPieceAt(Position(3, 6)))
//        assertEquals(verticalPawn, game.board.getPieceAt(Position(4, 7)))
//    }
//
//    @Test
//    fun `knight cannot take pawn horizontally`() {
//        assertThrows<IllegalArgumentException> { game.movePiece(Position(3, 7), Position(3, 6)) }
//
//        assertEquals(whiteKnight, game.board.getPieceAt(Position(3, 7)))
//        assertEquals(verticalPawn, game.board.getPieceAt(Position(4, 7)))
//        assertEquals(takeablePawn, game.board.getPieceAt(Position(5, 6)))
//        assertEquals(horizontalPawn, game.board.getPieceAt(Position(3, 6)))
//    }
//
//    @Test
//    fun `knight cannot take pawn vertically`() {
//        assertThrows<IllegalArgumentException> { game.movePiece(Position(3, 7), Position(4, 7)) }
//
//        assertEquals(whiteKnight, game.board.getPieceAt(Position(3, 7)))
//        assertEquals(verticalPawn, game.board.getPieceAt(Position(4, 7)))
//        assertEquals(takeablePawn, game.board.getPieceAt(Position(5, 6)))
//        assertEquals(horizontalPawn, game.board.getPieceAt(Position(3, 6)))
//    }
//
//    @Test
//    fun `knight can jump over pawn in L shape`() {
//        game.movePiece(Position(3, 7), Position(4, 5))
//
//        assertEquals(whiteKnight, game.board.getPieceAt(Position(4, 5)))
//        assertEquals(null, game.board.getPieceAt(Position(3, 7)))
//        assertEquals(verticalPawn, game.board.getPieceAt(Position(4, 7)))
//        assertEquals(takeablePawn, game.board.getPieceAt(Position(5, 6)))
//        assertEquals(horizontalPawn, game.board.getPieceAt(Position(3, 6)))
//    }
//
//    @Test
//    fun `knight cannot jump over pawn horizontally`() {
//        assertThrows<IllegalArgumentException> { game.movePiece(Position(3, 7), Position(3, 5)) }
//
//        assertEquals(whiteKnight, game.board.getPieceAt(Position(3, 7)))
//        assertEquals(null, game.board.getPieceAt(Position(3, 5)))
//        assertEquals(verticalPawn, game.board.getPieceAt(Position(4, 7)))
//        assertEquals(takeablePawn, game.board.getPieceAt(Position(5, 6)))
//        assertEquals(horizontalPawn, game.board.getPieceAt(Position(3, 6)))
//    }
//}
//
//class KingMovementTest {
//    private lateinit var list: List<Pair<Position, Piece>>
//    private lateinit var board: HashGameBoard
//    private lateinit var game: TestableGame
//    private lateinit var blackRook: Piece
//    private lateinit var whiteKing: Piece
//    private lateinit var blackKing: Piece
//
//    @BeforeEach
//    fun setUp() {
//        blackRook = Piece(Player.BLACK, RookPieceRules(Player.BLACK))
//        whiteKing = Piece(Player.WHITE, KingPieceRules(Player.WHITE, hasEverMoved = true))
//        blackKing = Piece(Player.BLACK, KingPieceRules(Player.BLACK, hasEverMoved = true))
//
//        list = listOf(Position(8, 2) to blackRook, Position(3, 3) to whiteKing, Position(6, 6) to blackKing)
//        board = HashGameBoard.build(RectangleBoardValidator(8, 8), list, Position(3, 3), Position(6, 6))
//        game = TestableGame(NoRules(), board, NoManager(), NoProvider())
//    }
//
//    @Test
//    fun `black king cannot move two squares`() {
//        assertThrows<IllegalArgumentException> { game.movePiece(Position(6, 6), Position(4, 6)) }
//
//        assertEquals(blackKing, game.board.getPieceAt(Position(6, 6)))
//        assertEquals(null, game.board.getPieceAt(Position(4, 6)))
//        assertEquals(whiteKing, game.board.getPieceAt(Position(3, 3)))
//        assertEquals(blackRook, game.board.getPieceAt(Position(8, 2)))
//    }
//
//    @Test
//    fun `black king can move left`() {
//        game.movePiece(Position(6, 6), Position(6, 5))
//
//        assertEquals(blackKing, game.board.getPieceAt(Position(6, 5)))
//        assertEquals(null, game.board.getPieceAt(Position(6, 6)))
//        assertEquals(whiteKing, game.board.getPieceAt(Position(3, 3)))
//        assertEquals(blackRook, game.board.getPieceAt(Position(8, 2)))
//    }
//
//    @Test
//    fun `white king can move up and to the right`() {
//        game.movePiece(Position(3, 3), Position(2, 4))
//
//        assertEquals(whiteKing, game.board.getPieceAt(Position(2, 4)))
//        assertEquals(null, game.board.getPieceAt(Position(3, 3)))
//        assertEquals(blackKing, game.board.getPieceAt(Position(6, 6)))
//        assertEquals(blackRook, game.board.getPieceAt(Position(8, 2)))
//    }
//
//    @Test
//    fun `black king is not checked by ally rook`() {
//        game.movePiece(Position(8, 2), Position(6, 2))
//
//        assertEquals(blackRook.rules.javaClass, game.board.getPieceAt(Position(6, 2))!!.rules.javaClass)
//        assertEquals(blackRook.player, game.board.getPieceAt(Position(6, 2))!!.player)
//        assertEquals(blackKing, game.board.getPieceAt(Position(6, 6)))
//        assertEquals(false, KingPieceRules.isChecked(game.board, Player.BLACK))
//    }
//
//    @Test
//    fun `white king is checked by enemy rook`() {
//        game.movePiece(Position(8, 2), Position(3, 2))
//
//        assertEquals(blackRook.rules.javaClass, game.board.getPieceAt(Position(3, 2))!!.rules.javaClass)
//        assertEquals(blackRook.player, game.board.getPieceAt(Position(3, 2))!!.player)
//        assertEquals(whiteKing, game.board.getPieceAt(Position(3, 3)))
//        assertEquals(true, KingPieceRules.isChecked(game.board, Player.WHITE))
//    }
//
//
//    // TODO: This test should be moved to SpecialMovementsTest, since it
//    //  would actually work by using game.run(), and in this case we
//    //  are using game.movePiece()
////    @Test
////    fun `white king cannot move if it would become checked`() {
////        assertThrows<IllegalArgumentException> { game.movePiece(Position(3, 3), Position(3, 2)) }
////
////        assertEquals(whiteKing, game.board.getPieceAt(Position(3, 3)))
////        assertEquals(null, game.board.getPieceAt(Position(3, 2)))
////
////        assertEquals(true, KingPieceRules.isChecked(game.board, Player.WHITE))
////    }
//}
//
//class NoRules : GameRules {
//    override fun isPieceMovable(position: Position): Boolean {
//        return true
//    }
//
//    override fun prePlayRules(board: GameBoard, player: Player, from: Position, to: Position): Boolean {
//        return true
//    }
//
//    override fun playerReachedWinCondition(player: Player, enemyState: PlayerState): Boolean {
//        return false
//    }
//
//    override fun wasTieReached(playerOnTurn: Player, enemyState: PlayerState): Boolean {
//        return false
//    }
//
//    override fun playerIsChecked(player: Player): Boolean {
//        return false
//    }
//
//    override fun postPlayRules(board: GameBoard, piece: Piece, finalPosition: Position): GameBoard {
//        return board
//    }
//}
//
//class NoManager: TurnManager {
//    override fun getTurn(): Player {
//        return Player.WHITE
//    }
//
//    override fun nextTurn(): TurnManager {
//        return this
//    }
//
//}
//
//class NoProvider : PlayerInputProvider {
//    override fun requestPlayerMove(player: Player): Pair<Position, Position> {
//        return Pair(Position(0, 0), Position(0, 1))
//    }
//
//    override fun requestPromotionPiece(player: Player): PieceRules {
//        return QueenPieceRules(player)
//    }
//}
