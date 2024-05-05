package edu.austral.dissis.chess.rules

interface Rule<T> {
    fun verify(): T
}

//TODO: need to rename rules, in general (piece rules too).
// May rename this interface to Condition
