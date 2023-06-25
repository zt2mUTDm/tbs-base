package online.money_daisuki.gaming.tbs.models.game;

import java.util.HashMap;
import java.util.Map;

import online.money_daisuki.api.algorithms.graph.pathfinding.shortest.bruteforce.GraphMovecostCalculatorModel;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.gaming.tbs.models.data.UnitTemplate;
import online.money_daisuki.gaming.tbs.models.game.businessLevelModel.TileGraphViewCalculatorSubModel;

public final class TileGraphViewCalculatorModel implements GraphMovecostCalculatorModel<Integer> {
	private final TileGraphViewCalculatorSubModel model;
	
	private final Integer start;
	
	private final UnitTemplate unitTemp;
	
	private final Map<Integer, Integer> costs;

	public TileGraphViewCalculatorModel(final Integer start, final UnitTemplate unitTemp, final TileGraphViewCalculatorSubModel model) {
		this.start = Requires.notNull(start, "start == null");
		this.unitTemp = Requires.notNull(unitTemp, "unitTemp == null");
		this.model = Requires.notNull(model, "model == null");
		
		this.costs = new HashMap<>();
	}
	
	@Override
	public Integer getStartNode() {
		return(start);
	}
	
	@Override
	public int getSuccessorsCount(final Integer node) {
		return(model.getTileConnectionCount(node));
	}
	
	@Override
	public Integer getSuccessor(final Integer node, final int i) {
		return(model.getConnectedTile(node, i));
	}
	
	@Override
	public boolean hasSuccessor(final Integer node, final int i) {
		return(model.isTileConnected(node, i));
	}
	
	@Override
	public int getMaxOverallCosts() {
		return(unitTemp.getSight());
	}

	@Override
	public int getCostsBetweenNodes(final Integer from, final Integer to) {
		return(1);
	}
	
	@Override
	public boolean hasCosts(final Integer node) {
		return(costs.containsKey(node));
	}
	
	@Override
	public int getCosts(final Integer node) {
		return(costs.get(node));
	}
	
	@Override
	public void setCost(final Integer node, final int c) {
		costs.put(node, c);
	}
	
	@Override
	public Map<Integer, Integer> getAllOverallCosts() {
		return(new HashMap<>(costs));
	}
}
