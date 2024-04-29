package edu.austral.dissis.chess.rules

class IndependentRuleChain<T> : Rule<T?> {

    val rules: Array<out IndependentRule<T>>
    val onSuccess: T?

    constructor(rules: List<Pair<Boolean, T>>) {
        this.rules = rules.map { IndependentRule(it.first, it.second) }.toTypedArray()
        this.onSuccess = null
    }

    constructor(vararg rules: Pair<Boolean, T>) {
        this.rules = rules.map { IndependentRule(it.first, it.second) }.toTypedArray()
        this.onSuccess = null
    }

    private constructor(
        rules: Array<out IndependentRule<T>>,
        onSuccess: T?
    ) {
        this.rules = rules
        this.onSuccess = onSuccess
    }

    override fun verify(): T? {
        return rules
            .find { !it.verify() }
            ?.onFail
            ?: onSuccess
    }

    fun onSuccess(onSuccess: T):IndependentRuleChain<T> {
        return IndependentRuleChain(rules, onSuccess)
    }
}

class IndependentRule<T> (
    private val condition: Boolean,
    val onFail: T
) {
    fun verify(): Boolean {
        return condition
    }
}
