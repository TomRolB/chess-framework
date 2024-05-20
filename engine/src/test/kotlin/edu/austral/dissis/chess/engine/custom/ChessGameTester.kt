package edu.austral.dissis.chess.engine.custom

import edu.austral.dissis.chess.test.TestBoard
import edu.austral.dissis.chess.test.TestGame
import edu.austral.dissis.chess.test.TestGameResult
import edu.austral.dissis.chess.test.game.BlackCheckMate
import edu.austral.dissis.chess.test.game.FinalTestMoveResult
import edu.austral.dissis.chess.test.game.Redo
import edu.austral.dissis.chess.test.game.TestGameRunner
import edu.austral.dissis.chess.test.game.TestInput
import edu.austral.dissis.chess.test.game.TestMove
import edu.austral.dissis.chess.test.game.TestMoveDraw
import edu.austral.dissis.chess.test.game.TestMoveFailure
import edu.austral.dissis.chess.test.game.TestMoveSuccess
import edu.austral.dissis.chess.test.game.Undo
import edu.austral.dissis.chess.test.game.WhiteCheckMate
import edu.austral.dissis.chess.test.gameParser.GameBoardParser
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.DynamicTest
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

class ChessGameTester(private val runner: TestGameRunner) {
    private val parser: GameBoardParser = GameBoardParser()

    fun test(): Stream<DynamicTest> {
        return getTestPaths().stream().map { gameTest(it) }
    }

    fun debug(fileName: String): Stream<DynamicTest> {
        return gameTest("/custom_test_cases/$fileName").let { Stream.of(it) }
    }

    private fun gameTest(resource: String): DynamicTest {
        val content = content(resource) ?: fail("$resource not found in classpath")
        val testGame = parser.parse(content)

        return DynamicTest.dynamicTest(testGame.title) {
            when (testGame.testResult) {
                TestGameResult.ALL_MOVES_VALID -> assertAllMovesValid(testGame)
                TestGameResult.LAST_MOVE_INVALID -> assertLastMove(testGame) { it is TestMoveFailure }
                TestGameResult.WHITE_MATE -> assertLastMove(testGame) { it is WhiteCheckMate }
                TestGameResult.BLACK_MATE -> assertLastMove(testGame) { it is BlackCheckMate }
                TestGameResult.DRAW -> assertLastMove(testGame) { it is TestMoveDraw }
            }
        }
    }

    private fun runMoves(
        title: String,
        runner: TestGameRunner,
        moves: List<TestInput>,
    ): TestGameRunner {
        return moves.fold(runner) { currentRunner, input ->
            val result =
                when (input) {
                    is Undo -> currentRunner.undo()
                    is Redo -> currentRunner.redo()
                    is TestMove -> currentRunner.executeMove(input.from, input.to)
                }
            when (result) {
                is TestMoveSuccess -> result.testGameRunner
                is FinalTestMoveResult -> fail(failedMoveMessage(title, input, result))
            }
        }
    }

    private fun assertAllMovesValid(testGame: TestGame) {
        val initialRunner = runner.withBoard(testGame.initialBoard)
        val resultingRunner = runMoves(testGame.title, initialRunner, testGame.inputs)
        checkFinalBoardMatches(resultingRunner.getBoard(), testGame.finalBoard)
    }

    private fun assertLastMove(
        testGame: TestGame,
        checkResult: (FinalTestMoveResult) -> Boolean,
    ) {
        val initialRunner = runner.withBoard(testGame.initialBoard)
        val preparatoryMoves = testGame.inputs.dropLast(1)
        val lastMove = testGame.inputs.last() as TestMove
        val finalRunner = runMoves(testGame.title, initialRunner, preparatoryMoves)
        when (val result = finalRunner.executeMove(lastMove.from, lastMove.to)) {
            is TestMoveSuccess -> fail("${testGame.title} failed, last move should result in game end but did not")
            is FinalTestMoveResult -> {
                if (!checkResult(result)) {
                    fail<String>("${testGame.title} failed, last move did not result in expected outcome")
                }
                checkFinalBoardMatches(result.finalBoard, testGame.finalBoard)
            }
        }
    }

    private fun checkFinalBoardMatches(
        actualBoard: TestBoard,
        expectedBoard: TestBoard,
    ) {
        if (actualBoard != expectedBoard) {
            fail<String>("\n$actualBoard\n did not match expected board \n$expectedBoard\n")
        }
    }

    private fun content(resource: String): String? {
        return this::class.java
            .getResourceAsStream(resource)
            ?.bufferedReader()
            ?.readLines()
            ?.joinToString("\n")
    }

    private fun getTestPaths(): List<String> {
        val resourcePath = "custom_test_cases/"
        val uri = Thread.currentThread().contextClassLoader.getResource(resourcePath)?.toURI()
        val testPaths = mutableListOf<String>()
        uri?.let {
            val path: Path =
                if (uri.scheme == "jar") {
                    // For JAR file, create a file system if not created
                    FileSystems.newFileSystem(uri, emptyMap<String, Any>()).getPath(resourcePath)
                } else {
                    // For files directly on the file system
                    Paths.get(uri)
                }

            // Stream the paths under the directory and process each file
            Files.walk(path, 1).use { paths ->
                paths.filter { p -> Files.isRegularFile(p) }
                    .forEach { p: Path ->
                        testPaths.add("/custom_test_cases/${p.fileName}")
                    }
            }
        } ?: println("Resource not found: $resourcePath")

        return testPaths
    }

    private fun failedMoveMessage(
        title: String,
        input: TestInput,
        result: FinalTestMoveResult,
    ): String {
        return when (input) {
            is Undo -> "$title failed, undo should not result in game end"
            is Redo -> "$title failed, redo should not result in game end"
            is TestMove -> {
                val fromFile = ('a'.code + input.from.col - 1).toChar()
                val fromRank = input.from.row

                val toFile = ('a'.code + input.to.col - 1).toChar()
                val toRank = input.to.row

                val pieceType = result.finalBoard.pieces[input.from].toString()

                "$title failed, moving $pieceType from $fromFile$fromRank to $toFile$toRank"
            }
        }
    }
}
