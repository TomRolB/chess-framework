package edu.austral.dissis.chess.engine.rules

interface Rule<T> {
    fun verify(): T
}

// TODO: need to rename rules, in general (piece rules too).
//  May rename this interface to Condition
// TODO: have to better organize directory as well
