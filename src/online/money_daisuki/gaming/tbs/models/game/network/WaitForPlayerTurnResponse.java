package online.money_daisuki.gaming.tbs.models.game.network;

import java.io.Serializable;

import online.money_daisuki.api.base.Requires;

public final class WaitForPlayerTurnResponse implements Serializable {
	private final int nextPlayerId;
	
	public WaitForPlayerTurnResponse(final int nextPlayerId) {
		this.nextPlayerId = Requires.positive(nextPlayerId, "nextPlayerId < 0");
	}
	public int getNextPlayerId() {
		return (nextPlayerId);
	}
}
