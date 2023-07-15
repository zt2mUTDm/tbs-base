package online.money_daisuki.gaming.tbs.models.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import online.money_daisuki.api.base.Requires;

public final class TileTemplateImpl implements TileTemplate {
	private final String name;
	private final int defensive;
	private final Map<Drive, Integer> enterCosts;
	private final Map<Drive, Integer> exitCosts;
	
	public TileTemplateImpl(final String name, final int defensive,
			final Map<Drive, Integer> enterCosts,
			final Map<Drive, Integer> exitCosts) {
		this.name = Requires.notNull(name, "name == null");
		this.defensive = defensive;
		this.enterCosts = Requires.containsNotNull(new HashMap<>(Requires.notNull(enterCosts, "enterCosts == null")));
		this.exitCosts = Requires.containsNotNull(new HashMap<>(Requires.notNull(exitCosts, "exitCosts == null")));
	}
	
	@Override
	public String getName() {
		return (name);
	}
	@Override
	public int getDefensive() {
		return (defensive);
	}
	
	@Override
	public boolean hasExitCost(final Drive drive) {
		return(exitCosts.containsKey(drive));
	}
	
	@Override
	public int getExitCosts(final Drive drive) {
		return (exitCosts.get(drive));
	}
	
	@Override
	public boolean hasEnterCost(final Drive drive) {
		return(enterCosts.containsKey(drive));
	}
	
	@Override
	public int getEnterCosts(final Drive drive) {
		return (enterCosts.get(drive));
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(obj == this) {
			return(true);
		} else if(obj.getClass() != getClass()) {
			return(false);
		}
		final TileTemplateImpl cast = (TileTemplateImpl) obj;
		return(cast.name.equals(name) && cast.defensive == defensive && cast.enterCosts.equals(enterCosts) &&
				cast.exitCosts.equals(exitCosts));
	}
	@Override
	public int hashCode() {
		return(Objects.hash(name, defensive, enterCosts, exitCosts));
	}
	
	@Override
	public String toString() {
		return(getClass().getName() + "[name=" + name + ",defensive=" + defensive + ",enterCosts=" + enterCosts +
				",exitCosts=" + exitCosts + "]");
	}
}
