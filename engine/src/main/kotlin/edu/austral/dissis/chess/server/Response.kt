package edu.austral.dissis.chess.server

import edu.austral.ingsis.clientserver.Message

sealed interface Response

class Broadcast<P : Any>(val message: Message<P>) : Response
class Unicast<P : Any>(val clientId: String, val message: Message<P>) : Response
data object Awaiting : Response
