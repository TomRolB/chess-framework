package edu.austral.dissis.chess.ui

import edu.austral.dissis.chess.chess.variants.getClassicEngine
import edu.austral.dissis.chess.chess.variants.getExtinctionEngine
import edu.austral.dissis.chess.gui.CachedImageResolver
import edu.austral.dissis.chess.gui.DefaultImageResolver
import edu.austral.dissis.chess.gui.GameView
import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Scene
import javafx.stage.Stage

fun main() {
    launch(ChessGameApplication::class.java)
}

class ChessGameApplication : Application() {
    private val gameEngine =
        getClassicEngine()
//        getCapablancaEngine()
//        getExtinctionEngine()
    private val imageResolver = CachedImageResolver(DefaultImageResolver())

    override fun start(primaryStage: Stage) {
        primaryStage.title = GAME_TITLE

        val root = GameView(gameEngine, imageResolver)
        primaryStage.scene = Scene(root)

        primaryStage.show()
    }

    companion object {
        const val GAME_TITLE = "Chess"
    }
}
