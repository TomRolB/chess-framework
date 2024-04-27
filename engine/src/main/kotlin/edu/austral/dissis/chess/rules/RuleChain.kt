package edu.austral.dissis.chess.rules

// Used for cases where we need to pass information
// between rules, since All cannot do this.
interface RuleChain<In, Out> {
    fun verify(arg: In): Out
}

// Sometimes an element of the chain is the last one, but
// we need to pass the next RuleChain object either way.
// Succeed serves this purpose: it will simply return true.
class Succeed<T> : RuleChain<T, Boolean> {
    override fun verify(arg: T): Boolean {
        return true
    }
}