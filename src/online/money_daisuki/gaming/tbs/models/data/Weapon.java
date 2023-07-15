package online.money_daisuki.gaming.tbs.models.data;

import java.io.Serializable;

public interface Weapon extends Serializable {
	
	String getName();
	
	Ammo getAmmo();
	
	int getMaxAmmo();
	
	int getStrength();
	
	int getMinDistance();
	
	int getMaxDistance();
	
	boolean canAttack(Drive drive);
	
	boolean canGetCounterstriked();
	
}
