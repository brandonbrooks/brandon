package brandon.gameData;

import java.awt.Color;
import java.io.Serializable;
import brandon.gui.ColorMapping;

public abstract class Player implements Serializable {
	public enum Status { ACTIVE, INACTIVE };

    public Status playerStatus = Status.ACTIVE;

    // Attributes associated with a player
    public String name;
    private int score;

    public int turnOrder = -1;

    private String icon;
    private int number = -1;
    private ColorMapping colorMapping;

    /** Return the player's color */
    public Color getColor() {
    	return colorMapping.color;
    }

    /** Return the player's muted color */
    public Color getMutedColor() {
      return colorMapping.mutedColor;
    }

    public ColorMapping getColorMapping() {
    	return colorMapping;
    }

    /** Construct a new player with 'name', 'color' and specified icon. */
    public Player(String name, ColorMapping colorMapping, String icon) {
    	this.name = name;
    	this.colorMapping = colorMapping;
    	this.icon = icon;
    	
        score = 0;       
    }

    /** Return player's status */
    public Status getPlayerStatus() {
		return playerStatus;
	}

    /** Set this player's status */
    public void setPlayerStatus(Status playerStatus) {
		this.playerStatus = playerStatus;
	}

    /** Return the player's name */
    public String getName() {
		return name;
	}

    /** Return the player's icon number */
    public String getIcon() {
		return icon;
	}

    /** Return the player's id */
    public int getId() {
    	return number;
    }

    /** Set the player's id */
    public void setId(int number) {
    	this.number = number;
    }
    
    public int getScore() {
    	return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
}
