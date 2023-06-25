package online.money_daisuki.gaming.tbs.models.game.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Deque;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.utils.query.TakingTaskQuery;
import online.money_daisuki.api.utils.query.TaskQuery;
import online.money_daisuki.gaming.tbs.models.game.UnitAttackedEvent;
import online.money_daisuki.gaming.tbs.models.game.UnitEndEvent;
import online.money_daisuki.gaming.tbs.models.game.UnitMovedEvent;
import online.money_daisuki.gaming.tbs.models.game.UpdateGameDataEvent;
import online.money_daisuki.gaming.tbs.models.game.businessLevelModel.BusinessLevelModel;

public final class NetworkClientBusinessLevelModel implements BusinessLevelModel {
	private final ObjectOutputStream out;
	private final ObjectInputStream in;
	
	private final TaskQuery query;
	
	public NetworkClientBusinessLevelModel(final ObjectInputStream in, final ObjectOutputStream out) {
		this.in = Requires.notNull(in, "in == null");
		this.out = Requires.notNull(out, "out == null");
		this.query = new TakingTaskQuery();
	}
	
	@Override
	public void moveUnit(final Deque<Integer> tiles, final DataSink<? super UnitMovedEvent> callback) {
		query.invoke(new Runnable() {
			@Override
			public void run() {
				write(new ProtocolUnitMoveRequestEvent());
				final UnitMovedEvent answer = (UnitMovedEvent) read();
				callback.sink(answer);
			}
		});
	}
	@Override
	public void attackUnit(final Deque<Integer> tiles, final DataSink<? super UnitAttackedEvent> callback) {
		query.invoke(new Runnable() {
			@Override
			public void run() {
				write(new ProtocolUnitAttackRequestEvent());
				final UnitAttackedEvent answer = (UnitAttackedEvent) read();
				callback.sink(answer);
			}
		});
	}
	@Override
	public void endTurn(final DataSink<? super UnitEndEvent> callback) {
		query.invoke(new Runnable() {
			@Override
			public void run() {
				write(new ProtocolEndTurnRequestEvent());
				final UnitEndEvent answer = (UnitEndEvent) read();
				callback.sink(answer);
			}
		});
	}
	@Override
	public void getGameData(final DataSink<? super UpdateGameDataEvent> callback) {
		query.invoke(new Runnable() {
			@Override
			public void run() {
				//write(new ProtocolEndTurnRequestEvent());
				//final UnitEndEvent answer = (UnitEndEvent) read();
				//callback.sink(answer);
			}
		});
	}
	
	private void write(final Serializable event) {
		try {
			out.writeObject(event);
			out.flush();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	private Object read() {
		try {
			return(in.readObject());
		} catch (final IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
