package online.money_daisuki.gaming.tbs.models.game.businessLevelModel;

import java.util.Deque;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.gaming.tbs.models.game.GameDataReponse;
import online.money_daisuki.gaming.tbs.models.game.UnitAttackedResponse;
import online.money_daisuki.gaming.tbs.models.game.EndTurnResponse;
import online.money_daisuki.gaming.tbs.models.game.UnitMovedEvent;
import online.money_daisuki.gaming.tbs.models.game.network.WaitForPlayerTurnResponse;

public interface BusinessLevelModel {
	
	void moveUnit(Deque<Integer> tiles, DataSink<? super UnitMovedEvent> callback);
	
	void attackUnit(Deque<Integer> tiles, DataSink<? super UnitAttackedResponse> callback);
	
	void endTurn(int playerId, DataSink<? super EndTurnResponse> callback);
	
	void getGameData(DataSink<? super GameDataReponse> callback);
	
	void waitForPlayerTurn(final int playerId, final DataSink<WaitForPlayerTurnResponse> callback);
	
}
