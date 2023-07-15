package online.money_daisuki.gaming.tbs.models.game.businessLevelModel;

public interface LocalBusinessLevelModel extends BusinessLevelModel {
	public int getCurrentPlayerId();
	
	void addNextTurnListener(Runnable l);
	
	boolean removeNextTurnListener(Runnable l);
	
}
