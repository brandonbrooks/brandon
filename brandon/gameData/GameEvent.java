package brandon.gameData;

import java.io.Serializable;
import java.util.ArrayList;

public class GameEvent implements Serializable {

	public int eventNumber;
	public String eventId;
	public int originatorId;
	public int playerId;
	public ArrayList data = new ArrayList();

	public GameEvent() {
		
	}
	
	/** Create a new game event (originating from no player) */
	public GameEvent(int originatorId, String eventId) {
		this.originatorId = originatorId;
		this.eventId = eventId;
		this.playerId = -1;
	}

	/** Create a new game event originating from a specific player */
	public GameEvent(int originatorId, String eventId, int playerId) {
		this.originatorId = originatorId;
		this.eventId = eventId;
		this.playerId = playerId;
	}

	/**
	 * Return a collection of data associated with this event
	 */
	public ArrayList getData() {
		return data;
	}

}
