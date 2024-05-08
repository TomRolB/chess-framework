package edu.austral.dissis.chess.engine.rules

/**
* RuleChain is used for cases where we need to pass information
* between rules, since All cannot do this.

* Parameters:
*  1. Constructor parameters: used for data which is known at
*     compile time
*  2. verify()'s parameter 'arg': used for data which is known at
*     runtime (the previous RuleChain object has to pass it down)
*/

interface RuleChain<In, Out> {
    // TODO: May replace all RuleChain implementations by sth else
    fun verify(arg: In): Out
}

/**
* Sometimes an element of the chain is the last one, but
* we need to pass the next RuleChain object either way.
* Succeed serves this purpose: it will simply return true.
*/
class Succeed<T> : RuleChain<T, Boolean> {
    override fun verify(arg: T): Boolean {
        return true
    }
}
