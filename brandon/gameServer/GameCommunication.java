package brandon.gameServer;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.io.*;
import brandon.utils.Log;
import brandon.gameData.Game;
import brandon.gameData.GameServer;

import java.util.Iterator;

/** Servlet which accepts commands from the Settlers Applet, and forwards
 *  these commands to the appropriate Command Objects handling these events
 */
public abstract class GameCommunication extends HttpServlet {
    public static String PREFIX = "";
    public static String VERSION_NUMBER = "";
    public static String VERSION_DATE = "";
    
    private int id = -1;
    
    public abstract List<? extends GameServer> getGameServers(); 
    
    public abstract GameServer getGameServer(int id);
    
    public abstract void setGame(ServerCommand commandObject, Game game);
    
    /** Main entry into a servlet. The applet requests are received through here. */
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
    	final String methodName = "doGet(HttpServletRequest, HttpServletResponse)";
    	
    	Log.setLevel(Log.LogLevel.INFO);
    	
        // 3 Standard Values passed with every request:
        // Command - the command sent from the client to the server
        // id - the server id where this command should go
        String command = request.getParameter("command");
        String serverId = request.getParameter("id");
        
        if ((command == null) && (serverId == null)) {
          // If no parameters are given, we are probably just running
          // this servlet by hand to see if it is up. Respond by displaying
          // a health message and the available servers.
          try {
              response.setContentType("text/html");
              ServletOutputStream outputStream = response.getOutputStream();
              outputStream.println("<TABLE BORDER='0' BGCOLOR='#000000' WIDTH='100%'>");
              outputStream.println("<TR><TD WIDTH='75%'><FONT COLOR='#DDDDDD' FACE='Arial'>");
              outputStream.println("&nbsp;&nbsp;Settlers of Catan Server</FONT></TD>");
              outputStream.println("<TD><FONT COLOR='#DDDDDD' FACE='Arial'>" + VERSION_NUMBER + "</FONT></TD>");
              outputStream.println("<TD><FONT COLOR='#DDDDDD' FACE='Arial'>" + VERSION_DATE);
              outputStream.println("</FONT></TR></TD></TABLE>");

              outputStream.println("<BR><BR>");
            
            List gameServers = getGameServers();
            int numGames = 0;

            if (gameServers != null) {
              numGames = gameServers.size();
            } else {
              outputStream.println("No games are currently running.");
              return;
            }

            outputStream.println("There are <B>" + numGames + "</B> games running.<BR><BR>");
            if (numGames > 0) {
              outputStream.println("<TABLE BORDER='0'><TR><TD>Num</TD><TD>Server Name</TD>");
              outputStream.println("<TD>Game Type</TD><TD>Players</TD><TD>Events</TD></TR>");

              for(Iterator i=gameServers.iterator(); i.hasNext();) {
                GameServer gameServer = (GameServer) i.next();
                String name = gameServer.getName();
                int numPlayers = gameServer.getNumPlayers();
                int id = gameServer.getId();
                String type = "";
                String events = "";
                outputStream.println("<TR><TD>" + id + "</TD>");
                outputStream.println("<TD>" + name + "</TD>");
                outputStream.println("<TD>" + type + "</TD>");
                outputStream.println("<TD>" + numPlayers + "</TD>");
                outputStream.println("<TD>" + events + "</TD>");
              }

            }
          } catch (IOException e) {
            Log.error(this, methodName, "servlet processing web page could had an IOException: " + e);
            e.printStackTrace();
          }

          return;
        }

        if (serverId == null) {
          id = -1;
        } else {
          try {
            id = Integer.parseInt(serverId);
          } catch (NumberFormatException e) {
            Log.error(this, methodName, "Could not Integer.parseInt(" + serverId + "): " + e);
          }
        }

        // Create a new object based on the command
        ServerCommand commandObject = null;
        Class commandClass = null;

        try {
          // Create a new class from the command name sent in
          commandClass = Class.forName(PREFIX + command);
        } catch (ClassNotFoundException e) {
          Log.error(this, methodName, "Class.forName(" + command + ") failed. " + e);
          e.printStackTrace();
        }

        try {
          // Create a new object from that class
          commandObject = (ServerCommand) commandClass.newInstance();
        } catch (Exception e) {
          // InstantiationException
          // IllegalAccessException
          Log.error(this, methodName, "commandClass.newInstance() failed for class " + command + ": " + e);
          e.printStackTrace();
        }

        // Set up the new ServerCommand Object
        ObjectOutputStream out = null;
        try {
          ServletOutputStream outputStream = response.getOutputStream();
          out = new ObjectOutputStream(outputStream);
        } catch (IOException e) {

        }
        commandObject.setObjectOutputStream(out);
        commandObject.setServletRequest(request);

        GameServer server = getGameServer(id);
        if (server == null) {
          // We're talking to the server without having our own instance.
          // We must be setting up a game right now.
          // Server has not been set up correctly
        } else {
          Game game = server.getGame();
          if (game == null) {
            Log.error(this, methodName,
            		"WARNING! Game Object not placed into the Command Object (it doesn't exist yet).");
          } else {
            setGame(commandObject, game);
          }
        }

        // Run the ServerCommand Object
        try {
        	commandObject.runCommand();
        } catch (Exception e) {
          Log.error(this, methodName, "Command [" + command + "] caused exception: " + e);
          e.printStackTrace();
        }
    }
}
