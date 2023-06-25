package online.money_daisuki.gaming.tbs.models.data;

public interface UnitTemplate {
	
	String getName();
	
	int getDefense();
	
	int getMoveDistance();
	
	Drive getDrive();
	
	int getSight();
	
	boolean canDriveByShooting();
	
	int getWeaponCount();
	
	Weapon getWeapon(int index);
	
	String getImageUrl(int player, int i);
	
}
