package online.money_daisuki.gaming.tbs.models.game.network;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import online.money_daisuki.api.base.NoDataListenerContainer;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.gaming.tbs.models.game.Player;
import online.money_daisuki.gaming.tbs.models.game.Player.PlayerType;
import online.money_daisuki.gaming.tbs.models.game.businessLevelModel.LocalBusinessLevelModel;

public final class LinearCommunicationServerManager {
	private boolean closed;
	
	private final ServerSocket ss;
	private final Player[] players;
	private final LocalBusinessLevelModel businessLevel;
	private final LevelDataModel levelDataModel;
	
	private final Map<Integer, Closeable> receivers;
	
	private final NoDataListenerContainer startListenersContainer;
	
	public LinearCommunicationServerManager(final ServerSocket ss, final Player[] players,
			final LocalBusinessLevelModel businessLevel, final LevelDataModel levelDataModel) {
		this.ss = Requires.notNull(ss, "ss == null");
		this.players = Requires.containsNotNull(Arrays.copyOf(players, Requires.notNull(players).length));
		this.businessLevel = Requires.notNull(businessLevel, "businessLevel == null");
		this.levelDataModel = Requires.notNull(levelDataModel, "levelDataModel == null");
		
		this.receivers = new HashMap<>();
		
		this.startListenersContainer = new NoDataListenerContainer();
	}
	public void run() {
		final Deque<Integer> networkPlayerIds = new LinkedList<>();
		for(int i = 0, size = players.length; i < size; i++) {
			if(players[i].getType() == PlayerType.NETWORK) {
				networkPlayerIds.add(i);
			}
		}
		if(networkPlayerIds.size() == 0) {
			fireStartedListeners();
			return;
		}
		
		while(!networkPlayerIds.isEmpty()) {
			try {
				final Socket socket = ss.accept();
				
				final ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
				final ObjectInputStream oin = new ObjectInputStream(socket.getInputStream());
				
				final Integer playerId = networkPlayerIds.removeFirst();
				
				final int playerCount = players.length;
				final Player[] clientPlayers = new Player[playerCount];
				for(int i = 0; i < playerCount; i++) {
					final Player serverPlayer = players[i];
					switch(serverPlayer.getType()) {
						case LOCAL:
						case NETWORK:
							if(i == playerId) {
								clientPlayers[i] = serverPlayer.replaceType(PlayerType.LOCAL);
							} else {
								clientPlayers[i] = serverPlayer.replaceType(PlayerType.NETWORK);
							}
						break;
						case DISABLED:
							clientPlayers[i] = serverPlayer;
						break;
						default:
							throw new UnsupportedOperationException("Unknown enum constrain: " + serverPlayer.getType());
					}
				}
				
				final NetworkServerReceiver recv = new NetworkServerReceiver(oin, oout, businessLevel,
						playerId, clientPlayers, levelDataModel);
				receivers.put(playerId, new Closeable() {
					@Override
					public void close() throws IOException {
						recv.close();
						socket.close();
					}
				});
				
				new Thread(new Runnable() {
					@Override
					public void run() {
						recv.run();
					}
				}).start();
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}
		fireStartedListeners();
	}
	
	public void addStartedListener(final Runnable l) {
		startListenersContainer.addListener(Requires.notNull(l, "l == null"));
	}
	public Collection<Runnable> getStartedListeners() {
		return(startListenersContainer.getListeners());
	}
	public boolean removeStartedListener(final Runnable l) {
		return(startListenersContainer.removeListener(Requires.notNull(l, "l == null")));
	}
	public void clearStartedListeners() {
		startListenersContainer.clearListeners();
	}
	
	private void fireStartedListeners() {
		startListenersContainer.fireListeners();
	}
	
	public void close() {
		for(final Closeable c:receivers.values()) {
			try {
				c.close();
			} catch (final IOException e) {
				System.err.println("Closing failed: " + e.getMessage());
			}
		}
		
		try {
			ss.close();
		} catch (final IOException e) {
			System.err.println("Closing failed: " + e.getMessage());
		}
		
		receivers.clear();
		closed = true;
	}
	public boolean isClosed() {
		return(closed);
	}
}
