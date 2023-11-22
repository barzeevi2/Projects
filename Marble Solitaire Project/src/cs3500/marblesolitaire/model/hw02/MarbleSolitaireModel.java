package cs3500.marblesolitaire.model.hw02;

import java.util.ArrayList;

/**
 * This interface represents the operations offered by the marble solitaire
 * model. One object of the model represents one game of marble solitaire
 */
public interface MarbleSolitaireModel extends MarbleSolitaireModelState {

  /**
   * Initialize the board
   *
   * @param sRow position of row index that the empty slot should be at
   * @param sCol position of column index that the empty slot should be at
   * @return data structure of desired board output
   */
  ArrayList<ArrayList<SlotState>> initBoard(int sRow, int sCol);

  /**
   * Initialize the state of a square on the board
   *
   * @param rowPos position of row index (y variable if xy-plane)
   * @param colPos position of column index (x variable if xy-plane)
   * @param col    ArrayList of slot states which is getting correct state added
   * @param sRow   position of row index that the empty slot should be at
   * @param sCol   position of column index that the empty slot should be at
   */
  void initState(int rowPos, int colPos, ArrayList<SlotState> col, int sRow, int sCol);

  /**
   * Determine and return if this position is invalid or not.
   *
   * @param rowPos position of row index
   * @param colPos position of column index
   * @return true if slot at given coordinates is invalid, false if not
   */
  boolean isInvalid(int rowPos, int colPos);

  /**
   * @param fromRow the row number of the position to be moved from
   *                (starts at 0)
   * @param fromCol the column number of the position to be moved from
   *                (starts at 0)
   * @param toRow   the row number of the position to be moved to
   *                (starts at 0)
   * @param toCol   the column number of the position to be moved to
   *                (starts at 0)
   * @return determine whether the given 'from' row and column can be moved
   * to the 'to' row and column
   */

  boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol);

  /**
   * Move a single marble from a given position to another given position.
   * A move is valid only if the from and to positions are valid. Specific
   * implementations may place additional constraints on the validity of a move.
   *
   * @param fromRow the row number of the position to be moved from
   *                (starts at 0)
   * @param fromCol the column number of the position to be moved from
   *                (starts at 0)
   * @param toRow   the row number of the position to be moved to
   *                (starts at 0)
   * @param toCol   the column number of the position to be moved to
   *                (starts at 0)
   * @throws IllegalArgumentException if the move is not possible
   */

  void move(int fromRow, int fromCol, int toRow, int toCol) throws
          IllegalArgumentException;

  /**
   * Determine and return if the game is over or not. A game is over if no
   * more moves can be made.
   *
   * @return true if the game is over, false otherwise
   */
  boolean isGameOver();
}
