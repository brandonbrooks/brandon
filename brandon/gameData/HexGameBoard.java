package brandon.gameData;

import java.util.ArrayList;

public abstract class HexGameBoard extends GameBoard {
	
	public HexGameBoard() {
	}

	/** Create a new Game board of hexes with a maximum size */
	public HexGameBoard(int maxCols, int maxRows) {
		super(maxCols, maxRows);
		
	   	// create HexTiles
		createHexTileArray(maxCols, maxRows);
    	
    	// Go through each hex tile, and add it to it's
    	// neighboring (adjacent) hex tiles
   		for(int j=0; (j < maxRows); j++) {
   			for(int i=0; (i < maxCols); i++) {
   				// Log.info("Creating tile " + i + "," + j);
    			HexTile tile = getHexTileAt(i, j);
    			createAdjacentTiles(tile);
    			createAdjacentSides(tile);
    			createAdjacentVertices(tile);
    		}
    	}
    	
    	// Go through each side, and let it know about the sides that are next to it
    	for(int j=0; (j < maxRows); j++) {
    		for(int i=0; (i < maxCols); i++) {
    			HexTile tile = getHexTileAt(i, j);
    			for(HexTile.SideDirection direction : HexTile.SideDirection.values()) {
    				if (tile.getSide(direction).getAdjacentSides().size() == 0) {
    					makeAdjacentSides(tile, direction);
    				}
    				if (tile.getSide(direction).getAdjacentVertices().size() == 0) {
    					makeAdjacentVertices(tile, direction);
    				}
    				if (tile.getSide(direction).getAdjacentHexes().size() == 0) {
    					makeAdjacentHexes(tile, direction);
    				}
    			}
    		}
    	}
    	
    	// Go through each vertice, and let it know about the vertices that are next to it
    	for(int j=0; (j < maxRows); j++) {
    		for(int i=0; (i < maxCols); i++) {
    			HexTile tile = getHexTileAt(i, j);
    			for(HexTile.VerticeDirection direction : HexTile.VerticeDirection.values()) {
    				if (tile.getVertice(direction).getAdjacentVertices().size() == 0) {
    					makeAdjacentVertices(tile, direction);
    				}
    				if (tile.getVertice(direction).getAdjacentSides().size() == 0) {
    					makeAdjacentSides(tile, direction);
    				}
    				if (tile.getVertice(direction).getAdjacentHexes().size() == 0) {
    					makeAdjacentHexes(tile, direction);
    				}
    			}
    		}
    	}
    	
	}

	/** Helper function to return the opposite of a given vertice direction on a direction from this vertice */
	private HexTile.VerticeDirection oppositeVertice(HexTile.SideDirection side, HexTile.VerticeDirection vertice) {
		switch(side) {
			case W: switch(vertice) {
				case NW: return HexTile.VerticeDirection.NE;
				case SW: default: return HexTile.VerticeDirection.SE;
			}
			case NW: switch (vertice) {
				case N: return HexTile.VerticeDirection.SE;
				case NW: default: return HexTile.VerticeDirection.S;
			}
			case NE: switch(vertice) {
				case N: return HexTile.VerticeDirection.SW;
				case NE: default: return HexTile.VerticeDirection.S;
			}
			case E: switch (vertice) {
				case NE: return HexTile.VerticeDirection.NW;
				case SE: default: return HexTile.VerticeDirection.SW;
			}
			case SE: switch(vertice) {
				case SE: return HexTile.VerticeDirection.N;
				case S: default: return HexTile.VerticeDirection.NW;
			}
			case SW: default: switch (vertice) {
				case N: return HexTile.VerticeDirection.SW;
				case NE: default: return HexTile.VerticeDirection.S;
			}
		}
	}
	
	/** Helper function to return the opposite of a given side direction */
	private HexTile.SideDirection oppositeSide(HexTile.SideDirection side) {
		switch(side) {
			case NE: return HexTile.SideDirection.SW;
			case E:  return HexTile.SideDirection.W;
			case SE: return HexTile.SideDirection.NW;
			case SW: return HexTile.SideDirection.NE;
			case W:  return HexTile.SideDirection.E;
			case NW: default:
				     return HexTile.SideDirection.SE;
		}
	}
	
	private String sideToString(HexTile.SideDirection side) {
		switch(side) {
			case NE: return "NE";
			case E:  return "E";
			case SE: return "SE";
			case SW: return "SW";
			case W:  return "W";
			case NW: default: return "NW";
		}
	}
	
	private String verticeToString(HexTile.VerticeDirection vertice) {
		switch(vertice) {
			case N: return "N";
			case NE: return "NE";
			case SE: return "SE";
			case S: return "S";
			case SW: return "SW";
			case NW: default: return "NW";
		}		
	}
	
	/** Override this method to create a different subclass of HexTile */
	protected abstract HexTile createHexTile(int x, int y);
    
	/** Override this method to create a different subclass of HexSide */
    protected abstract HexSide createSide();
    
    /** Override this method to create a different subclass of HexVertice */
    protected abstract HexVertice createVertice();
	
	public abstract HexTile getHexTileAt(int x, int y);
	
	public abstract void setHexTile(int x, int y, HexTile tile);
	
	public abstract void createHexTileArray(int x, int y);
	
    /** Returns a side of a hex tile at the specified location */
    public HexSide getSide(int row, int col, HexTile.SideDirection location) {
        HexTile hexTile = getHexTileAt(row, col);
        return hexTile.getSide(location);
    }

    /** Returns a vertice of a hex tile at the specified location */
    public HexVertice getVertice(int row, int col, HexTile.VerticeDirection location) {
        HexTile hexTile = getHexTileAt(row, col);
        return hexTile.getVertice(location);
    }
    
    /** Returns the hexTile at the relative location to the tile supplied.
     *  Null is returned if there is no tile at that location. */
    private HexTile getHexTileByLocation(HexTile tile, HexTile.SideDirection direction) {
		boolean evenRow;
		int x = tile.x;
		int y = tile.y;
		// Find out where the HexTile next to this tile should be
		evenRow = ((tile.y % 2) == 0);

		switch(direction) {
			case NE:
				x = evenRow ? tile.x : (tile.x + 1);
				y = tile.y - 1;
				break;
			case E:
				x = tile.x + 1;
				y = tile.y;
				break;
			case SE:
				x = evenRow ? tile.x : (tile.x + 1);
				y = tile.y + 1;
				break;
			case SW:
				x = evenRow ? (tile.x - 1) : tile.x;
				y = tile.y + 1;
				break;
			case W:
				x = tile.x - 1;
				y = tile.y;
				break;
			case NW:
				x = evenRow ? (tile.x - 1) : tile.x; 
				y = tile.y - 1;
				break;
		}
		
		if ((x < 0) || ( y < 0)) {
			return null;
		}

		
		if ((x >= getColumns()) || (y >= getRows())) {
			return null;
		}
		
		return getHexTileAt(x, y);
    }
    
    public ArrayList<HexTile> getHexTileByLocation(HexTile tile, HexTile.VerticeDirection direction) {
		ArrayList<HexTile> v = new ArrayList<HexTile>();
		HexTile tile1 = null;
		HexTile tile2 = null;
		
		switch(direction) {
			case N:		
				tile1 = getHexTileByLocation(tile, HexTile.SideDirection.NW);
				tile2 = getHexTileByLocation(tile, HexTile.SideDirection.NE);
				break;
			case NE:
				tile1 = getHexTileByLocation(tile, HexTile.SideDirection.NE);
				tile2 = getHexTileByLocation(tile, HexTile.SideDirection.E);
				break;
			case SE:
				tile1 = getHexTileByLocation(tile, HexTile.SideDirection.E);
				tile2 = getHexTileByLocation(tile, HexTile.SideDirection.SE);
				break;
			case S:
				tile1 = getHexTileByLocation(tile, HexTile.SideDirection.SE);
				tile2 = getHexTileByLocation(tile, HexTile.SideDirection.SW);
				break;
			case SW:
				tile1 = getHexTileByLocation(tile, HexTile.SideDirection.SW);
				tile2 = getHexTileByLocation(tile, HexTile.SideDirection.W);				
				break;
			case NW:
				tile1 = getHexTileByLocation(tile, HexTile.SideDirection.W);
				tile2 = getHexTileByLocation(tile, HexTile.SideDirection.NE);
				break;
		}

		if (tile1 != null) {
			v.add(tile1);
		}
		if (tile2 != null) {
			v.add(tile2);
		}
		return v;
    	
    }
    
    
    /** For every side direction, sets the adjacent hexes of this tile. */
    public void createAdjacentTiles(HexTile hexTile) {
    	// Set up the NE hexTile
    	for(HexTile.SideDirection direction : HexTile.SideDirection.values()) {
    		HexTile tile = getHexTileByLocation(hexTile, direction); 
    		if (tile != null)
    			hexTile.setAdjacentHex(direction, tile);
    	}
    
    }

    
    /** create all the sides for this hex tile, making sure not to duplicate
     *  any sides. If possible, use the sides that were previously created on adjacent hexTiles. */
    public void createAdjacentSides(HexTile hexTile) {
    	for(HexTile.SideDirection direction : HexTile.SideDirection.values()) {
    		HexSide side = null;
    		
    		// Does the side we want to use already exist? If it does,
    		// it's the OPPOSITE SIDE for the hexTile in that side's direction
    		HexTile tile = getHexTileByLocation(hexTile, direction);
    		if (tile != null) {
    			side = tile.getSide(oppositeSide(direction));
    		}
    		
    		// If the side does not already exist, let's create it
    		if (side == null) {
    			side = createSide();
    			side.setDefaultHexTile(hexTile);
    			side.setDefaultSideDirection(direction);
    		}

    		// set the side to this side we found, or newly created side
    		hexTile.setSide(direction, side);    		
			side.addAdjacentTile(hexTile, direction);
    	}
    }
    
    public void createAdjacentVertices(HexTile hexTile) {
    	for(HexTile.VerticeDirection direction : HexTile.VerticeDirection.values()) {
    		HexVertice vertice = null;
    		
    		// Does the vertice we want to use already exist? If it does,
    		// it's the OPPOSITE VERTICE for the hexTile in that side's direction
    		ArrayList<HexTile> tiles = getHexTileByLocation(hexTile, direction);
    		if (tiles.size() > 0) {
    			int i=0;
    			for(HexTile tile : tiles) {
    				i++;
    				
    				for(HexTile.SideDirection side : HexTile.SideDirection.values()) {
    					if ((hexTile.getAdjacentHex(side) != null) &&
    					    (tile.getId() == hexTile.getAdjacentHex(side).getId())) {
    						vertice = tile.getVertice(oppositeVertice(side, direction));
    						break;
    					}
    				}
    				if (vertice != null) {	
    					break;
    				}

    			}
    		}
            
    		if (vertice == null) {
    			vertice = createVertice();
    			vertice.setDefaultHexTile(hexTile);
    			vertice.setDefaultVerticeDirection(direction);
    		}
    		
    		// set the vertice to this vertice we found, or newly created vertice
    		hexTile.setVertice(direction, vertice);
    	
    	}
    }
    
    /** Given a side direction, returns adjacent side directions. The first two are
     *  on the current tile. The last two are on the side's other tile. */
    private HexTile.SideDirection[] adjacentSideDirections(HexTile.SideDirection direction) {
    	HexTile.SideDirection[] directions = new HexTile.SideDirection[4];
    	switch(direction) {
    		case NE: directions[0] = HexTile.SideDirection.NW;
    				 directions[1] = HexTile.SideDirection.E;
    				 directions[2] = HexTile.SideDirection.W;
    				 directions[3] = HexTile.SideDirection.SE;
    				 break;
    		case E:  directions[0] = HexTile.SideDirection.NE;
			 		 directions[1] = HexTile.SideDirection.SE;
			 		 directions[2] = HexTile.SideDirection.NW;
			 		 directions[3] = HexTile.SideDirection.SW;
			 		 break;
    		case SE: directions[0] = HexTile.SideDirection.SW;
	 		 		 directions[1] = HexTile.SideDirection.E;
	 		 		 directions[2] = HexTile.SideDirection.W;
	 		 		 directions[3] = HexTile.SideDirection.NE;
	 		 		 break;			 		 
    		case SW: directions[0] = HexTile.SideDirection.SE;
    				 directions[1] = HexTile.SideDirection.W;
    				 directions[2] = HexTile.SideDirection.NW;
    				 directions[3] = HexTile.SideDirection.E;
    				 break;	
    		case W:  directions[0] = HexTile.SideDirection.NW;
	 		 		 directions[1] = HexTile.SideDirection.SW;
	 		 		 directions[2] = HexTile.SideDirection.NE;
	 		 		 directions[3] = HexTile.SideDirection.SE;
	 		 		 break;	
    		case NW: directions[0] = HexTile.SideDirection.W;
	 		 		 directions[1] = HexTile.SideDirection.NE;
	 		 		 directions[2] = HexTile.SideDirection.NW;
	 		 		 directions[3] = HexTile.SideDirection.E;
	 		 		 break;	 
    	}
    	
    	return directions;
    }
    
    /** Returns the list of adjacent sides to a particular side */
    private void makeAdjacentSides(HexTile tile, HexTile.SideDirection direction) {
    	HexSide originalSide = tile.getSide(direction);
    	ArrayList<? extends HexTile> tiles = originalSide.getAdjacentHexes();

    	HexTile otherTile = null;
    	for(HexTile loopingTile : tiles) {
    		if (loopingTile != tile) {
    			otherTile = loopingTile;
    			break;
    		}
    	}
    	
    	HexTile.SideDirection[] sideDirections = adjacentSideDirections(direction);
    	originalSide.addAdjacentSide(tile.getSide(sideDirections[0]));
    	originalSide.addAdjacentSide(tile.getSide(sideDirections[1]));
    	
    	if (otherTile != null) {
    		originalSide.addAdjacentSide(otherTile.getSide(sideDirections[2]));
    		originalSide.addAdjacentSide(otherTile.getSide(sideDirections[3]));

    	} else {
    		// If the 'otherTile' is null, it's still possible that we need to add another side
    		// in all the following cases for tiles on the edge (TOP, BOTTOM, LEFT, RIGHT)
    		
    		// Tile at the TOP and NE direction - use tile to it's right's NW
    		// Tile at the TOP and NW direction - use tile to it's left's NE
    		if (tile.x == 0) {
    			otherTile = null;
    			if  (direction == HexTile.SideDirection.NE) {
        			otherTile = getHexTileAt(tile.y + 1, 0);
        			if (otherTile != null) {
        				originalSide.addAdjacentSide(otherTile.getSide(HexTile.SideDirection.NW));
        			}
    			}
    			if  (direction == HexTile.SideDirection.NW) {
        			otherTile = getHexTileAt(tile.y - 1, 0);
        			if (otherTile != null) {
        				originalSide.addAdjacentSide(otherTile.getSide(HexTile.SideDirection.NE));
        			}
        		}
    		}
    		
    		// Tile on the LEFT (and even) and NW or SW direction - use the tile above/below's W.
    		if ((tile.y == 0) && (tile.x % 2 == 0)) {
    			otherTile = null;
    			if (direction == HexTile.SideDirection.NW) {
    				otherTile = getHexTileAt(0, tile.x - 1);
    			}
    			if (direction == HexTile.SideDirection.SW) {
    				otherTile = getHexTileAt(0, tile.x + 1);
    			}
    			if (otherTile != null) {
    				originalSide.addAdjacentSide(otherTile.getSide(HexTile.SideDirection.W));
    			}
    		}
    		
    		// Tile at the BOTTOM and SE direction - use tile to it's right's SW
    		// Tile at the BOTTOM and SW direction - use tile to it's left's SE
    		if (tile.x == (this.rows -1)) {
    			otherTile = null;
    			if  (direction == HexTile.SideDirection.SE) {
        			otherTile = getHexTileAt(tile.y + 1, 0);
        			if (otherTile != null) {
        				originalSide.addAdjacentSide(otherTile.getSide(HexTile.SideDirection.SW));
        			}
    			}
    			if  (direction == HexTile.SideDirection.SW) {
        			otherTile = getHexTileAt(tile.y - 1, 0);
        			if (otherTile != null) {
        				originalSide.addAdjacentSide(otherTile.getSide(HexTile.SideDirection.SE));
        			}
        		}
    		}
    		
    		
    		// Tile on the RIGHT (and odd) and NE or SE direction - use the tile above/below's E.
    		if ((tile.y == (this.cols - 1)) && (tile.x % 2 == 1)) {
    			otherTile = null;
    			if (direction == HexTile.SideDirection.NE) {
    				otherTile = getHexTileAt(0, tile.x - 1);
    			}
    			if (direction == HexTile.SideDirection.SE) {
    				otherTile = getHexTileAt(0, tile.x + 1);
    			}
    			if (otherTile != null) {
    				originalSide.addAdjacentSide(otherTile.getSide(HexTile.SideDirection.E));
    			}
    		}
    		   		
    		
    	}
    	
    }
    
    /** Given a side, make references inside that side to all the vertices adjacent to it */
    public void makeAdjacentVertices(HexTile tile, HexTile.SideDirection direction) {
    	HexSide side = tile.getSide(direction);
    	   	switch(direction) {
    	   		case NE: side.addAdjacentVertice(tile.getVertice(HexTile.VerticeDirection.N));
    	   			     side.addAdjacentVertice(tile.getVertice(HexTile.VerticeDirection.NE));
    	   			     break;
    	   		case E:  side.addAdjacentVertice(tile.getVertice(HexTile.VerticeDirection.NE));
    	   				 side.addAdjacentVertice(tile.getVertice(HexTile.VerticeDirection.SE));
    	   				 break;
    	   		case SE: side.addAdjacentVertice(tile.getVertice(HexTile.VerticeDirection.SE));
  				 		 side.addAdjacentVertice(tile.getVertice(HexTile.VerticeDirection.S));
  				 		 break;
    	   		case SW: side.addAdjacentVertice(tile.getVertice(HexTile.VerticeDirection.S));
  				 		 side.addAdjacentVertice(tile.getVertice(HexTile.VerticeDirection.SW));
  				 		 break;
    	   		case W:  side.addAdjacentVertice(tile.getVertice(HexTile.VerticeDirection.SW));
  				 		 side.addAdjacentVertice(tile.getVertice(HexTile.VerticeDirection.NW));
  				 		 break;
    	   		case NW: side.addAdjacentVertice(tile.getVertice(HexTile.VerticeDirection.NW));
  				 		 side.addAdjacentVertice(tile.getVertice(HexTile.VerticeDirection.N));
  				 		 break;
    	   	}
    }
    
    /** Given a vertice, find all the adjacent vertices to this particular vertice. Every vertice
     *  has either 2 or 3 adjacent vertices. Most of the time it's 3 - except for the cases where
     *  the vertice is on the outside border of the board. */
    public void makeAdjacentVertices(HexTile tile, HexTile.VerticeDirection direction) {
    	HexVertice vertice = tile.getVertice(direction);
    	HexTile eTile = tile.getAdjacentHex(HexTile.SideDirection.E);
    	HexTile wTile = tile.getAdjacentHex(HexTile.SideDirection.W);
    	HexTile neTile = tile.getAdjacentHex(HexTile.SideDirection.NE);
    	HexTile nwTile = tile.getAdjacentHex(HexTile.SideDirection.NW);
    	HexTile seTile = tile.getAdjacentHex(HexTile.SideDirection.SE);
    	HexTile swTile = tile.getAdjacentHex(HexTile.SideDirection.SW);
    	
    	switch(direction) {
    		case NE: 
    				 vertice.addAdjacentVertice(tile.getVertice(HexTile.VerticeDirection.N));
    				 vertice.addAdjacentVertice(tile.getVertice(HexTile.VerticeDirection.SE));
    				 if (eTile != null) {
    					 vertice.addAdjacentVertice(eTile.getVertice(HexTile.VerticeDirection.N));
    				 } else if (neTile != null) {
    					 vertice.addAdjacentVertice(neTile.getVertice(HexTile.VerticeDirection.SE));
    				 }
    				 break;
    		case SE:
    				 vertice.addAdjacentVertice(tile.getVertice(HexTile.VerticeDirection.NE));
    				 vertice.addAdjacentVertice(tile.getVertice(HexTile.VerticeDirection.S));
    				 if (eTile != null) {
    					 vertice.addAdjacentVertice(eTile.getVertice(HexTile.VerticeDirection.S));
    				 } else if (seTile != null) {
    					 vertice.addAdjacentVertice(seTile.getVertice(HexTile.VerticeDirection.NE));
    				 }
    				 break;
    		case S:
    				 vertice.addAdjacentVertice(tile.getVertice(HexTile.VerticeDirection.SE));
    				 vertice.addAdjacentVertice(tile.getVertice(HexTile.VerticeDirection.SW));
    				 if (seTile != null) {
    					 vertice.addAdjacentVertice(seTile.getVertice(HexTile.VerticeDirection.SW));
    				 } else if (swTile != null) {
    					 vertice.addAdjacentVertice(swTile.getVertice(HexTile.VerticeDirection.SE));
    				 }
    				 break;
    		case SW:
    				 vertice.addAdjacentVertice(tile.getVertice(HexTile.VerticeDirection.NW));
    				 vertice.addAdjacentVertice(tile.getVertice(HexTile.VerticeDirection.S));
    				 if (swTile != null) {
    					 vertice.addAdjacentVertice(swTile.getVertice(HexTile.VerticeDirection.NW));
    				 } else if (wTile != null) {
    					 vertice.addAdjacentVertice(wTile.getVertice(HexTile.VerticeDirection.S));
    				 }
    				 break;
     		case NW:
    				 vertice.addAdjacentVertice(tile.getVertice(HexTile.VerticeDirection.N));
    				 vertice.addAdjacentVertice(tile.getVertice(HexTile.VerticeDirection.SW));
    				 if (wTile != null) {
    					 vertice.addAdjacentVertice(wTile.getVertice(HexTile.VerticeDirection.N));
    				 } else if (nwTile != null) {
    					 vertice.addAdjacentVertice(nwTile.getVertice(HexTile.VerticeDirection.SW));
    				 }
    				 break;   				 
     		case N:
    				 vertice.addAdjacentVertice(tile.getVertice(HexTile.VerticeDirection.NW));
    				 vertice.addAdjacentVertice(tile.getVertice(HexTile.VerticeDirection.NE));
    				 if (nwTile != null) {
    					 vertice.addAdjacentVertice(nwTile.getVertice(HexTile.VerticeDirection.NE));
    				 } else if (neTile != null) {
    					 vertice.addAdjacentVertice(neTile.getVertice(HexTile.VerticeDirection.NW));
    				 }
    				 break;   				 
    				 
    	}
    }
    
    /** Given a Vertice, make all the adjacent sides to this vertice. */
    public void makeAdjacentSides(HexTile tile, HexTile.VerticeDirection direction) {
    	HexVertice vertice = tile.getVertice(direction);
    	HexTile eTile = tile.getAdjacentHex(HexTile.SideDirection.E);
    	HexTile wTile = tile.getAdjacentHex(HexTile.SideDirection.W);
    	HexTile neTile = tile.getAdjacentHex(HexTile.SideDirection.NE);
    	HexTile nwTile = tile.getAdjacentHex(HexTile.SideDirection.NW);
    	HexTile seTile = tile.getAdjacentHex(HexTile.SideDirection.SE);
    	HexTile swTile = tile.getAdjacentHex(HexTile.SideDirection.SW);
    	
    	switch(direction) {
    		case NE:
    			vertice.addAdjacentSide(tile.getSide(HexTile.SideDirection.NE));
    			vertice.addAdjacentSide(tile.getSide(HexTile.SideDirection.E));
    			if (eTile != null) {
    				vertice.addAdjacentSide(eTile.getSide(HexTile.SideDirection.NW));
    			} else if (neTile != null) {
				 	vertice.addAdjacentSide(neTile.getSide(HexTile.SideDirection.SE));
    			}
    			break;
    		case SE:
    			vertice.addAdjacentSide(tile.getSide(HexTile.SideDirection.E));
    			vertice.addAdjacentSide(tile.getSide(HexTile.SideDirection.SE));
    			if (eTile != null) {
    				vertice.addAdjacentSide(eTile.getSide(HexTile.SideDirection.SW));
    			} else if (seTile != null) {
				 	vertice.addAdjacentSide(seTile.getSide(HexTile.SideDirection.NE));
    			}
    			break;
    		case S:
    			vertice.addAdjacentSide(tile.getSide(HexTile.SideDirection.SE));
    			vertice.addAdjacentSide(tile.getSide(HexTile.SideDirection.SW));
    			if (swTile != null) {
    				vertice.addAdjacentSide(swTile.getSide(HexTile.SideDirection.E));
    			} else if (seTile != null) {
				 	vertice.addAdjacentSide(seTile.getSide(HexTile.SideDirection.W));
    			}
    			break;
    		case SW:
    			vertice.addAdjacentSide(tile.getSide(HexTile.SideDirection.W));
    			vertice.addAdjacentSide(tile.getSide(HexTile.SideDirection.SW));
    			if (wTile != null) {
    				vertice.addAdjacentSide(wTile.getSide(HexTile.SideDirection.SE));
    			} else if (swTile != null) {
				 	vertice.addAdjacentSide(swTile.getSide(HexTile.SideDirection.NE));
    			}
    			break;
    		case NW:
    			vertice.addAdjacentSide(tile.getSide(HexTile.SideDirection.W));
    			vertice.addAdjacentSide(tile.getSide(HexTile.SideDirection.NW));
    			if (wTile != null) {
    				vertice.addAdjacentSide(wTile.getSide(HexTile.SideDirection.NE));
    			} else if (nwTile != null) {
				 	vertice.addAdjacentSide(nwTile.getSide(HexTile.SideDirection.SW));
    			}
    			break;
    		case N:
    			vertice.addAdjacentSide(tile.getSide(HexTile.SideDirection.NE));
    			vertice.addAdjacentSide(tile.getSide(HexTile.SideDirection.NW));
    			if (nwTile != null) {
    				vertice.addAdjacentSide(nwTile.getSide(HexTile.SideDirection.E));
    			} else if (neTile != null) {
				 	vertice.addAdjacentSide(neTile.getSide(HexTile.SideDirection.W));
    			} 
    			break;
    	}
    }
    
    /** Let each side know about the adjacent hexes to it's side (up to 2) */
    private void makeAdjacentHexes(HexTile tile, HexTile.SideDirection direction) {
    	HexSide side = tile.getSide(direction);

    	side.addAdjacentTile(tile, oppositeSide(direction));
    	if (tile.getAdjacentHex(direction) != null) {
    		side.addAdjacentTile(tile, direction);
    	}
    }

        
    /** Let each vertice know about the adjacent hexes to it's vertice (up to 3) */
    // TODO: Note that when adding an adjacent hex, the vertice direction N,NE,SE doesn't
    // matter at all. Remove this later!
    private void makeAdjacentHexes(HexTile tile, HexTile.VerticeDirection direction) {
    	HexVertice vertice = tile.getVertice(direction);
    	vertice.addAdjacentHex(tile, HexTile.VerticeDirection.N);
    	
    	HexTile tile1 = null;
    	HexTile tile2 = null;
    	
    	switch(direction) {
    		case N:
    			tile1 = tile.getAdjacentHex(HexTile.SideDirection.NW);
    			tile2 = tile.getAdjacentHex(HexTile.SideDirection.NE);
    			break;
    		case NE:
    			tile1 = tile.getAdjacentHex(HexTile.SideDirection.NE);
    			tile2 = tile.getAdjacentHex(HexTile.SideDirection.E);
    			break;
    		case SE:
    			tile1 = tile.getAdjacentHex(HexTile.SideDirection.E);
    			tile2 = tile.getAdjacentHex(HexTile.SideDirection.SE);
    			break;
    		case S:
    			tile1 = tile.getAdjacentHex(HexTile.SideDirection.SE);
    			tile2 = tile.getAdjacentHex(HexTile.SideDirection.SW);
    			break;
      		case SW:
    			tile1 = tile.getAdjacentHex(HexTile.SideDirection.SW);
    			tile2 = tile.getAdjacentHex(HexTile.SideDirection.W);
    			break;
    		case NW:
    			tile1 = tile.getAdjacentHex(HexTile.SideDirection.W);
    			tile2 = tile.getAdjacentHex(HexTile.SideDirection.NW);
    			break;    			
    	}
    	
    	if (tile1 != null) {
    		vertice.addAdjacentHex(tile1, HexTile.VerticeDirection.NE);
    	}
    	if (tile2 != null) {
    		vertice.addAdjacentHex(tile2, HexTile.VerticeDirection.SE);
    	}    	
    	
    
    }
    
}
