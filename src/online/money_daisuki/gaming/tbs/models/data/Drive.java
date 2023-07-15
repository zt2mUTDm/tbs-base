package online.money_daisuki.gaming.tbs.models.data;

import java.io.Serializable;

public interface Drive extends Serializable {
	
	String getName();
	
	boolean terrainDefenseApplicable();
	
	int getFuelLossPerRound();
	
}
