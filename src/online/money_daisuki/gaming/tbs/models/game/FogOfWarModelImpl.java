package online.money_daisuki.gaming.tbs.models.game;

import java.util.HashMap;
import java.util.Map;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.gaming.tbs.models.data.UnitTemplate;
import online.money_daisuki.gaming.tbs.models.game.TileStateModel.TileState;

public final class FogOfWarModelImpl implements FogOfWarModel {
	private final Map<Integer, Integer> visibilities;
	private final Converter<Integer, TileState> stateStrategy;
	
	public FogOfWarModelImpl(final Converter<Integer, TileState> stateStrategy) {
		visibilities = new HashMap<>();
		this.stateStrategy = Requires.notNull(stateStrategy, "stateStrategy == null");
	}
	@Override
	public TileState getTileState(final Integer tileId) {
		final Integer visibility = visibilities.get(tileId);
		if(visibility != null) {
			return(stateStrategy.convert(visibility));
		}
		return(TileState.NEVER_SEEN);
	}
	@Override
	public void addVisibility(final Integer tileId, final UnitTemplate unitTemp, final Integer visibilityStretch) {
		changeVisibility(tileId, getRealViewStretch(unitTemp, visibilityStretch));
	}
	@Override
	public void removeVisibility(final Integer tileId, final UnitTemplate unitTemp, final Integer visibilityStretch) {
		changeVisibility(tileId, -getRealViewStretch(unitTemp, visibilityStretch));
	}
	private void changeVisibility(final Integer tileId, final Integer visibilityStretch) {
		Integer i = visibilities.get(tileId);
		if(i != null) {
			i = i + visibilityStretch;
		} else {
			i = visibilityStretch;
		}
		visibilities.put(tileId, i);
	}
	
	private Integer getRealViewStretch(final UnitTemplate unitTemp, final Integer visibilityStretch) {
		final int viewDistance = unitTemp.getSight();
		return(viewDistance - visibilityStretch.intValue() + 1);
	}
}
