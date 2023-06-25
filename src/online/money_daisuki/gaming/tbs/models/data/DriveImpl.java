package online.money_daisuki.gaming.tbs.models.data;

import java.util.Objects;

import online.money_daisuki.api.base.Requires;

public final class DriveImpl implements Drive {
	private final String name;
	private final boolean terrainDefenseApplicable;
	private final int fuelLossPerRound;
	
	public DriveImpl(final String name, final boolean terrainDefenseApplicable, final int fuelLossPerRound) {
		this.name = Requires.notNull(name);
		this.terrainDefenseApplicable = terrainDefenseApplicable;
		this.fuelLossPerRound = Requires.positive(fuelLossPerRound, "fuelLossPerRound < 0");
	}
	
	@Override
	public String getName() {
		return (name);
	}
	@Override
	public boolean terrainDefenseApplicable() {
		return (terrainDefenseApplicable);
	}
	@Override
	public int getFuelLossPerRound() {
		return (fuelLossPerRound);
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(obj == this) {
			return(true);
		} else if(obj.getClass() != getClass()) {
			return(false);
		}
		final DriveImpl cast = (DriveImpl) obj;
		return(cast.name.equals(name) && cast.terrainDefenseApplicable == terrainDefenseApplicable &&
				cast.fuelLossPerRound == fuelLossPerRound);
	}
	@Override
	public int hashCode() {
		return(Objects.hash(name, terrainDefenseApplicable, fuelLossPerRound));
	}
	
	@Override
	public String toString() {
		return(getClass().getName() + "[name=" + name + ", terrainDefenseApplicable=" + terrainDefenseApplicable +
				",fuelLossPerRound=" + fuelLossPerRound  + "]");
	}
}
