package online.money_daisuki.gaming.tbs.models.game.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.utils.query.TakingTaskQuery;
import online.money_daisuki.gaming.tbs.models.game.EndTurnResponse;
import online.money_daisuki.gaming.tbs.models.game.GameDataReponse;
import online.money_daisuki.gaming.tbs.models.game.UnitAttackedResponse;
import online.money_daisuki.gaming.tbs.models.game.UnitMovedEvent;
import online.money_daisuki.gaming.tbs.models.game.businessLevelModel.BusinessLevelModel;

public final class NetworkClientBusinessLevelModel implements BusinessLevelModel {
	private final ObjectOutputStream out;
	private final ObjectInputStream in;
	
	private final TakingTaskQuery query;
	
	public NetworkClientBusinessLevelModel(final ObjectInputStream in, final ObjectOutputStream out) {
		this.in = Requires.notNull(in, "in == null");
		this.out = Requires.notNull(out, "out == null");
		this.query = new TakingTaskQuery();
	}
	
	@Override
	public void moveUnit(final Deque<Integer> tiles, final DataSink<? super UnitMovedEvent> callback) {
		final Deque<Integer> copy = new LinkedList<>(tiles);
		query.invoke(new Runnable() {
			@Override
			public void run() {
				write(new UnitMoveRequest(copy));
				final UnitMovedEvent answer = (UnitMovedEvent) read();
				callback.sink(answer);
			}
		});
	}
	@Override
	public void attackUnit(final Deque<Integer> tiles, final DataSink<? super UnitAttackedResponse> callback) {
		final Deque<Integer> copy = new LinkedList<>(tiles);
		query.invoke(new Runnable() {
			@Override
			public void run() {
				write(new UnitAttackRequest(copy));
				final UnitAttackedResponse answer = (UnitAttackedResponse) read();
				callback.sink(answer);
			}
		});
	}
	@Override
	public void endTurn(final int playerId, final DataSink<? super EndTurnResponse> callback) {
		query.invoke(new Runnable() {
			@Override
			public void run() {
				write(new EndTurnRequest());
				final EndTurnResponse answer = (EndTurnResponse) read();
				callback.sink(answer);
			}
		});
	}
	@Override
	public void getGameData(final DataSink<? super GameDataReponse> callback) {
		query.invoke(new Runnable() {
			@Override
			public void run() {
				write(new GameDataRequest());
				final GameDataReponse answer = (GameDataReponse) read();
				callback.sink(answer);
			}
		});
	}
	public void sendLevelName(final DataSink<? super SendLevelDataResponse> callback) {
		query.invoke(new Runnable() {
			@Override
			public void run() {
				write(new SendLevelDataRequest());
				final SendLevelDataResponse answer = (SendLevelDataResponse) read();
				callback.sink(answer);
			}
		});
	}
	
	@Override
	public void waitForPlayerTurn(final int playerId, final DataSink<WaitForPlayerTurnResponse> callback) {
		query.invoke(new Runnable() {
			@Override
			public void run() {
				write(new WaitForPlayerTurnRequest());
				final WaitForPlayerTurnResponse answer = (WaitForPlayerTurnResponse) read();
				callback.sink(answer);
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
	
	public void run() {
		query.run();
	}
	public void stop() {
		query.stop();
	}
}
