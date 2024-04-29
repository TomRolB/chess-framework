package edu.austral.dissis.chess.rules

class FirstFailOrNull<T> : Rule<T?> {

    val rules: Array<out IndependentRule<T>>

    constructor(vararg rules: IndependentRule<T>) {
        this.rules = rules
    }

    constructor(vararg rules: Pair<Boolean, T>) {
        this.rules = rules.map { IndependentRule(it.first, it.second) }.toTypedArray()
    }

    override fun verify(): T? {
        return rules
            .find { !it.verify() }
            ?.onFail
    }
}

class IndependentRule<T>(
    private val condition: Boolean,
    val onFail: T
) {
    fun verify(): Boolean {
        return condition
    }
}
