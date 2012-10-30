package brandon.gameData;

import java.io.Serializable;
import java.util.ArrayList;

/** Represents a hex tile on the board */
public abstract class HexTile implements Serializable {    
    public static enum SideDirection { NE, E, SE, SW, W, NW };
    public static enum VerticeDirection { N, NE, SE, S, SW, NW };

	private static int nextId = 0;
	
	// HexTile Attributes
	
	/** y-coordinate (row) of this tile */
	public int x;
	
	/** x-coordinate (column) of this tile */
	public int y;
	
	private int id = 0;

	/** Creates a new blank hex tile */
	public HexTile() {
		id = nextId++;
		
		// Leave everything blank - as there are no sides or vertices yet
		// until we add them to a HexBoard.
	}

	public abstract ArrayList<? extends HexTile> getAdjacentTiles();
	
	public abstract ArrayList<? extends HexVertice> getVertices();
	
	public abstract ArrayList<? extends HexSide> getSides();

	public abstract HexSide getSide(SideDirection sideDirection);
	
	public abstract HexTile getAdjacentHex(SideDirection sideDirection);
	
	public abstract HexVertice getVertice(VerticeDirection verticeDirection);
	
	public abstract void setAdjacentHex(SideDirection sideDirection, HexTile hexTile);
	
	public abstract void setSide(SideDirection sideDirection, HexSide side);
	
	public abstract void setVertice(VerticeDirection verticeDirection, HexVertice vertice);
	
	public int getId() {
		return id;
	}

	public static String convertToString(SideDirection side) {
		switch(side) {
			case NE: return "NE";
			case E:  return "E";
			case SE: return "SE";
			case SW: return "SW";
			case W:  return "W";
			case NW: return "NW";
			default: return null;
		}
	}
	
	public static SideDirection getSideFromString(String side) {
		if (side != null) {
			if (side.equals("NE")) {
				return SideDirection.NE;
			} else if (side.equals("E")) {
				return SideDirection.E;
			} else if (side.equals("SE")) {
				return SideDirection.SE;
			} else if (side.equals("SW")) {
				return SideDirection.SW;
			} else if (side.equals("W")) {
				return SideDirection.W;
			} else if (side.equals("NW")) {
				return SideDirection.NW;
			}
		}
		return null;
	}

	
	public static String convertToString(VerticeDirection vertice) {
		switch(vertice) {
			case N: return "N";
			case NE:  return "NE";
			case SE: return "SE";
			case S: return "S";
			case SW:  return "SW";
			case NW: return "NW";
			default: return null;
		}
	}
	
	public static VerticeDirection getVerticeFromString(String vertice) {
		if (vertice != null) {
			if (vertice.equals("N")) {
				return VerticeDirection.N;
			} else if (vertice.equals("NE")) {
				return VerticeDirection.NE;
			} else if (vertice.equals("SE")) {
				return VerticeDirection.SE;
			} else if (vertice.equals("S")) {
				return VerticeDirection.S;
			} else if (vertice.equals("SW")) {
				return VerticeDirection.SW;
			} else if (vertice.equals("NW")) {
				return VerticeDirection.NW;
			}
		}
		return null;
	}
	
}
