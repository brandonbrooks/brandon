package brandon.gameData.test;

import brandon.gameData.*;
import java.util.List;

public class TestHexGameBoard {
	public static void main(String[] args) {
		System.out.println("Creating a new HexGameBoard");
	/*	
		HexGameBoard hexGameBoard = new HexGameBoard(7,7);
		
		for(int j=0;(j < 7); j++) {
			for(int i=0;(i < 7); i++) {
				HexSide side = hexGameBoard.getSide(i,j,HexTile.SideDirection.NE);
				System.out.println("Side [" + i + "," + j + ":NE]");
				List<HexTile> list = side.getAdjacentHexes();
				if (list.size() == 0) { System.out.println("   has no adjacent Tiles."); }
				for(HexTile tile : list) {
					System.out.println("   has adjacent Tile: [" + tile.col + "," + tile.row + "]");
				}
			}
		}

		for(int j=0;(j < 7); j++) {
			for(int i=0;(i < 7); i++) {
				HexSide side = hexGameBoard.getSide(i,j,HexTile.SideDirection.NE);
				System.out.println("Side [" + i + "," + j + ":NE]");
				List<HexVertice> list = side.getAdjacentVertices();
				if (list.size() == 0) { System.out.println("   has no adjacent Vertices."); }
				for(HexVertice vertice : list) {
					System.out.println("   has adjacent Vertice.");
				}
			}
		}
	*/	
		
	}
}
