package online.money_daisuki.gaming.tbs.models.game.businessLevelModel;

import java.util.Deque;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.gaming.tbs.models.game.UnitAttackedEvent;
import online.money_daisuki.gaming.tbs.models.game.UnitEndEvent;
import online.money_daisuki.gaming.tbs.models.game.UnitMovedEvent;
import online.money_daisuki.gaming.tbs.models.game.UpdateGameDataEvent;

public interface BusinessLevelModel {
	
	void moveUnit(Deque<Integer> tiles, DataSink<? super UnitMovedEvent> callback);
	
	void attackUnit(Deque<Integer> tiles, DataSink<? super UnitAttackedEvent> callback);
	
	void endTurn(DataSink<? super UnitEndEvent> callback);
	
	void getGameData(DataSink<? super UpdateGameDataEvent> callback);
	
}
