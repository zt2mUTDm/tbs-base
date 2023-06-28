package online.money_daisuki.gaming.tbs.models.data;

import java.util.Arrays;
import java.util.Objects;

import online.money_daisuki.api.base.Requires;

public final class UnitTemplateImpl implements UnitTemplate {
	private final String name;
	private final Weapon[] weapons;
	private final int defense;
	private final int moveDistance;
	private final Drive drive;
	private final boolean moveXorAttack;
	private final boolean canCapturing;
	private final boolean driveByShooting;
	private final int maxFuel;
	private final int sight;
	private final int weight;
	private final int cargo;
	private final String[][] images;
	
	public UnitTemplateImpl(final String name, final Weapon[] weapons, final int defense, final int moveDistance,
			final Drive drive, final boolean moveXorAttack, final boolean canCapturing,
			final boolean driveByShooting, final int maxFuel, final int sight, final int weight,
			final int cargo, final String[][] images) {
		this.name = Requires.notNull(name, "name == null");
		this.weapons = Requires.containsNotNull(Arrays.copyOf(weapons, Requires.notNull(weapons, "weapons == null").length));
		this.defense = Requires.positive(defense, "defense < 0");
		this.moveDistance = Requires.positive(moveDistance, "moveDistance < null");
		this.drive = Requires.notNull(drive, "drive == null");
		this.moveXorAttack = moveXorAttack;
		this.canCapturing = canCapturing;
		this.driveByShooting = driveByShooting;
		this.maxFuel = Requires.positive(maxFuel, "maxFuel < 0");
		this.sight = Requires.positive(sight, "sight < 0");
		this.weight = Requires.greaterThanZero(weight, "weight < 0");
		this.cargo = Requires.positive(cargo, "cargo < 0");
		this.images = Requires.notNull(copy2DArray(images), "images == null");
	}
	private <T> T[][] copy2DArray(final T[][] arr) {
		final T[][] newArr = Arrays.copyOf(arr, arr.length);
		for(int i = 0, size = newArr.length; i < size; i++) {
			final T[] inner = Requires.containsNotNull(Requires.notNull(arr[i]));
			newArr[i] = Arrays.copyOf(inner, inner.length);
		}
		return(newArr);
	}
	
	@Override
	public String getName() {
		return (name);
	}
	
	@Override
	public int getDefense() {
		return (defense);
	}
	
	@Override
	public int getMaxFuel() {
		return (maxFuel);
	}
	
	@Override
	public int getMoveDistance() {
		return(moveDistance);
	}
	
	@Override
	public Drive getDrive() {
		return (drive);
	}
	
	@Override
	public int getSight() {
		return (sight);
	}
	
	@Override
	public boolean canDriveByShooting() {
		return(driveByShooting);
	}
	@Override
	public String getImageUrl(final int player, final int i) {
		return(images[player][i]);
	}
	
	@Override
	public int getWeaponCount() {
		return(weapons.length);
	}
	@Override
	public Weapon getWeapon(final int index) {
		return(weapons[index]);
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(obj == this) {
			return(true);
		} else if(obj.getClass() != getClass()) {
			return(false);
		}
		final UnitTemplateImpl cast = (UnitTemplateImpl) obj;
		return(cast.name.equals(name) && Arrays.deepEquals(cast.weapons, weapons) && cast.defense == defense &&
				cast.moveDistance == moveDistance && cast.drive.equals(drive) &&
				cast.moveXorAttack == moveXorAttack && cast.canCapturing == canCapturing &&
				cast.driveByShooting == driveByShooting && cast.maxFuel == maxFuel && cast.sight == sight &&
				cast.weight == weight && cast.cargo == cargo && Arrays.deepEquals(cast.images, images));
	}
	@Override
	public int hashCode() {
		return(Objects.hash(name, Arrays.deepHashCode(weapons), defense, moveDistance, drive, moveXorAttack,
				canCapturing, driveByShooting, maxFuel, sight, weight, cargo, Arrays.deepHashCode(images)));
	}
	
	@Override
	public String toString() {
		return(getClass().getName() + "[name=" + name + ",weapons=" + Arrays.toString(weapons) + ",defense=" + defense +
				",moveXorAttack=" + moveXorAttack + ",canCapturing=" + canCapturing + ",driveByShooting=" + driveByShooting +
				",maxFuel=" + maxFuel + ",sight=" + sight + ",weight=" + weight + ",cargo=" + cargo + ", images=" +
				Arrays.toString(images) + "]");
	}
}
