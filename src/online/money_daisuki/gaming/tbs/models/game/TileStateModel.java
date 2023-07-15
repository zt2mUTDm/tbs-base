package online.money_daisuki.gaming.tbs.models.game;

import java.io.Serializable;

public interface TileStateModel {
	
	void addEnabled(Integer tileId);
	
	boolean isEnabled(Integer tileId);
	
	boolean removeEnabled(Integer tileId);
	
	int getEnabledCount();
	
	void clearEnableds();
	
	TileState getState(Integer tileId);
	
	void addMoveMark(Integer tileId);
	
	void removeMoveMark(Integer tileId);
	
	void clearMoveMarks();
	
	boolean hasMoveMark(Integer tileId);
	
	void addAttackMark(Integer tileId);
	
	void removeAttackMark(Integer tileId);
	
	void clearAttackMarks();
	
	boolean hasAttackMark(Integer tileId);
	
	public enum TileState implements Serializable {
		VISIBLE(true, true),
		INVISIBLE(false, true),
		DISABLED(true, false),
		INVISIBLE_DISABLED(false, false),
		NEVER_SEEN(false, false);
		
		private final boolean seeUnit;
		private final boolean canMoveOn;
		
		private TileState(final boolean seeUnit, final boolean canMoveOn) {
			this.seeUnit = seeUnit;
			this.canMoveOn = canMoveOn;
		}
		public boolean seeUnits() {
			return(seeUnit);
		}
		public boolean canMoveOn() {
			return (canMoveOn);
		}
	}
}
