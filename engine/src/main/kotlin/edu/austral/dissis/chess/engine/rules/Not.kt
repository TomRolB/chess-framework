package edu.austral.dissis.chess.engine.rules

class Not(val rule: Rule<Boolean>) : Rule<Boolean> {
    override fun verify(): Boolean {
        return !rule.verify()
    }
}
