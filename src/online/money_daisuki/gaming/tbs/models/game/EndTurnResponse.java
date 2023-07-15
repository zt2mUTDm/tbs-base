package online.money_daisuki.gaming.tbs.models.game;

import java.io.Serializable;

import online.money_daisuki.api.base.Requires;

public final class EndTurnResponse implements Serializable {
	private final int nextPlayerId;
	
	public EndTurnResponse(final int nextPlayerId) {
		this.nextPlayerId = Requires.positive(nextPlayerId, "nextPlayerId < 0");
	}
	public int getNextPlayerId() {
		return (nextPlayerId);
	}
}
