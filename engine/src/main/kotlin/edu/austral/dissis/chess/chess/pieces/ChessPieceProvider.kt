package edu.austral.dissis.chess.chess.pieces

import edu.austral.dissis.chess.chess.pieces.CapablancaPieceTypes.ARCHBISHOP
import edu.austral.dissis.chess.chess.pieces.CapablancaPieceTypes.CHANCELLOR
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.BISHOP
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.KING
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.KNIGHT
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.PAWN
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.QUEEN
import edu.austral.dissis.chess.chess.pieces.ChessPieceTypes.ROOK
import edu.austral.dissis.chess.chess.pieces.ChessRulesProvider.getBishopRules
import edu.austral.dissis.chess.chess.pieces.ChessRulesProvider.getKingRules
import edu.austral.dissis.chess.chess.pieces.ChessRulesProvider.getKnightRules
import edu.austral.dissis.chess.chess.pieces.ChessRulesProvider.getPawnRules
import edu.austral.dissis.chess.chess.pieces.ChessRulesProvider.getRookRules
import edu.austral.dissis.chess.engine.Player
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.rules.pieces.CombinedRules

// TODO: Many rules are actually general, such as NoSelfCheckInValidPlays.
//  Consider whether this can be unified somewhere, or if it's better to keep it this way
//  (for instance, we may want a piece to not be affected by checks at all)

object ChessPieceProvider {
    fun getRook(player: Player) =
        Piece(
            type = ROOK,
            player = player,
            rules =
                getRookRules(player),
        )

    fun getBishop(player: Player) =
        Piece(
            type = BISHOP,
            player = player,
            rules =
                getBishopRules(player),
        )

    fun getQueen(player: Player) =
        Piece(
            type = QUEEN,
            player = player,
            rules =
                CombinedRules(
                    getRookRules(player),
                    getBishopRules(player),
                ),
        )

    fun getKnight(player: Player) =
        Piece(
            KNIGHT,
            player = player,
            rules =
                getKnightRules(player),
        )

    fun getPawn(player: Player) =
        // TODO: Chaining updates?
        Piece(
            PAWN,
            player,
            rules =
                getPawnRules(player),
        )

    fun getKing(player: Player) =
        Piece(
            KING,
            player,
            rules =
                getKingRules(player),
        )

    fun getChancellor(player: Player) =
        Piece(
            CHANCELLOR,
            player,
            rules =
                CombinedRules(
                    getRookRules(player),
                    getKnightRules(player),
                ),
        )

    fun getArchbishop(player: Player) =
        Piece(
            ARCHBISHOP,
            player,
            rules =
                CombinedRules(
                    getBishopRules(player),
                    getKnightRules(player),
                ),
        )
}
