package online.money_daisuki.gaming.tbs.models.game;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.gaming.tbs.models.game.businessLevelModel.TileGraphViewCalculatorSubModel;

public final class TileGraphViewCalculatorLevelSubModel implements TileGraphViewCalculatorSubModel {
	private final LevelModel level;
	
	public TileGraphViewCalculatorLevelSubModel(final LevelModel level) {
		this.level = Requires.notNull(level, "level == null");
	}
	@Override
	public int getTileConnectionCount(final Integer tileId) {
		return(level.getTileConnectionCount(tileId));
	}
	@Override
	public Integer getConnectedTile(final Integer tileId, final int i) {
		return(level.getConnectedTile(tileId, i));
	}
	@Override
	public boolean isTileConnected(final Integer tileId, final int i) {
		return(level.isTileConnected(tileId, i));
	}
}
