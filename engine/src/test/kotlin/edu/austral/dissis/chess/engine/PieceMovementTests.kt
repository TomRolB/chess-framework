package edu.austral.dissis.chess.engine

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PawnMovementTests {
    // This class tests movements per se.
    // We are not interested on the game,
    // so we define classes such as NoRules.

    private lateinit var list: List<Pair<String, Piece>>
    private lateinit var board: HashGameBoard
    private lateinit var game: TurnManagingGame
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
        game = TurnManagingGame(NoRules(), board, NoManager(), NoProvider())
    }

    @Test
    fun `black pawn can take diagonally`() {
        val whitePawn = Piece(Player.WHITE, PawnPieceRules(Player.WHITE))
        val blackPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))

        game.board = game.board.setPieceAt("d1", whitePawn)
        game.board = game.board.setPieceAt("c2", blackPawn)

        game.movePiece("c2", "d1")

        assertEquals(null, game.board.getPieceAt("c2"))
        assertEquals(true, game.board.getPieceAt("d1")!!.rules is PawnPieceRules)
        assertEquals(Player.BLACK, game.board.getPieceAt("d1")!!.player)
    }

    @Test
    fun `white pawn can take diagonally`() {
        game.movePiece("d1", "c2")

        assertEquals(null, game.board.getPieceAt("d1"))
        assertEquals(true, game.board.getPieceAt("c2")!!.rules is PawnPieceRules)
        assertEquals(Player.WHITE, game.board.getPieceAt("c2")!!.player)
    }

    @Test
    fun `white pawn cannot move anywhere`() {
        assertThrows<IllegalArgumentException> { game.movePiece("d1", "a8") }

        assertEquals(whitePawn, game.board.getPieceAt("d1"))
        assertEquals(null, game.board.getPieceAt("a8"))
        assertEquals(blackPawn, game.board.getPieceAt("c2"))
    }

    @Test
    fun `white pawn can move ahead`() {
        game.movePiece("d1", "d2")

        assertEquals(true, game.board.getPieceAt("d2")!!.rules is PawnPieceRules)
        assertEquals(Player.WHITE, game.board.getPieceAt("d2")!!.player)
        assertEquals(null, game.board.getPieceAt("d1"))
        assertEquals(blackPawn, game.board.getPieceAt("c2"))
    }

    @Test
    fun `white pawn cannot move to the other diagonal`() {
        assertThrows<IllegalArgumentException> { game.movePiece("d1", "e2") }

        assertEquals(whitePawn, game.board.getPieceAt("d1"))
        assertEquals(null, game.board.getPieceAt("e2"))
        assertEquals(blackPawn, game.board.getPieceAt("c2"))
    }

    @Test
    fun `white moves two spaces but then cannot again`() {
        game.movePiece("d1", "d3")

        assertEquals(whitePawn.rules.javaClass, game.board.getPieceAt("d3")!!.rules.javaClass)
        assertEquals(whitePawn.player, game.board.getPieceAt("d3")!!.player)
        assertEquals(null, game.board.getPieceAt("d1"))
        assertEquals(blackPawn, game.board.getPieceAt("c2"))

        assertThrows<IllegalArgumentException> { game.movePiece("d3", "d5") }

        assertEquals(whitePawn.rules.javaClass, game.board.getPieceAt("d3")!!.rules.javaClass)
        assertEquals(whitePawn.player, game.board.getPieceAt("d3")!!.player)
        assertEquals(null, game.board.getPieceAt("d5"))
        assertEquals(blackPawn, game.board.getPieceAt("c2"))
    }
}

class RookMovementTest {
    private lateinit var list: List<Pair<String, Piece>>
    private lateinit var board: HashGameBoard
    private lateinit var game: TurnManagingGame
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
        game = TurnManagingGame(NoRules(), board, NoManager(), NoProvider())
    }

    @Test
    fun `rook takes closest pawn`() {
        game.movePiece("b2", "b4")

        assertEquals(whiteRook.rules.javaClass, game.board.getPieceAt("b4")!!.rules.javaClass)
        assertEquals(whiteRook.player, game.board.getPieceAt("b4")!!.player)
        assertEquals(null, game.board.getPieceAt("b2"))
        assertEquals(blockedPawn, game.board.getPieceAt("b6"))
    }

    @Test
    fun `rook cannot take blocked pawn`() {
        assertThrows<IllegalArgumentException> { game.movePiece("b2", "b6") }

        assertEquals(whiteRook, game.board.getPieceAt("b2"))
        assertEquals(closestPawn, game.board.getPieceAt("b4"))
        assertEquals(blockedPawn, game.board.getPieceAt("b6"))
    }

    @Test
    fun `rook can move back`() {
        game.movePiece("b2", "b1")

        assertEquals(whiteRook.rules.javaClass, game.board.getPieceAt("b1")!!.rules.javaClass)
        assertEquals(whiteRook.player, game.board.getPieceAt("b1")!!.player)
        assertEquals(null, game.board.getPieceAt("b2"))
        assertEquals(closestPawn, game.board.getPieceAt("b4"))
        assertEquals(blockedPawn, game.board.getPieceAt("b6"))
    }

    @Test
    fun `rook can move right`() {
        game.movePiece("b2", "a2")

        assertEquals(whiteRook.rules.javaClass, game.board.getPieceAt("a2")!!.rules.javaClass)
        assertEquals(whiteRook.player, game.board.getPieceAt("a2")!!.player)
        assertEquals(null, game.board.getPieceAt("b2"))
        assertEquals(closestPawn, game.board.getPieceAt("b4"))
        assertEquals(blockedPawn, game.board.getPieceAt("b6"))
    }

    @Test
    fun `rook can move left`() {
        game.movePiece("b2", "h2")

        assertEquals(whiteRook.rules.javaClass, game.board.getPieceAt("h2")!!.rules.javaClass)
        assertEquals(whiteRook.player, game.board.getPieceAt("h2")!!.player)
        assertEquals(null, game.board.getPieceAt("b2"))
        assertEquals(closestPawn, game.board.getPieceAt("b4"))
        assertEquals(blockedPawn, game.board.getPieceAt("b6"))
    }

    @Test
    fun `rook cannot move diagonally`() {
        assertThrows<IllegalArgumentException> { game.movePiece("b2", "d4") }

        assertEquals(whiteRook, game.board.getPieceAt("b2"))
        assertEquals(null, game.board.getPieceAt("d4"))
        assertEquals(closestPawn, game.board.getPieceAt("b4"))
        assertEquals(blockedPawn, game.board.getPieceAt("b6"))
    }
}

class BishopMovementTest {
    private lateinit var list: List<Pair<String, Piece>>
    private lateinit var board: HashGameBoard
    private lateinit var game: TurnManagingGame
    private lateinit var closestPawn: Piece
    private lateinit var blockedPawn: Piece
    private lateinit var whiteBishop: Piece
    private lateinit var whiteKing: Piece
    private lateinit var blackKing: Piece

    @BeforeEach
    fun setUp() {
        closestPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
        blockedPawn = Piece(Player.BLACK, PawnPieceRules(Player.BLACK))
        whiteBishop = Piece(Player.WHITE, BishopPieceRules(Player.WHITE))
        whiteKing = Piece(Player.WHITE, KingPieceRules(Player.WHITE))
        blackKing = Piece(Player.BLACK, KingPieceRules(Player.BLACK))

        list = listOf("d4" to closestPawn, "c3" to blockedPawn, "g7" to whiteBishop, "h1" to whiteKing, "a8" to blackKing)
        board = HashGameBoard.build(RectangleBoardValidator(8, 8), list, "h1", "a8")
        game = TurnManagingGame(NoRules(), board, NoManager(), NoProvider())
    }

    @Test
    fun `bishop takes closest pawn`() {
        game.movePiece("g7", "d4")

        assertEquals(whiteBishop, game.board.getPieceAt("d4"))
        assertEquals(null, game.board.getPieceAt("g7"))
        assertEquals(blockedPawn, game.board.getPieceAt("c3"))
    }

    @Test
    fun `bishop cannot take blocked pawn`() {
        assertThrows<IllegalArgumentException> { game.movePiece("g7", "c3") }

        assertEquals(whiteBishop, game.board.getPieceAt("g7"))
        assertEquals(closestPawn, game.board.getPieceAt("d4"))
        assertEquals(blockedPawn, game.board.getPieceAt("c3"))
    }

    @Test
    fun `bishop can move to corner`() {
        game.movePiece("g7", "h8")

        assertEquals(whiteBishop, game.board.getPieceAt("h8"))
        assertEquals(null, game.board.getPieceAt("g7"))
        assertEquals(closestPawn, game.board.getPieceAt("d4"))
        assertEquals(blockedPawn, game.board.getPieceAt("c3"))
    }

    @Test
    fun `bishop can move to the front and to the right`() {
        game.movePiece("g7", "f8")

        assertEquals(whiteBishop, game.board.getPieceAt("f8"))
        assertEquals(null, game.board.getPieceAt("g7"))
        assertEquals(closestPawn, game.board.getPieceAt("d4"))
        assertEquals(blockedPawn, game.board.getPieceAt("c3"))
    }

    @Test
    fun `bishop can move close to the first pawn`() {
        game.movePiece("g7", "e5")

        assertEquals(whiteBishop, game.board.getPieceAt("e5"))
        assertEquals(null, game.board.getPieceAt("g7"))
        assertEquals(closestPawn, game.board.getPieceAt("d4"))
        assertEquals(blockedPawn, game.board.getPieceAt("c3"))
    }

    @Test
    fun `bishop cannot move vertically`() {
        assertThrows<IllegalArgumentException> { game.movePiece("g7", "g2") }

        assertEquals(whiteBishop, game.board.getPieceAt("g7"))
        assertEquals(null, game.board.getPieceAt("g2"))
        assertEquals(closestPawn, game.board.getPieceAt("d4"))
        assertEquals(blockedPawn, game.board.getPieceAt("c3"))
    }
}

class QueenMovementTest {
    private lateinit var list: List<Pair<String, Piece>>
    private lateinit var board: HashGameBoard
    private lateinit var game: TurnManagingGame
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
        whiteQueen = Piece(Player.WHITE, QueenPieceRules(Player.WHITE))
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
        game = TurnManagingGame(NoRules(), board, NoManager(), NoProvider())
    }

    @Test
    fun `queen takes pawn horizontally`() {
        game.movePiece("d6", "b6")

        assertEquals(whiteQueen, game.board.getPieceAt("b6"))
        assertEquals(null, game.board.getPieceAt("d6"))
        assertEquals(unreachablePawn, game.board.getPieceAt("b5"))
        assertEquals(diagonalPawn, game.board.getPieceAt("b4"))
    }

    @Test
    fun `queen cannot take unreachable pawn`() {
        assertThrows<IllegalArgumentException> { game.movePiece("d6", "b5") }

        assertEquals(whiteQueen, game.board.getPieceAt("d6"))
        assertEquals(horizontalPawn, game.board.getPieceAt("b6"))
        assertEquals(unreachablePawn, game.board.getPieceAt("b5"))
        assertEquals(diagonalPawn, game.board.getPieceAt("b4"))
    }

    @Test
    fun `queen takes pawn diagonally`() {
        game.movePiece("d6", "b4")

        assertEquals(whiteQueen, game.board.getPieceAt("b4"))
        assertEquals(null, game.board.getPieceAt("d6"))
        assertEquals(unreachablePawn, game.board.getPieceAt("b5"))
        assertEquals(horizontalPawn, game.board.getPieceAt("b6"))
    }

    @Test
    fun `queen can move back`() {
        game.movePiece("d6", "d1")

        assertEquals(whiteQueen, game.board.getPieceAt("d1"))
        assertEquals(null, game.board.getPieceAt("d6"))
        assertEquals(horizontalPawn, game.board.getPieceAt("b6"))
        assertEquals(unreachablePawn, game.board.getPieceAt("b5"))
        assertEquals(diagonalPawn, game.board.getPieceAt("b4"))
    }

    @Test
    fun `queen can move to the front and to the left`() {
        game.movePiece("d6", "e7")

        assertEquals(whiteQueen, game.board.getPieceAt("e7"))
        assertEquals(null, game.board.getPieceAt("d6"))
        assertEquals(horizontalPawn, game.board.getPieceAt("b6"))
        assertEquals(unreachablePawn, game.board.getPieceAt("b5"))
        assertEquals(diagonalPawn, game.board.getPieceAt("b4"))
    }

    @Test
    fun `queen cannot jump pawn diagonally`() {
        assertThrows<IllegalArgumentException> { game.movePiece("d6", "a3") }

        assertEquals(whiteQueen, game.board.getPieceAt("d6"))
        assertEquals(null, game.board.getPieceAt("a3"))
        assertEquals(horizontalPawn, game.board.getPieceAt("b6"))
        assertEquals(unreachablePawn, game.board.getPieceAt("b5"))
        assertEquals(diagonalPawn, game.board.getPieceAt("b4"))
    }

    @Test
    fun `queen cannot move like knight`() {
        assertThrows<IllegalArgumentException> { game.movePiece("d6", "e4") }

        assertEquals(whiteQueen, game.board.getPieceAt("d6"))
        assertEquals(null, game.board.getPieceAt("e4"))
        assertEquals(horizontalPawn, game.board.getPieceAt("b6"))
        assertEquals(unreachablePawn, game.board.getPieceAt("b5"))
        assertEquals(diagonalPawn, game.board.getPieceAt("b4"))
    }
}

class KnightMovementTest {
    private lateinit var list: List<Pair<String, Piece>>
    private lateinit var board: HashGameBoard
    private lateinit var game: TurnManagingGame
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
        whiteKnight = Piece(Player.WHITE, KnightPieceRules(Player.WHITE))
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
        game = TurnManagingGame(NoRules(), board, NoManager(), NoProvider())
    }

    @Test
    fun `knight takes pawn moving in an L shape`() {
        game.movePiece("g3", "f5")

        assertEquals(whiteKnight, game.board.getPieceAt("f5"))
        assertEquals(null, game.board.getPieceAt("g3"))
        assertEquals(horizontalPawn, game.board.getPieceAt("f3"))
        assertEquals(verticalPawn, game.board.getPieceAt("g4"))
    }

    @Test
    fun `knight cannot take pawn horizontally`() {
        assertThrows<IllegalArgumentException> { game.movePiece("g3", "f3") }

        assertEquals(whiteKnight, game.board.getPieceAt("g3"))
        assertEquals(verticalPawn, game.board.getPieceAt("g4"))
        assertEquals(takeablePawn, game.board.getPieceAt("f5"))
        assertEquals(horizontalPawn, game.board.getPieceAt("f3"))
    }

    @Test
    fun `knight cannot take pawn vertically`() {
        assertThrows<IllegalArgumentException> { game.movePiece("g3", "g4") }

        assertEquals(whiteKnight, game.board.getPieceAt("g3"))
        assertEquals(verticalPawn, game.board.getPieceAt("g4"))
        assertEquals(takeablePawn, game.board.getPieceAt("f5"))
        assertEquals(horizontalPawn, game.board.getPieceAt("f3"))
    }

    @Test
    fun `knight can jump over pawn in L shape`() {
        game.movePiece("g3", "e4")

        assertEquals(whiteKnight, game.board.getPieceAt("e4"))
        assertEquals(null, game.board.getPieceAt("g3"))
        assertEquals(verticalPawn, game.board.getPieceAt("g4"))
        assertEquals(takeablePawn, game.board.getPieceAt("f5"))
        assertEquals(horizontalPawn, game.board.getPieceAt("f3"))
    }

    @Test
    fun `knight cannot jump over pawn horizontally`() {
        assertThrows<IllegalArgumentException> { game.movePiece("g3", "e3") }

        assertEquals(whiteKnight, game.board.getPieceAt("g3"))
        assertEquals(null, game.board.getPieceAt("e3"))
        assertEquals(verticalPawn, game.board.getPieceAt("g4"))
        assertEquals(takeablePawn, game.board.getPieceAt("f5"))
        assertEquals(horizontalPawn, game.board.getPieceAt("f3"))
    }
}

class KingMovementTest {
    private lateinit var list: List<Pair<String, Piece>>
    private lateinit var board: HashGameBoard
    private lateinit var game: TurnManagingGame
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
        game = TurnManagingGame(NoRules(), board, NoManager(), NoProvider())
    }

    @Test
    fun `black king cannot move two squares`() {
        assertThrows<IllegalArgumentException> { game.movePiece("f6", "f4") }

        assertEquals(blackKing, game.board.getPieceAt("f6"))
        assertEquals(null, game.board.getPieceAt("f4"))
        assertEquals(whiteKing, game.board.getPieceAt("c3"))
        assertEquals(blackRook, game.board.getPieceAt("b8"))
    }

    @Test
    fun `black king can move left`() {
        game.movePiece("f6", "e6")

        assertEquals(blackKing, game.board.getPieceAt("e6"))
        assertEquals(null, game.board.getPieceAt("f6"))
        assertEquals(whiteKing, game.board.getPieceAt("c3"))
        assertEquals(blackRook, game.board.getPieceAt("b8"))
    }

    @Test
    fun `white king can move up and to the right`() {
        game.movePiece("c3", "d2")

        assertEquals(whiteKing, game.board.getPieceAt("d2"))
        assertEquals(null, game.board.getPieceAt("c3"))
        assertEquals(blackKing, game.board.getPieceAt("f6"))
        assertEquals(blackRook, game.board.getPieceAt("b8"))
    }

    @Test
    fun `black king is not checked by ally rook`() {
        game.movePiece("b8", "b6")

        assertEquals(blackRook.rules.javaClass, game.board.getPieceAt("b6")!!.rules.javaClass)
        assertEquals(blackRook.player, game.board.getPieceAt("b6")!!.player)
        assertEquals(blackKing, game.board.getPieceAt("f6"))
        assertEquals(false, KingPieceRules.isChecked(game.board, Player.BLACK))
    }

    @Test
    fun `white king is checked by enemy rook`() {
        game.movePiece("b8", "b3")

        assertEquals(blackRook.rules.javaClass, game.board.getPieceAt("b3")!!.rules.javaClass)
        assertEquals(blackRook.player, game.board.getPieceAt("b3")!!.player)
        assertEquals(whiteKing, game.board.getPieceAt("c3"))
        assertEquals(true, KingPieceRules.isChecked(game.board, Player.WHITE))
    }


    // TODO: This test should be moved to SpecialMovementsTest, since it
    //  would actually work by using game.run(), and in this case we
    //  are using game.movePiece()
//    @Test
//    fun `white king cannot move if it would become checked`() {
//        assertThrows<IllegalArgumentException> { game.movePiece("c3", "b3") }
//
//        assertEquals(whiteKing, game.board.getPieceAt("c3"))
//        assertEquals(null, game.board.getPieceAt("b3"))
//
//        assertEquals(true, KingPieceRules.isChecked(game.board, Player.WHITE))
//    }
}

class NoRules : GameRules {
    override fun isPieceMovable(position: String): Boolean {
        return true
    }

    override fun isMoveValid(board: GameBoard, player: Player, from: String, to: String): Boolean {
        return true
    }

    override fun playerReachedWinCondition(player: Player, enemyState: PlayerState): Boolean {
        return false
    }

    override fun wasTieReached(playerOnTurn: Player, enemyState: PlayerState): Boolean {
        return false
    }

    override fun playerIsChecked(player: Player): Boolean {
        return false
    }

    override fun runPostPlayProcedures(
        board: GameBoard,
        piece: Piece,
        finalPosition: String,
        inputProvider: PlayerInputProvider,
    ): GameBoard {
        return board
    }
}

class NoManager: TurnManager {
    override fun getTurn(): Player {
        return Player.WHITE
    }

    override fun nextTurn(): TurnManager {
        return this
    }

}

class NoProvider : PlayerInputProvider {
    override fun requestPlayerMove(player: Player): Pair<String, String> {
        return Pair("", "")
    }

    override fun requestPromotionPiece(player: Player): PieceRules {
        return QueenPieceRules(player)
    }
}
