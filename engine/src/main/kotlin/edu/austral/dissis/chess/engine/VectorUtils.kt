package edu.austral.dissis.chess.engine

import kotlin.math.sign

fun areVectorsParallel(
    vector1: Pair<Int, Int>,
    vector2: Pair<Int, Int>,
) = vector1.second * vector2.first == vector1.first * vector2.second

fun doVectorsShareOrientation(
    vector1: Pair<Int, Int>,
    vector2: Pair<Int, Int>,
): Boolean {
    return vector1.first.sign == vector2.first.sign
}
