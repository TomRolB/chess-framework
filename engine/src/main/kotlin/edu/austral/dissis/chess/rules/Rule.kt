package edu.austral.dissis.chess.rules

interface Rule<T> {
    fun verify(): T
}

//TODO: Rule design is a mess. Fix it
