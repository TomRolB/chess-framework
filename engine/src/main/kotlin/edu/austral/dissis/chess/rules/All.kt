package edu.austral.dissis.chess.rules

class All(vararg val rules: Rule<Boolean>) : Rule<Boolean> {
    override fun verify(): Boolean {
        return rules.all { it.verify() }
    }
}
