package online.money_daisuki.gaming.tbs.models.game;

import java.util.Map;

import online.money_daisuki.gaming.tbs.models.game.TileStateModel.TileState;

public final class FailedUnitMovedEvent implements UnitMovedEvent {
	@Override
	public boolean wasSuccessful() {
		return(false);
	}
	@Override
	public Integer getActualTile() {
		throw new UnsupportedOperationException();
	}
	@Override
	public Map<Integer, TileState> getChangedTiles() {
		throw new UnsupportedOperationException();
	}
	@Override
	public Map<Integer, Unit> getNewUnits() {
		throw new UnsupportedOperationException();
	}
}
