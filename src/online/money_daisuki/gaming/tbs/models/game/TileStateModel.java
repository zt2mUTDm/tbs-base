package online.money_daisuki.gaming.tbs.models.game;

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
	
	public enum TileState {
		VISIBLE(true),
		INVISIBLE(false),
		DISABLED(true),
		INVISIBLE_DISABLED(false),
		NEVER_SEEN(false);
		
		private final boolean b;
		
		private TileState(final boolean b) {
			this.b = b;
		}
		public boolean seeUnits() {
			return(b);
		}
	}
}
