package online.money_daisuki.gaming.tbs.models.game.network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;

import online.money_daisuki.api.base.Requires;

public final class UnitAttackRequest implements Serializable {
	private final Collection<Integer> tiles;
	
	public UnitAttackRequest(final Collection<Integer> tiles) {
		this.tiles = Requires.containsNotNull(new ArrayList<>(Requires.notNull(tiles, "tiles == null")));
	}
	public Deque<Integer> getTiles() {
		return(new LinkedList<>(tiles));
	}
}
