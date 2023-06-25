package online.money_daisuki.gaming.tbs.models.game;

import online.money_daisuki.gaming.tbs.models.data.UnitTemplate;
import online.money_daisuki.gaming.tbs.models.game.TileStateModel.TileState;

public interface FogOfWarModel {
	
	void addVisibility(Integer tileId, UnitTemplate unitTemp, Integer visibilityStretch);
	
	void removeVisibility(Integer tileId, UnitTemplate unitTemp, Integer visibilityStretch);
	
	TileState getTileState(Integer tileId);
	
}
