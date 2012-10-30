package brandon.gameServer;

import java.util.Collection;

import brandon.gameData.GameServer;

public abstract class GameServerFactory {
	
	protected void addGameServer(GameServer gameServer) {
	}

	/** Return the SettlersServer object for the server id requested */
	public abstract GameServer getGameServer(int getId);
	
    /** Return a collection of game servers currently running */
    public abstract Collection<? extends GameServer> getGameServers(); 
    
    /** Override this method to return the correct 'GameServer' objects */
    public abstract GameServer newGameServer(String gameName, int gameType);
 
}
