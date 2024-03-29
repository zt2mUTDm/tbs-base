package online.money_daisuki.gaming.tbs.models.game;

import java.io.Serializable;

import online.money_daisuki.api.base.Requires;

public final class UnitAttackedResponse implements Serializable {
	private final boolean successful;
	
	private final Integer attackTile;
	private final Integer defenseTile;
	
	private final int attackHp;
	private final int defenseHp;
	
	public UnitAttackedResponse(final Integer attackTile, final Integer defenseTile, final int attDmg, final int defDmg) {
		this(true, Requires.notNull(attackTile, "attackTile == null"), Requires.notNull(defenseTile, "defenseTile == null"),
				attDmg, defDmg);
	}
	private UnitAttackedResponse(final boolean successful, final Integer attackTile, final Integer defenseTile,
			final int attackHp, final int defenseHp) {
		this.successful = successful;
		
		this.attackTile = attackTile;
		this.defenseTile = defenseTile;
		
		this.attackHp = Requires.positive(attackHp, "attackHp < 0");
		this.defenseHp = Requires.positive(defenseHp, "defenseHp < 0");
	}
	
	public boolean wasSuccessful() {
		return(successful);
	}
	
	public Integer getAttackTile() {
		if(!wasSuccessful()) {
			throw new IllegalStateException("Wasn't successful");
		}
		return(attackTile);
	}
	public Integer getDefenseTile() {
		if(!wasSuccessful()) {
			throw new IllegalStateException("Wasn't successful");
		}
		return(defenseTile);
	}
	
	public int getAttackHp() {
		return (attackHp);
	}
	public int getDefenseHp() {
		return (defenseHp);
	}
	
	public static UnitAttackedResponse createFailure() {
		return(new UnitAttackedResponse(false, null, null, 0, 0));
	}
}
