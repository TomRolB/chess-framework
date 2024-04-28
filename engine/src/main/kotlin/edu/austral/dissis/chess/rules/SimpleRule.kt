package edu.austral.dissis.chess.rules

class SimpleRule(private val condition: Boolean) : Rule<Boolean> {
    override fun verify(): Boolean {
        return condition
    }
}
