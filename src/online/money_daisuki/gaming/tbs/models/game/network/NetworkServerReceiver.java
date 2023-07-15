package online.money_daisuki.gaming.tbs.models.game.network;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.gaming.tbs.models.game.EndTurnResponse;
import online.money_daisuki.gaming.tbs.models.game.FailedUnitMovedEvent;
import online.money_daisuki.gaming.tbs.models.game.GameDataReponse;
import online.money_daisuki.gaming.tbs.models.game.Player;
import online.money_daisuki.gaming.tbs.models.game.UnitAttackedResponse;
import online.money_daisuki.gaming.tbs.models.game.UnitMovedEvent;
import online.money_daisuki.gaming.tbs.models.game.businessLevelModel.LocalBusinessLevelModel;

public final class NetworkServerReceiver implements Runnable, Closeable {
	private final ObjectOutputStream out;
	private final ObjectInputStream in;
	
	private final Player[] players;
	
	private final LocalBusinessLevelModel model;
	
	private final Map<Class<?>, DataSink<Object>> handlers;
	private final int playerId;
	
	private final LevelDataModel levelDataModel;
	
	public NetworkServerReceiver(final ObjectInputStream in, final ObjectOutputStream out,
			final LocalBusinessLevelModel model, final int playerId, final Player[] players,
			final LevelDataModel levelDataModel) {
		this.in = Requires.notNull(in, "in == null");
		this.out = Requires.notNull(out, "out == null");
		this.model = Requires.notNull(model, "model == null");
		this.playerId = Requires.positive(playerId, "playerId < 0");
		this.players = Requires.containsNotNull(Arrays.copyOf(players, Requires.notNull(players).length));
		this.levelDataModel = Requires.notNull(levelDataModel, "levelDataModel < 0");
		
		handlers = new HashMap<>();
		handlers.put(SendLevelDataRequest.class, new SendLevelDataHandler());
		handlers.put(GameDataRequest.class, new GameDataHandler());
		handlers.put(UnitMoveRequest.class, new MoveHandler());
		handlers.put(UnitAttackRequest.class, new AttackHandler());
		handlers.put(WaitForPlayerTurnRequest.class, new WaitForPlayerTurnHandler());
		handlers.put(EndTurnRequest.class, new EndTurnHandler());
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
	
	private void write(final Serializable value) {
		try {
			out.writeObject(value);
			out.flush();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void close() throws IOException {
		out.close();
		in.close();
	}
	
	private final class MoveHandler implements DataSink<Object> {
		@Override
		public void sink(final Object value) {
			final UnitMoveRequest event = (UnitMoveRequest) value;
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
	private final class AttackHandler implements DataSink<Object> {
		@Override
		public void sink(final Object value) {
			final UnitAttackRequest event = (UnitAttackRequest) value;
			if(model.getCurrentPlayerId() != playerId) {
				write(new FailedUnitMovedEvent());
				return;
			}
			
			final Deque<Integer> tiles = new LinkedList<>(Requires.containsNotNull(Requires.notNull(event.getTiles())));
			
			model.attackUnit(tiles, new DataSink<UnitAttackedResponse>() {
				@Override
				public void sink(final UnitAttackedResponse value) {
					write(value);
				}
			});
		}
	}
	private final class SendLevelDataHandler implements DataSink<Object> {
		@Override
		public void sink(final Object value) {
			write(new SendLevelDataResponse(levelDataModel.getLevelName(), levelDataModel.getPackName(),
					players));
		}
	}
	private final class GameDataHandler implements DataSink<Object> {
		@Override
		public void sink(final Object value) {
			model.getGameData(new DataSink<GameDataReponse>() {
				@Override
				public void sink(final GameDataReponse value) {
					write(value);
				}
			});
		}
	}
	private final class WaitForPlayerTurnHandler implements DataSink<Object> {
		@Override
		public void sink(final Object value) {
			model.waitForPlayerTurn(playerId, new DataSink<WaitForPlayerTurnResponse>() {
				@Override
				public void sink(final WaitForPlayerTurnResponse value) {
					write(new WaitForPlayerTurnResponse(playerId));
				}
			});
		}
	}
	private final class EndTurnHandler implements DataSink<Object> {
		@Override
		public void sink(final Object value) {
			model.endTurn(playerId, new DataSink<EndTurnResponse>() {
				@Override
				public void sink(final EndTurnResponse value) {
					write(new EndTurnResponse(playerId));
				}
			});
		}
	}
}
