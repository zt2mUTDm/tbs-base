package online.money_daisuki.gaming.tbs.models.data;

import java.util.Objects;

import online.money_daisuki.api.base.Requires;

public final class TileConnectionImpl implements TileConnection {
	private final String hasFormula;
	private final String idFormula;
	
	public TileConnectionImpl(final String hasFormula, final String idFormula) {
		this.hasFormula = Requires.notNull(hasFormula, "hasFormula == null");
		this.idFormula = Requires.notNull(idFormula, "idFormula == null");
	}
	@Override
	public String getHasFormula() {
		return (hasFormula);
	}
	@Override
	public String getIdFormula() {
		return (idFormula);
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(obj == this) {
			return(true);
		} else if(obj.getClass() != getClass()) {
			return(false);
		}
		final TileConnectionImpl cast = (TileConnectionImpl) obj;
		return(cast.hasFormula.equals(hasFormula) && cast.idFormula.equals(idFormula));
	}
	@Override
	public int hashCode() {
		return(Objects.hash(hasFormula, idFormula));
	}
	
	@Override
	public String toString() {
		return(getClass().getName() + "[hasFormula=" + hasFormula + ",idFormula=" + idFormula + "]");
	}
}
