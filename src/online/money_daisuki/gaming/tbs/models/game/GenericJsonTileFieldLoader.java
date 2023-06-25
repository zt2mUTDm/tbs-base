package online.money_daisuki.gaming.tbs.models.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.io.json.JsonList;

public final class GenericJsonTileFieldLoader implements DataSource<TileField> {
	private final JsonList list;
	private final int maxConnectionCount;
	
	public GenericJsonTileFieldLoader(final JsonList list, final int maxConnectionCount) {
		this.list = Requires.notNull(list, "list == null");
		this.maxConnectionCount = Requires.greaterThanZero(maxConnectionCount, "maxConnectionCount <= 0");
	}
	@Override
	public TileField source() {
		final int tileCount = list.size();
		
		final int[] tileTypes = new int[tileCount];
		for(int i = 0; i < tileCount; i++) {
			tileTypes[i] = -1;
		}
		final Map<Integer, List<Integer>> tileConnections = new HashMap<>();
		
		final Deque<Integer> toWork = new LinkedList<>();
		toWork.addFirst(0);
		
		while(!toWork.isEmpty()) {
			final Integer id = toWork.removeLast();
			
			if(tileTypes[id] != -1) {
				continue;
			}
			
			final JsonList tileList = list.get(id).asList();
			Requires.equal(tileList.size(), maxConnectionCount + 1);
			
			tileTypes[id] = tileList.get(0).asData().asNumber().asBigInteger().intValueExact();
			tileConnections.put(id, new ArrayList<>(maxConnectionCount));
			
			final List<Integer> list = tileConnections.get(id);
			
			for(int i = 1, size = tileList.size(); i < size; i++) {
				final int nextTile = tileList.get(i).asData().asNumber().asBigInteger().intValueExact();
				
				if(nextTile != -1) {
					list.add(nextTile);
					toWork.add(nextTile);
				} else {
					list.add(null);
				}
			}
		}
		return(new ConnectionTileField(tileTypes, tileConnections));
	}
	
	private final class ConnectionTileField implements TileField {
		private final int[] tileTypes;
		private final Map<Integer, List<Integer>> tileConnections;
		
		public ConnectionTileField(final int[] tileTypes, final Map<Integer, List<Integer>> tileConnections) {
			this.tileTypes = Arrays.copyOf(tileTypes, Requires.notNull(tileTypes, "tileTypes == null").length);
			this.tileConnections = copyConnections(Requires.notNull(tileConnections, "tileConnections == null"));
		}
		private Map<Integer, List<Integer>> copyConnections(final Map<Integer, List<Integer>> connections) {
			final Map<Integer, List<Integer>> map = new HashMap<>();
			for(final Entry<Integer, List<Integer>> e:connections.entrySet()) {
				map.put(e.getKey(), new ArrayList<>(e.getValue()));
			}
			return(map);
		}
		
		@Override
		public int getTileType(final int id) {
			return(tileTypes[id]);
		}
		@Override
		public int getTileCount() {
			return(tileTypes.length);
		}
		@Override
		public Integer getConnectionCount(final int id) {
			final List<Integer> connections = tileConnections.get(id);
			return(connections != null ? connections.size() : 0);
		}
		@Override
		public Integer getConnectedTile(final int id, final int connection) {
			final List<Integer> connections = tileConnections.get(id);
			return(connections.get(connection));
		}
		@Override
		public boolean isTileConnected(final int id, final int connection) {
			final List<Integer> connections = tileConnections.get(id);
			return(connections.get(connection) != null);
		}
	}
}
