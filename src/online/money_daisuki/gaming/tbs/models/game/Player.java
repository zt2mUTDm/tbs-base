package online.money_daisuki.gaming.tbs.models.game;

import java.io.Serializable;

public interface Player extends Serializable {
	
	PlayerType getType();
	
	String getName();
	
	int getColor();
	
	Player replaceType(PlayerType newType);
	
	public enum PlayerType implements Serializable {
		LOCAL,
		NETWORK,
		DISABLED
	}
}
