package online.money_daisuki.gaming.tbs.models.game;

import java.util.HashSet;
import java.util.Set;

import online.money_daisuki.api.base.Requires;

public final class TileStateModelImpl implements TileStateModel {
	private final Set<Integer> enabled;
	private final LevelModel level;
	private final Set<Integer> moveMarkedTiles;
	private final Set<Integer> attackMarkedTiles;
	
	public TileStateModelImpl(final LevelModel level) {
		this.level = Requires.notNull(level, "level == null");
		this.enabled = new HashSet<>();
		this.moveMarkedTiles = new HashSet<>();
		this.attackMarkedTiles = new HashSet<>();
	}
	
	@Override
	public void addEnabled(final Integer tileId) {
		enabled.add(Requires.notNull(tileId, "tileId == null"));
	}
	@Override
	public boolean isEnabled(final Integer tileId) {
		return(enabled.contains(Requires.notNull(tileId, "tileId == null")));
	}
	@Override
	public boolean removeEnabled(final Integer tileId) {
		return(enabled.remove(Requires.notNull(tileId, "tileId == null")));
	}
	@Override
	public int getEnabledCount() {
		return(enabled.size());
	}
	@Override
	public void clearEnableds() {
		enabled.clear();
	}
	@Override
	public TileState getState(final Integer tileId) {
		final TileState state = level.getTileState(tileId);
		if(enabled.isEmpty() || enabled.contains(tileId)) {
			return(state);
		} else {
			return(state == TileState.NEVER_SEEN ? state : TileState.DISABLED);
		}
	} 
	
	@Override
	public void addMoveMark(final Integer tileId) {
		moveMarkedTiles.add(tileId);
	}
	@Override
	public void removeMoveMark(final Integer tileId) {
		moveMarkedTiles.remove(tileId);
	}
	@Override
	public void clearMoveMarks() {
		moveMarkedTiles.clear();
	}
	@Override
	public boolean hasMoveMark(final Integer tileId) {
		return(moveMarkedTiles.contains(tileId));
	}
	
	@Override
	public void addAttackMark(final Integer tileId) {
		attackMarkedTiles.add(tileId);
	}
	@Override
	public void removeAttackMark(final Integer tileId) {
		attackMarkedTiles.remove(tileId);
	}
	@Override
	public void clearAttackMarks() {
		attackMarkedTiles.clear();
	}
	@Override
	public boolean hasAttackMark(final Integer tileId) {
		return(attackMarkedTiles.contains(tileId));
	}
	
}
