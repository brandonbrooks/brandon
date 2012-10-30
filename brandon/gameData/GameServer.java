package brandon.gameData;

import java.io.File;
import java.util.Calendar;
import java.util.ArrayList;
import brandon.utils.Log;

public abstract class GameServer {
	// Listing of all the Game servers, and next server id
	// These need to be initialized right away, as they can be called (via accessor methods)
	// through the applet or HTML page before any instances are created.
	
    // Data contained in each session
    protected String gameStartTime = null;
    protected String gameName = null;
    private int id;
    protected int boardType = -1;
    
    /** Returns the ID for this session */
    public int getId() {
    	return id;
    }

    public void setId(int id) {
    	this.id = id;
    }
	
	/** Returns the name of the Game Server with its ID */
	public String getFullName() {
    	if (getGame() == null) return null;
    	return id + ": " + getName();
    }
    
    public String getStartTime() {
        return gameStartTime;
    }

	
	public abstract Game getGame();
	
	
    /** Return a String of player names joined in this game */
    public String getPlayers() {
      if (getGame() == null) { return "(No Game Object)"; }

      String playersString = new String("(");

      for(Player player : getGame().getPlayers()) {
        playersString = " " + playersString + player.getName();
      }

      if (getGame().getPlayers() == null) {
          return "(No Players)";
      }
      
      return playersString + ")";
    }

    /** Return the number of players in this game */
    public int getNumPlayers() {
      if (getGame() == null) { return 0; }
      ArrayList players = getGame().getPlayers();
      if (players == null) { return 0; }
      return players.size();
    }

    /** Return the player name who started this game */
    public String getPlayerStarted() {
    	String methodName = "getPlayerStarted";
      ArrayList players = getGame().getPlayers();
      String playerName = null;
      try {
        playerName = (String) players.get(0);
      } catch (Exception e) {
        Log.error(this, methodName, "Exception: " + e);
      }
      return playerName;
    }


    /** Returns the files in the specified directory as an ArrayList of Strings */
    public static ArrayList<String> getFilesInDirectory(String directoryName) {
      ArrayList<String> fileList = new ArrayList<String>();
      File file = new File(directoryName);
      if (file.isDirectory()) {
        for(String fileName : file.list()) {
          fileList.add(fileName);
        }
      }
      return fileList;
    }

    public void initialize(String gameName, int boardType, int id) {
        this.gameName = gameName;
        this.boardType = boardType;
        setId(id);
        
    	// Get time and date this game started
    	Calendar gameStartCalendar = Calendar.getInstance();
    	int month = gameStartCalendar.get(Calendar.MONTH);
    	int day = gameStartCalendar.get(Calendar.DATE);
    	int year = gameStartCalendar.get(Calendar.YEAR);
    	int hour = gameStartCalendar.get(Calendar.HOUR);
    	int minute = gameStartCalendar.get(Calendar.MINUTE);

    	// Set the starting time of the game
    	String minutePadding = "";
    	if (minute < 10) { minutePadding = "0"; }

    	gameStartTime = (month + 1) + "/" + day + "/" + year + " at " +
	    	hour + ":" + minutePadding + minute;
	
    }
    
    public String getName() {
    	return gameName;
    }

    public void setName(String gameName) {
    	this.gameName = gameName;
    }
    
    public GameServer() {
    }

}
