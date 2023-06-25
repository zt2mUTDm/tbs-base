package online.money_daisuki.gaming.tbs.models.game;

import java.util.Objects;

import online.money_daisuki.api.base.Requires;

public final class PlayerImpl implements Player {
	private final boolean enabled;
	private final String name;
	private final int color;
	
	public PlayerImpl(final boolean enabled, final String name, final int color) {
		this.enabled = enabled;
		this.name = Requires.notNull(name, "name == null");
		this.color = color;
	}
	
	@Override
	public boolean isEnabled() {
		return (enabled);
	}
	@Override
	public String getName() {
		return (name);
	}
	@Override
	public int getColor() {
		return (color);
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(obj == this) {
			return(true);
		} else if(obj.getClass() != getClass()) {
			return(false);
		}
		final PlayerImpl cast = (PlayerImpl) obj;
		return(cast.name.equals(name) && cast.color == color);
	}
	@Override
	public int hashCode() {
		return(Objects.hash(name, color));
	}
	
	@Override
	public String toString() {
		return(getClass().getName() + "[name=" + name + ",color=" + color + "]");
	}

}
