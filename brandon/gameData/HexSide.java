package brandon.gameData;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class HexSide implements Serializable {
	private static int nextId = 0;
	
	private int id = 0;
	
	/** Construct a new empty side */
	public HexSide() {
		id = nextId++;
	}
	
	/** create a side attached to a hex at the specified location */
	public HexSide(HexTile containedOnHex, HexTile.SideDirection direction) {
		this();
		addAdjacentTile(containedOnHex, direction);
	}
	
	public abstract ArrayList<? extends HexTile> getAdjacentHexes();
	
	public abstract ArrayList<? extends HexVertice> getAdjacentVertices();
	
	public abstract ArrayList<? extends HexSide> getAdjacentSides();
	
	public abstract HexTile getAdjacentHexTile(HexTile.SideDirection direction);
	
	public abstract void addAdjacentTile(HexTile tile, HexTile.SideDirection direction);
	
	public abstract void addAdjacentSide(HexSide side);
	
	public abstract void addAdjacentVertice(HexVertice vertice);
	
	public int getId() {
		return id;
	}
	
	public abstract void setDefaultHexTile(HexTile tile);
	public abstract void setDefaultSideDirection(HexTile.SideDirection side);
	
}
