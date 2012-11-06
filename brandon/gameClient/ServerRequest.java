package brandon.gameClient;

import java.io.*;
import java.util.*;
import java.net.*;
import brandon.gameData.SerializableGameServer;
import brandon.gameData.Game;
import brandon.gui.Media;
import brandon.gui.MediaResource;
import brandon.gui.ImageResource;
import brandon.gui.AudioResource;
import brandon.gui.MultiImageResource;
import brandon.utils.Log;
import brandon.utils.Problem;
import java.util.HashMap;
import java.net.NoRouteToHostException;

public abstract class ServerRequest {
	public static final int NO_SERVER_ID = -9999;
	
	protected String serverName;
	protected String servletName;
	protected int serverId = NO_SERVER_ID;
	   // NO_SERVER_ID means that the server has not given a server id to this class yet,
	   // thus no game has been initialized

	public static final String GET_GAME_OBJECT = "GetGameObject";
	public static final String GET_GAME_SERVERS = "GetGameServers";
	public static final String GET_IMAGES = "GetImages";
	public static final String NEW_SERVER = "NewServer";
    public static final String SERVER_VERSION = "ServerVersion";
    
    class ResponsePacket {
    	int id;
    	boolean completed = false;
    	int responseCode = -1;
    	Object data = null;
    	
    	public ResponsePacket(int id) {
    		this.id = id;
    	}
    	
    }
    
    class RequestPacket {
    	int id;
    	Thread notifier;
    	String requestType;
    	ArrayList data = new ArrayList();
    	
    	public RequestPacket(String requestType, Thread notifier) {
    		this.requestType = requestType;
    		id = nextRequestId++;
    		this.notifier = notifier;
    		ResponsePacket responsePacket = new ResponsePacket(id);
    		responses.put(new Integer(id), responsePacket);
    	}
    }
    
    private HashMap<Integer, ResponsePacket> responses = new HashMap<Integer, ResponsePacket>();
    private ArrayList<RequestPacket> requests = new ArrayList<RequestPacket>();
    private int nextRequestId = 0;
    
	/** Initializes the ServerRequest object
	 *  @param serverName   URL of the server
	 *  @param servletName  Name of the servlet on the server
	 *  */
	public ServerRequest(String serverName, String servletName) {
		this.serverName = serverName;
		this.servletName = servletName;
	}
	
	/** Request that the server get the current game object */
	public int requestGame(Thread notifier) {
		RequestPacket requestPacket = new RequestPacket(GET_GAME_OBJECT, notifier);
		requests.add(requestPacket);
		return requestPacket.id;
	}
	
	/** Retrieve the current game object from the server */
	public Game getGame() {
		final String methodName = "getGame";
		
		Game game = null;

		try {
			URL url;
			URLConnection conn;
			url = new URL(serverName + servletName + "?command=GetGameObject&id=" + serverId);
			conn = url.openConnection();
			conn.setDoInput(true);
			ObjectInputStream in = new ObjectInputStream(conn.getInputStream());
			game = (Game) in.readObject();
			in.close();
		} catch (Exception e) {
		}

		Log.info(this, methodName, "Received Game object!");

		if (game == null) {
			Problem.warning("The Game Object is null. It shouldn't be!");
		} else {
            Log.info(this, methodName, "Number of players in the game: " + game.getPlayers().size());
		}

		return game;
		
	}

	/** Returns the Server Name and port. The internal server name includes
	 *  http://, so we chop this off the beginning and return the result. */
	public String getServerName() {
		return serverName.substring(7);
	}
	
	/** Request that the server get the current game object */
	public void requestCommand(Thread notifier, String command, Map parameters) {
		RequestPacket requestPacket = new RequestPacket(command, notifier);
		requestPacket.data.add(parameters);
		requests.add(requestPacket);
	}
	

	/** Generic command. Use this form of 'sendCommand' if the command
	 *  you're looking to send doesn't have it's own method.
	 */
	public int sendCommand(String command, Map parameters) {
		// extract all the parameters into a string

		String stringParameters = new String();

		for (Iterator i = parameters.keySet().iterator(); i.hasNext();) {
			Object key = i.next();
			Object value = parameters.get(key);
			stringParameters += "&" + key.toString() + "=" + value.toString();
		}

		return sendURL(command, stringParameters, -1);
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	
	public int getServerId() {
		return serverId;
	}

	public boolean hasValidServerId() {
		return (serverId != NO_SERVER_ID);
	}
	
	public abstract Collection<? extends SerializableGameServer> getServers();


	/** Returns a TRUE if the request returned with a response from the server. */
	public boolean requestFinished(int requestId) {
		String methodName = "requestFinished(" + requestId + ")";
		
		ResponsePacket response = responses.get(requestId);
		if (response == null) {
			Log.error(this, methodName, "Packet " + requestId + " was NOT found");
			return false;
		}
		
        return response.completed;
        
	}
	
	/** Returns a TRUE if the request returned with a successful response from the server. */
	public boolean requestSuccessful(int requestId) {
		ResponsePacket response = responses.get(requestId);
		if (response == null)
			return false;
		
		if (response.responseCode != -1)
			return true;
		
        return false;
		
	}
	
	public Object getObjectResponse(int requestId) {
		String methodName = "getObjectResponse(" + requestId + ")";
		
		ResponsePacket response = responses.get(requestId);
		if ((response.completed == true) && (response.responseCode != -1)) {
			return response.data;
		}
		
        if (response.completed == false) {
            Log.error(this, methodName,
                    "Response from Request #" + requestId + " was not completed. Could not return a value.");
		} else {
		    Log.error(this, methodName,
		            "Response from Request #" + requestId + " did not complete successfully. " + 
		            "Error code: " + response.responseCode + " was returned.");
		}
		return null;
	}
	
	public String getStringResponse(int requestId) {
		return (String) getObjectResponse(requestId);
	}
	
	public Game getGameResponse(int requestId) {
		return (Game) getObjectResponse(requestId);
	}
	
	public Media getMediaResponse(int requestId) {
		return (Media) getObjectResponse(requestId);
	}
	
	public String getServerVersion() {
		final String methodName = "getServerVersion";
		String response = "";
		String urlString = serverName + servletName	+ "?command=" + SERVER_VERSION;
		
		try {
			Log.info(this, methodName, "sendURL: " + urlString);
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();

			ObjectInputStream in = new ObjectInputStream(conn.getInputStream());
			response = ((String) in.readObject());
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			Problem.serverError("Could not connect to server "
				+ serverName + " Error:[" + e + "]");
            System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			Problem.serverError("SendURL: URL Error [" + e + "]");
		}

		return response; 
	}
	
	/** Asks the server for a list of all the images to load.
	 *  Returns an ArrayList of MediaResources. */
	public Media getImages(Media media) throws NoRouteToHostException {
		final String methodName = "getImages(Media)";
		
		// Get all the different types of images and combine together into
		// one ArrayList, and return it

		// Regular Image Resources
		ArrayList<String> images = sendURL_ListResponse(GET_IMAGES);

		for(String url : images) {
			String name = url.substring(0, url.length() - 4);

			// If name starts with '/players/' then this image should be tagged
			// as a player's image
			MediaResource mr = null;

			if (url.length() > 8) {
				if (url.substring(0, 6).equals("Player")) {
					mr = new ImageResource(name, url, ImageResource.ImageType.PLAYER_IMAGE);
				} else if (url.substring(0, 5).equals("Audio")) {
					mr = new AudioResource(name, url);
				} else if (url.substring(0, 5).equals("Multi")) {
					// Chop off the 'Multi Part'
					name = name.substring(5, name.length());
					int minusLocation = name.indexOf('-');
					if (minusLocation == -1) {
						Log.error(this, methodName, "Found MultiImage [" + url + "] with no dash");
					}
					String numFramesString = null;
					int numFrames = 1;
					try {
						numFramesString = name.substring(minusLocation + 1,	name.length());
						numFrames = new Integer(numFramesString).intValue();
					} catch (Exception e) {
						Log.error(this, methodName, "Error dealing with MultiImage ["	+ url
							+ "]. Can't find numFrames");
					}
					// Chop off the end '-xx' for number of frames
					name = name.substring(0, minusLocation);
					mr = new MultiImageResource(name, url, numFrames);
				} else {
					mr = new ImageResource(name, url);
				}
			} else {
				mr = new ImageResource(name, url);
			}
			media.add(mr);
		}

		return media;
	}

	/** Calls the Server to request a new game.
	 *  Returns the serverId of this new game */
	public int getNewServerId(String gameName, int gameType) {
		String methodName = "getNewServerId(" + gameName + ", " + gameType +")";
		int serverId = -1;

		try {
			gameName = URLEncoder.encode(gameName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Problem.error("getNewServerId: I can't encode [" + gameName + "]");
		}

		//try {
		//	gameType = URLEncoder.encode(gameType, "UTF-8");
		//} catch (UnsupportedEncodingException e) {
	//		e.printStackTrace();
	//		Problem.error("getNewServerId: I can't encode [" + gameType + "]");
	//	}

		String urlString = serverName + servletName + "?command=" + NEW_SERVER
			+ "&name=" + gameName + "&type=" + gameType;

		try {
			Log.info(this, methodName, "getNewServerID: " + urlString);
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();

			ObjectInputStream in = new ObjectInputStream(conn.getInputStream());
			serverId = ((Integer) in.readObject()).intValue();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			Problem.serverError("Could not connect to server "
				+ serverName + " Error:[" + e + "]");
            System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			Problem.serverError("getNewServerId: URL Error [" + e + "]");
		}

		return serverId;
	}
	
	/** Send a command to the server. Response is returned as an int */
	protected int sendURL(String command, String parameters, int playerId) {
		String methodName = "sendURL(" + command + ", " + parameters + ", " + playerId + ")";
		int response = -1;
		String urlString = serverName + servletName	+
		                   "?command=" + command + "&id=" + serverId;
		
		if (playerId != -1) {
			urlString += "&playerNum=" + playerId;
		}
		
		if ((parameters != null) && (!parameters.equals(""))) {
			urlString += "&" + parameters;
		}

		try {
			Log.info(this, methodName, "sendURL: " + urlString);
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();

			ObjectInputStream in = new ObjectInputStream(conn.getInputStream());
			response = ((Integer) in.readObject()).intValue();
			in.close();
		} catch (EOFException e) {
			// This is okay - we just didn't get an object
			Log.warning(this, methodName, "EOFException - did not retrieve response object");
			response = 1;
		} catch (IOException e) {
			e.printStackTrace();
			Problem.serverError("Could not connect to server "
				+ serverName + " Error:[" + e + "]");
            System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			Problem.serverError("SendURL: URL Error [" + e + "]");
		}

		return response;
	}

	/** Send a command to the server. Response is returned as an ArrayList */
	protected ArrayList sendURL_ListResponse(String command) {
		String methodName = "sendURL_ListResponse(" + command + ")";
		ArrayList response = null;

		String urlString = serverName + servletName + "?command=" + command;

		try {
			Log.info(this, methodName, "sendURL_ListResponse: " + urlString);
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();

			ObjectInputStream in = new ObjectInputStream(conn.getInputStream());
			response = (ArrayList) in.readObject();
			in.close();
		} catch (EOFException e) {
			// This is okay - we just didn't get an object
			Log.warning(this, methodName, "EOFException - did not retrieve response object");
			response = null;
		} catch (IOException e) {
			e.printStackTrace();
			Problem.serverError("Could not connect to server "
				+ serverName + " Error:[" + e + "]");
            System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			Problem
				.serverError("sendURL_ListResponse: URL Error [" + e + "]");
		}

		return response;

	}
	
	/** Send a command to the server. Response is returned as a Vector */
	protected String sendURL_StringResponse(String command) {
		String methodName = "sendURL_StringResponse(" + command + ")";
		String response = new String();

		String urlString = serverName + servletName + "?command=" + command;

		try {
			Log.info(this, methodName, "sendURL_ListResponse: " + urlString);
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();

			BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
			int b;
			while(( b = in.read()) != -1) {
				response = response.concat("" + b);
			}
			in.close();

		} catch (EOFException e) {
			// This is okay - we just didn't get an object
			Log.warning(this, methodName, "EOFException - did not retrieve response object");
			response = null;
		} catch (IOException e) {
			e.printStackTrace();
			Problem.serverError("Could not connect to server "
				+ serverName + " Error:[" + e + "]");
            System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			Problem.serverError("sendURL_StringResponse: URL Error [" + e + "]");
		}

		return response;

	}
	
	/** Thread which actually sends the commands to the server.
	 *  We need to have a thread because we want the user interface to be
	 *  responsive while we're talking to the server. */
	/*class ServerRequestThread extends Thread {
		public void run() {
			setName("ServerRequestThread");
			
			while(1==1) {
				try {
					if (requests.size() == 0) {
						// Wait until we're notified that there is a new element in the vector
						wait();
					}
				} catch (InterruptedException e) {
				}
				
				// Grab the first object off the request vector
				RequestPacket packet = requests.get(0);
				String requestType = packet.requestType;
				if (requestType == null) {
					Log.error("ServerRequestThread: NULL request. Cannot process it.");
				} else if (requestType.equals(GET_GAME_OBJECT)) {
					getGame();
				} else if (requestType.equals(GET_GAME_SERVERS)) {
					getServers();
				} else if (requestType.equals(GET_IMAGES)) {
					// getImages((Media) packet.data.get(0));
				} else if (requestType.equals(NEW_SERVER)) {
					getNewServerId((String) packet.data.get(0), (String) packet.data.get(1));
				} else if (requestType.equals(SERVER_VERSION)) {
					getServerVersion();
				} else {
					Log.error("ServerRequestThread: invalid request: [" + requestType + "]");
				}
				
			}
		}
	}
	*/
	
}
