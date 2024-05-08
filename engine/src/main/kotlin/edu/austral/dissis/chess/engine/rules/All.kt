package edu.austral.dissis.chess.engine.rules

class All(vararg val rules: edu.austral.dissis.chess.engine.rules.Rule<Boolean>) :
    edu.austral.dissis.chess.engine.rules.Rule<Boolean> {
    override fun verify(): Boolean {
        return rules.all { it.verify() }
    }
}
