package brandon.gameServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.servlet.http.HttpServletRequest;
import brandon.utils.Log;
import brandon.gameData.Game;

public abstract class ServerCommand {

	protected ObjectOutputStream out;
	protected HttpServletRequest request;
	protected Integer playerNum;

	public ServerCommand() {
	}

	public void setObjectOutputStream(ObjectOutputStream out) {
		this.out = out;
	}

	public abstract void setGame(Game game);
	
	public abstract Game getGame();
	
	public void setServletRequest(HttpServletRequest request) {
		final String methodName = "setServletRequest(HttpServletRequest)";
		this.request = request;

		String playerNumString = null;
		try {
			playerNumString = request.getParameter("playerNum");
			if (playerNumString != null) {
				playerNum = new Integer(playerNumString);
			}
		} catch (NumberFormatException e) {
			Log.error(this, methodName, "Number Format EXCEPTION for player [" + playerNumString + "]");
		}

	}

	protected abstract void execute() throws IOException;

	protected void sendAck() throws IOException {
		out.writeObject(new Integer(1));
	}

	public void runCommand() {
		final String methodName = "runCommand";
		try {
			execute();
			out.flush();
			out.close();
		} catch (IOException e) {
			// Report IOException error here
			Log.error(this, methodName, "Command " + this.getClass().getName() + " threw an IOException "	+ e);
			e.printStackTrace();
		}
	}

}
