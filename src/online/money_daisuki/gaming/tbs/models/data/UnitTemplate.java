package online.money_daisuki.gaming.tbs.models.data;

import java.io.Serializable;

public interface UnitTemplate extends Serializable {
	
	String getName();
	
	int getDefense();
	
	int getMaxFuel();
	
	int getMoveDistance();
	
	Drive getDrive();
	
	int getSight();
	
	boolean canDriveByShooting();
	
	boolean canMoveXorAttack();
	
	int getWeaponCount();
	
	Weapon getWeapon(int index);
	
}
