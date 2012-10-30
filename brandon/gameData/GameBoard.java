package brandon.gameData;

import brandon.utils.Log;
import java.io.Serializable;

public abstract class GameBoard implements Serializable {
	protected int rows;
	protected int cols;

    /** Return the number of rows in this game board */
    public int getRows() {
		return rows;
	}
    
    /** Returns the number of columns in this game board */
    public int getColumns() {
    	return cols;
    }
    
    /** Set the size of the game board */
    public GameBoard(int cols, int rows) {
    	Log.info(this, "GameBoard(" + cols + ", " + rows + ")", " called");
    	this.rows = rows;
    	this.cols = cols;
    }

    public GameBoard() {
    }
}
