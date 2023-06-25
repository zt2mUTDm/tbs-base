package online.money_daisuki.gaming.tbs.models.data;

public interface Weapon {
	
	String getName();
	
	Ammo getAmmo();
	
	int getMaxAmmo();
	
	int getStrength();
	
	int getMinDistance();
	
	int getMaxDistance();
	
	boolean canAttack(Drive drive);
	
	boolean canGetCounterstriked();
	
}
