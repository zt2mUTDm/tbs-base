package online.money_daisuki.gaming.tbs.models.data;

public interface TileTemplate {
	
	String getName();
	
	int getDefensive();
	
	boolean hasExitCost(Drive drive);
	
	int getExitCosts(Drive drive);
	
	boolean hasEnterCost(Drive drive);
	
	int getEnterCosts(Drive drive);
	
}
