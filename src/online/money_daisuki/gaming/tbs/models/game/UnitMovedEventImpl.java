package online.money_daisuki.gaming.tbs.models.game;

import java.util.HashMap;
import java.util.Map;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.SetableDataSource;
import online.money_daisuki.gaming.tbs.models.game.TileStateModel.TileState;

public final class UnitMovedEventImpl implements UnitMovedEvent {
	private final Integer actualTile;
	private final Map<Integer, TileState> newTileStates;
	private final Map<Integer, Unit> newUnits;
	private final UnitAttackedResponse attackEvent;
	
	public UnitMovedEventImpl(final boolean sucessful, final Integer actualTile,
			final Map<Integer, TileState> newTileStates, final Map<Integer, Unit> newUnits,
			final SetableDataSource<UnitAttackedResponse> attackEvent) {
		this.actualTile = Requires.notNull(actualTile, "actualTile == null");
		this.newTileStates = Requires.containsNotNull(new HashMap<>(Requires.notNull(newTileStates)));
		this.newUnits = Requires.containsNotNull(new HashMap<>(Requires.notNull(newUnits)));
		this.attackEvent = attackEvent.isSet() ? attackEvent.source() : null;
	}
	
	@Override
	public boolean wasSuccessful() {
		return(true);
	}
	@Override
	public Integer getTargetTile() {
		return(actualTile);
	}
	@Override
	public Map<Integer, TileState> getChangedTiles() {
		return(new HashMap<>(newTileStates));
	}
	@Override
	public Map<Integer, Unit> getNewUnits() {
		return (new HashMap<>(newUnits));
	}
	@Override
	public boolean hasAttackEvent() {
		return(attackEvent != null);
	}
	@Override
	public UnitAttackedResponse getAttackEvent() {
		return(attackEvent);
	}
}
