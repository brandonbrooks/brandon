package brandon.gameData;

import java.io.Serializable;
import java.util.ArrayList;

/** A hex tile vertice */
public abstract class HexVertice implements Serializable {
	private static int nextId = 0;
	private int id = 0;
	
	/** Create a new empty vertice */
	public HexVertice() {
		id = nextId++;
	}

	public abstract void addAdjacentHex(HexTile ownerHex, HexTile.VerticeDirection direction);
	
	public abstract HexTile getAdjacentHex(HexTile.VerticeDirection direction);
	
	public abstract ArrayList<? extends HexTile> getAdjacentHexes();
	
	public abstract ArrayList<? extends HexVertice> getAdjacentVertices();
	
	public abstract void addAdjacentVertice(HexVertice vertice);
	
	public abstract ArrayList<? extends HexSide> getAdjacentSides();
	public abstract void addAdjacentSide(HexSide side);
	
	public int getId() {
		return id;
	}

	public abstract void setDefaultHexTile(HexTile tile);
	public abstract void setDefaultVerticeDirection(HexTile.VerticeDirection direction);
	public abstract HexTile getDefaultHexTile();
	public abstract HexTile.VerticeDirection getDefaultVerticeDirection();
}
