package edu.austral.dissis.chess.rules

interface Rule<T> {
    fun verify(): T
}
