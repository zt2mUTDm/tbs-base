package online.money_daisuki.gaming.tbs.models.game;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.gaming.tbs.models.data.UnitTemplate;

public final class UnitImpl implements Unit {
	private final UnitTemplate template;
	private final int playerId;
	
	private int hp;
	private final int fuel;
	
	private boolean canMove;
	private boolean canAttack;
	
	private int moveDistanceLeft;
	
	private int viewDirection;
	
	private int level;
	
	public UnitImpl(final UnitTemplate template, final int playerId, final int hp, final int fuel) {
		this.template = Requires.notNull(template, "template == null");
		this.playerId = Requires.positive(playerId, "playerId == null");
		this.hp = Requires.positive(hp, "hp == null");
		this.fuel = Requires.positive(fuel, "fuel == null");
		
		reset();
	}
	@Override
	public boolean canMove() {
		return(canMove);
	}
	
	@Override
	public int getPlayerId() {
		return(playerId);
	}
	@Override
	public UnitTemplate getTemplate() {
		return(template);
	}
	
	@Override
	public int getHp() {
		return (hp);
	}
	@Override
	public void setHp(final int hp) {
		this.hp = Requires.greaterThanZero(hp, "hp <= 0");
	}
	
	@Override
	public int getFuel() {
		return(fuel);
	}
	
	@Override
	public void move(final int distance) {
		moveDistanceLeft-= distance;
		//fuel-= distance;
		canMove = false;
		
		if(template.canMoveXorAttack()) {
			canAttack = false;
		}
	}
	@Override
	public void attack() {
		canAttack = false;
		if(template.canDriveByShooting()) {
			canMove = true;
		} else if(template.canMoveXorAttack()) {
			canMove = false;
		}
	}
	
	@Override
	public void reset() {
		moveDistanceLeft = template.getMoveDistance();
		if(moveDistanceLeft > 0) {
			canMove = true;
		}
		canAttack = true;
	}
	
	@Override
	public boolean canAttack() {
		return(canAttack);
	}
	
	@Override
	public int getViewDirection() {
		return (viewDirection);
	}
	@Override
	public void setViewDirection(final int viewDirection) {
		this.viewDirection = Requires.positive(viewDirection, "viewDirection < 0");
	}
	
	@Override
	public void levelUp() {
		level = Math.min(level + 1, 10);
	}
	@Override
	public int getLevel() {
		return (level);
	}
	
	@Override
	public String toString() {
		return(getClass().getName() + "[template=" + template + ",playerId=" + playerId + ",hp=" + hp + ",fuel=" + fuel +
				",canMove=" + canMove + ",canAttack=" + canAttack + ",moveDistanceLeft=" + moveDistanceLeft +
				",viewDirection=" + viewDirection + "]");
	}
	
	@Override
	public UnitImpl copy() {
		final UnitImpl newUnit = new UnitImpl(template, playerId, hp, fuel);
		newUnit.canMove = canMove;
		newUnit.canAttack = canAttack;
		newUnit.moveDistanceLeft = moveDistanceLeft;
		newUnit.viewDirection = viewDirection;
		newUnit.level = level;
		
		return(newUnit);
	}
}
