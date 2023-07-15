package online.money_daisuki.gaming.tbs.models.game;

import java.io.Serializable;

import online.money_daisuki.gaming.tbs.models.data.UnitTemplate;

public interface Unit extends Serializable {
	
	boolean canMove();
	
	boolean canAttack();
	
	int getPlayerId();
	
	UnitTemplate getTemplate();
	
	void reset();
	
	int getHp();
	
	void setHp(int newAttHp);
	
	int getFuel();
	
	void move(int distance);
	
	void attack();
	
	int getViewDirection();
	
	void setViewDirection(int connectionId);
	
	void levelUp();
	
	int getLevel();
	
	
	Unit copy();
	
}
