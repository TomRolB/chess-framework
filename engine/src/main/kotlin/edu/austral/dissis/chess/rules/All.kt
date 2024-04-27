package edu.austral.dissis.chess.rules

class All : Rule<Boolean> {
    val rules: Array<out Rule<Boolean>>

    constructor(vararg rules: Rule<Boolean>) {
        this.rules = rules
    }

    constructor(vararg conditions: Boolean) {
        this.rules = conditions.map { SimpleRule(it) }.toTypedArray()
    }

    override fun verify(): Boolean {
        return rules.all { it.verify() }
    }
}