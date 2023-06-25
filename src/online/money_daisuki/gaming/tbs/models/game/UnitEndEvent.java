package online.money_daisuki.gaming.tbs.models.game;

import online.money_daisuki.api.base.Requires;

public final class UnitEndEvent {
	private final int nextPlayerId;
	
	public UnitEndEvent(final int nextPlayerId) {
		this.nextPlayerId = Requires.positive(nextPlayerId, "nextPlayerId < 0");
	}
	public int getNextPlayerId() {
		return (nextPlayerId);
	}
}
