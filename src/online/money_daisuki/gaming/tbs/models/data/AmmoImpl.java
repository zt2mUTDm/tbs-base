package online.money_daisuki.gaming.tbs.models.data;

import java.util.Objects;

import online.money_daisuki.api.base.Requires;

public final class AmmoImpl implements Ammo {
	private final String name;
	private final AmmoType type;
	
	public AmmoImpl(final String name, final AmmoType type) {
		this.name = Requires.notNull(name, "name == null");
		this.type = Requires.notNull(type, "type == null");
	}
	
	@Override
	public String getName() {
		return (name);
	}
	@Override
	public AmmoType getType() {
		return (type);
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(obj == this) {
			return(true);
		} else if(obj.getClass() != getClass()) {
			return(false);
		}
		final AmmoImpl cast = (AmmoImpl) obj;
		return(cast.name.equals(name) && cast.type.equals(type));
	}
	@Override
	public int hashCode() {
		return(Objects.hash(name, type));
	}
	
	@Override
	public String toString() {
		return(getClass().getName() + "[name=" + name + ",type=" + type + "]");
	}
}
