package edu.austral.dissis.chess.engine

class GameFlowTests {
}

class BufferedInputProvider: PlayerInputProvider {
    override fun requestPlayerMove(player: Player): String {
        TODO("Not yet implemented. The moves will be loaded" +
                ", so that we don't have to provide input each" +
                "single time the game requests it")
    }
}