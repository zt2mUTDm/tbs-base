package online.money_daisuki.gaming.tbs.models.data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import online.money_daisuki.api.base.Requires;

public final class WeaponImpl implements Weapon {
	private final String name;
	private final Ammo ammo;
	private final int maxAmmo;
	private final int minDistance;
	private final int maxDistance;
	private final int strength;
	private final Set<Drive> canAttack;
	private final boolean canGetCounterstriked;
	
	public WeaponImpl(final String name, final Ammo ammo, final int maxAmmo, final int minDistance, final int maxDistance,
			final int strength, final Set<Drive> canAttack, final boolean canGetCounterstriked) {
		this.name = Requires.notNull(name, "name == null");
		this.ammo = Requires.notNull(ammo, "ammo == null");
		this.maxAmmo = Requires.positive(maxAmmo, "maxAmmo < 0");
		this.minDistance = Requires.positive(minDistance, "minDistance < 0");
		this.maxDistance = Requires.positive(maxDistance, "maxDistance < 0");
		this.strength = Requires.positive(strength, "strength < 0");
		this.canAttack = new HashSet<>(Requires.notNull(canAttack, "canAttack == null"));
		this.canGetCounterstriked = canGetCounterstriked;
	}
	
	@Override
	public String getName() {
		return (name);
	}
	@Override
	public Ammo getAmmo() {
		return (ammo);
	}
	@Override
	public int getMaxAmmo() {
		return (maxAmmo);
	}
	@Override
	public int getMinDistance() {
		return (minDistance);
	}
	@Override
	public int getMaxDistance() {
		return (maxDistance);
	}
	@Override
	public int getStrength() {
		return (strength);
	}
	@Override
	public boolean canAttack(final Drive drive) {
		return (canAttack.contains(Requires.notNull(drive, "drive == null")));
	}
	@Override
	public boolean canGetCounterstriked() {
		return(canGetCounterstriked);
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(obj == this) {
			return(true);
		} else if(obj.getClass() != getClass()) {
			return(false);
		}
		final WeaponImpl cast = (WeaponImpl) obj;
		return(cast.name.equals(name) && cast.ammo.equals(ammo) && cast.maxAmmo == maxAmmo &&
				cast.minDistance == minDistance && cast.maxDistance == maxDistance && cast.strength == strength &&
				cast.canAttack.equals(canAttack) && cast.canGetCounterstriked == canGetCounterstriked);
	}
	@Override
	public int hashCode() {
		return(Objects.hash(name, ammo, maxAmmo, minDistance, maxDistance, strength, canAttack, canGetCounterstriked));
	}
	
	@Override
	public String toString() {
		return(getClass().getName() + "[name=" + name + ",ammo=" + ammo + ",maxAmmo=" + maxAmmo +
				",minDistance=" + minDistance + ",maxDistance=" + maxDistance + ",attack=" + strength +
				",canAttack=" + canAttack + ",canGetCounterstriked=" + canGetCounterstriked + "]");
	}
}
