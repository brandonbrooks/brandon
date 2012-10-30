package brandon.gameData;

import java.util.ArrayList;
import brandon.utils.CardDeck;
import brandon.utils.Log;

/** This interface designs what must be known about a game
 *  by the GameServer
 */
public abstract class Game {
	public abstract ArrayList<? extends Player> getPlayers();
    public abstract ArrayList<? extends GameEvent> getEvents();
    public abstract ArrayList<? extends GameEvent> getEvents(int startingEventNum);
	
	public Player getStartingPlayer() {
		return null;
	}
	public Player getPlayerTurn() {
		return null;
	}
	public Player getPlayer(int playerNum) {
        return null;   
    }

    public int addPlayer(Player player) {
    	Log.error(this, "addPlayer", "called. Subclass shoule be called instead.");
    	return -1;
    }

    public void addEvent(GameEvent event) {
    	Log.error(this, "addEvent", "called. Subclass should be called instead.");
    }
	
	public void setBoard(GameBoard gb) {
	}
	public void setCardDeck(CardDeck cd) {
	}
	
}
