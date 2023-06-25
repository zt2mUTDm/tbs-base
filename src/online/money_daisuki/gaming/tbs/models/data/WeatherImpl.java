package online.money_daisuki.gaming.tbs.models.data;

import java.util.Objects;

import online.money_daisuki.api.base.Requires;

public final class WeatherImpl implements Weather {
	private final String name;
	
	public WeatherImpl(final String name) {
		this.name = Requires.notNull(name);
	}
	
	@Override
	public String getName() {
		return (name);
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(obj == this) {
			return(true);
		} else if(obj.getClass() != getClass()) {
			return(false);
		}
		final WeatherImpl cast = (WeatherImpl) obj;
		return(cast.name.equals(name));
	}
	@Override
	public int hashCode() {
		return(Objects.hash(name));
	}
	
	@Override
	public String toString() {
		return(getClass().getName() + "[name=" + name + "]");
	}
}
