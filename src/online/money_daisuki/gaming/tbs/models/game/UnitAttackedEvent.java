package online.money_daisuki.gaming.tbs.models.game;

import online.money_daisuki.api.base.Requires;

public final class UnitAttackedEvent {
	private final boolean successful;
	
	private final Unit attackUnit;
	private final Unit defenseUnit;
	
	private final int attackHp;
	private final int defenseHp;
	
	public UnitAttackedEvent(final Unit attackUnit, final Unit defenseUnit, final int attDmg, final int defDmg) {
		this(true, Requires.notNull(attackUnit, "attackUnit == null"), Requires.notNull(defenseUnit, "defenseUnit == null"),
				attDmg, defDmg);
	}
	private UnitAttackedEvent(final boolean successful, final Unit attackUnit, final Unit defenseUnit,
			final int attackHp, final int defenseHp) {
		this.successful = successful;
		
		this.attackUnit = attackUnit;
		this.defenseUnit = defenseUnit;
		
		this.attackHp = Requires.positive(attackHp, "attackHp < 0");
		this.defenseHp = Requires.positive(defenseHp, "defenseHp < 0");
	}
	
	public boolean wasSuccessful() {
		return(successful);
	}
	
	public Unit getAttackUnit() {
		if(!wasSuccessful()) {
			throw new IllegalStateException("Wasn't successful");
		}
		return(attackUnit);
	}
	public Unit getDefenseUnit() {
		if(!wasSuccessful()) {
			throw new IllegalStateException("Wasn't successful");
		}
		return(defenseUnit);
	}
	
	public int getAttackHp() {
		return (attackHp);
	}
	public int getDefenseHp() {
		return (defenseHp);
	}
	
	public static UnitAttackedEvent createFailure() {
		return(new UnitAttackedEvent(false, null, null, 0, 0));
	}
}
