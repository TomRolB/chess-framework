package edu.austral.dissis.chess.engine.rules

class SimpleRule(private val condition: Boolean) : Rule<Boolean> {
    override fun verify(): Boolean {
        return condition
    }
}
