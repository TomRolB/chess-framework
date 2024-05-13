package edu.austral.dissis.chess.engine.rules

interface Rule<T> {
    fun verify(): T
}
