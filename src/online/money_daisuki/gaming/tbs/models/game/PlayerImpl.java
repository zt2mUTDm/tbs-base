package online.money_daisuki.gaming.tbs.models.game;

import java.util.Objects;

import online.money_daisuki.api.base.Requires;

public final class PlayerImpl implements Player {
	private final PlayerType type;
	private final String name;
	private final int color;
	
	public PlayerImpl(final PlayerType type, final String name, final int color) {
		this.type = Requires.notNull(type, "type == null");;
		this.name = Requires.notNull(name, "name == null");
		this.color = color;
	}
	
	@Override
	public PlayerType getType() {
		return (type);
	}
	@Override
	public String getName() {
		return (name);
	}
	@Override
	public int getColor() {
		return (color);
	}
	
	public PlayerImpl replaceType(final PlayerType newType) {
		if(newType == type) {
			return(this);
		} else {
			return(new PlayerImpl(newType, name, color));
		}
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(obj == this) {
			return(true);
		} else if(obj.getClass() != getClass()) {
			return(false);
		}
		final PlayerImpl cast = (PlayerImpl) obj;
		return(cast.type == type && cast.name.equals(name) && cast.color == color);
	}
	@Override
	public int hashCode() {
		return(Objects.hash(type, name, color));
	}
	
	@Override
	public String toString() {
		return(getClass().getName() + "[type=" + type + ",name=" + name + ",color=" + color + "]");
	}
	
}
