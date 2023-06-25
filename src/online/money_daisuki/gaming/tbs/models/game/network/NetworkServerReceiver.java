package online.money_daisuki.gaming.tbs.models.game.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.gaming.tbs.models.game.FailedUnitMovedEvent;
import online.money_daisuki.gaming.tbs.models.game.UnitMovedEvent;
import online.money_daisuki.gaming.tbs.models.game.businessLevelModel.LocalBusinessLevelModel;

public final class NetworkServerReceiver implements Runnable {
	private final ObjectOutputStream out;
	private final ObjectInputStream in;
	
	private final LocalBusinessLevelModel model;
	
	private final Map<Class<?>, DataSink<Object>> handlers;
	private final int playerId;
	
	public NetworkServerReceiver(final ObjectInputStream in, final ObjectOutputStream out,
			final LocalBusinessLevelModel model, final int playerId) {
		this.in = Requires.notNull(in, "in == null");
		this.out = Requires.notNull(out, "out == null");
		this.model = Requires.notNull(model, "model == null");
		this.playerId = Requires.positive(playerId, "playerId < 0");
		
		handlers = new HashMap<>();
		handlers.put(ProtocolUnitMoveRequestEvent.class, new MoveHandler());
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				final Object obj = in.readObject();
				handlers.get(obj.getClass()).sink(obj);
			} catch (ClassNotFoundException | IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private void write(final Object value) {
		try {
			out.writeObject(value);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private final class MoveHandler implements DataSink<Object> {
		@Override
		public void sink(final Object value) {
			final ProtocolUnitMoveRequestEvent event = (ProtocolUnitMoveRequestEvent) value;
			if(model.getCurrentPlayerId() != playerId) {
				write(new FailedUnitMovedEvent());
				return;
			}
			
			final Deque<Integer> tiles = new LinkedList<>(Requires.containsNotNull(Requires.notNull(event.getTiles())));
			
			model.moveUnit(tiles, new DataSink<UnitMovedEvent>() {
				@Override
				public void sink(final UnitMovedEvent value) {
					write(value);
				}
			});
		}
	}
}
