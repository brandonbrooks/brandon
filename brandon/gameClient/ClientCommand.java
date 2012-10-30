package brandon.gameClient;

import brandon.gameData.GameEvent;

public abstract class ClientCommand {

	public abstract void setGameEvent(GameEvent gameEvent);

	// Just a comment
	/** Executes the command call */
	public abstract void execute();
}
