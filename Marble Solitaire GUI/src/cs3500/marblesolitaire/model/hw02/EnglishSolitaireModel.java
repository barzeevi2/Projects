package cs3500.marblesolitaire.model.hw02;

// @ Jason Ie

import java.util.ArrayList;

public class EnglishSolitaireModel implements MarbleSolitaireModel {
  private ArrayList<ArrayList<SlotState>> board;
  private int armThickness;
  private int score;

  // Default constructor with arm thickness 3
  public EnglishSolitaireModel() {
    this.armThickness = 3;
    this.board = this.initBoard(armThickness, armThickness);
    this.score = 32;
  }

  // Constructor with arm thickness 3, Empty slot at (sRow, sCol)
  public EnglishSolitaireModel(int sRow, int sCol) throws IllegalArgumentException {
    this.armThickness = 3;
    if (sRow >= this.getBoardSize() || sRow < 0 || sCol >= this.getBoardSize()
            || sCol < 0 || this.isInvalid(sRow, sCol)) {
      throw new IllegalArgumentException("Invalid empty cell position (" + sCol + "," + sRow + ")");
    } else {
      this.board = this.initBoard(sRow, sCol);
    }
    this.score = 32;
  }

  // Constructor with given arm thickness
  public EnglishSolitaireModel(int armThickness) throws IllegalArgumentException {
    if (armThickness < 0 || (armThickness % 2) == 0) {
      throw new IllegalArgumentException("Arm thickness must be a positive odd number");
    } else {
      this.armThickness = armThickness;
    }
    this.board = this.initBoard(armThickness, armThickness);
    this.score = ((this.getBoardSize() * armThickness) + ((armThickness + 1) * armThickness)) - 1;
  }

  // Finally a fourth constructor should take the arm thickness, row and column of the empty slot in that order.
  // It should throw an IllegalArgumentException if the arm thickness is not a positive odd number, or the empty cell position is invalid.
  public EnglishSolitaireModel(int armThickness, int sRow, int sCol) {
    if (armThickness < 0 || (armThickness % 2) == 0) {
      throw new IllegalArgumentException("Arm thickness must be a positive odd number");
    } else {
      this.armThickness = armThickness;
    }
    if (sRow >= this.getBoardSize() || sRow < 0 || sCol >= this.getBoardSize()
            || sCol < 0 || this.isInvalid(sRow, sCol)) {
      throw new IllegalArgumentException("Invalid empty cell position (" + sCol + "," + sRow + ")");
    } else {
      this.board = this.initBoard(sRow, sCol);
    }
    this.score = ((this.getBoardSize() * armThickness) + ((armThickness + 1) * armThickness)) - 1;
  }

  /**
   * Initialize the board
   *
   * @param sRow position of row index that the empty slot should be at
   * @param sCol position of column index that the empty slot should be at
   * @return data structure of desired board output
   */
  public ArrayList<ArrayList<SlotState>> initBoard(int sRow, int sCol) {
    ArrayList<ArrayList<SlotState>> b = new ArrayList<ArrayList<SlotState>>();
    for (int rowPos = 0; rowPos < this.getBoardSize(); rowPos++) {
      ArrayList<SlotState> col = new ArrayList<SlotState>();
      for (int colPos = 0; colPos < this.getBoardSize(); colPos++) {
//        System.out.println("Generating slot at (" + colPos + ", " + rowPos + ")");
        this.initState(rowPos, colPos, col, sRow, sCol);
      }
      b.add(col);
    }
    return b;
  }

  /**
   * Initialize the state of a square on the board
   *
   * @param rowPos position of row index (y variable if xy-plane)
   * @param colPos position of column index (x variable if xy-plane)
   * @param col    ArrayList of slot states which is getting correct state added
   * @param sRow   position of row index that the empty slot should be at
   * @param sCol   position of column index that the empty slot should be at
   */
  public void initState(int rowPos, int colPos, ArrayList<SlotState> col, int sRow, int sCol) {
    if (this.isInvalid(rowPos, colPos)) {
      col.add(SlotState.Invalid);
    } else if (rowPos == sCol && colPos == sRow) {
      col.add(SlotState.Empty);
    } else {
      col.add(SlotState.Marble);
    }
  }

  /**
   * Determine and return if this position is invalid or not.
   *
   * @param rowPos position of row index
   * @param colPos position of column index
   * @return true if slot at given coordinates is invalid, false if not
   */
  public boolean isInvalid(int rowPos, int colPos) {
    int invalidSquareSize = this.armThickness / 2;
    return (rowPos <= invalidSquareSize
            && colPos <= invalidSquareSize)
            || (rowPos <= invalidSquareSize
            && (colPos > invalidSquareSize + this.armThickness))
            || ((rowPos > invalidSquareSize + this.armThickness)
            && colPos <= invalidSquareSize)
            || (rowPos > (invalidSquareSize + this.armThickness)
            && colPos > (invalidSquareSize + this.armThickness));
  }

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

  public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
    if (this.getSlotAt(fromRow, fromCol).equals(SlotState.Marble)
            && (this.getSlotAt(toRow, toCol).equals(SlotState.Empty))) {
      if (toCol - fromCol == 2) {
        return (this.getSlotAt(fromRow, fromCol + 1).equals(SlotState.Marble));
      } else if (toCol - fromCol == -2) {
        return (this.getSlotAt(fromRow, fromCol - 1).equals(SlotState.Marble));
      } else if (toRow - fromRow == 2) {
        return (this.getSlotAt(fromRow + 1, fromCol).equals(SlotState.Marble));
      } else if (toRow - fromRow == -2) {
        return (this.getSlotAt(fromRow - 1, fromCol).equals(SlotState.Marble));
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  // javadoc in interface
  @Override
  public void move(int fromRow, int fromCol, int toRow, int toCol) throws IllegalArgumentException {
    if (this.getSlotAt(fromRow, fromCol).equals(SlotState.Marble)) {
      if (this.getSlotAt(toRow, toCol).equals(SlotState.Empty)) {
        if (toCol - fromCol == 2) {
          if (this.getSlotAt(fromRow, fromCol + 1).equals(SlotState.Marble)) {
            this.board.get(fromCol).set(fromRow, SlotState.Empty);
            this.board.get(fromCol + 1).set(fromRow, SlotState.Empty);
            this.board.get(toCol).set(toRow, SlotState.Marble);
            score--;
            System.out.println("Marble moved from (" + fromCol + ", " + fromRow + ") to (" + toCol + ", " + toRow + ")");
          } else {
            throw new IllegalArgumentException("Must jump over a space with a marble");
          }
        } else if (toCol - fromCol == -2) {
          if (this.getSlotAt(fromRow, fromCol - 1).equals(SlotState.Marble)) {
            this.board.get(fromCol).set(fromRow, SlotState.Empty);
            this.board.get(fromCol - 1).set(fromRow, SlotState.Empty);
            this.board.get(toCol).set(toRow, SlotState.Marble);
            score--;
            System.out.println("Marble moved from (" + fromCol + ", " + fromRow + ") to (" + toCol + ", " + toRow + ")");
          } else {
            throw new IllegalArgumentException("Must jump over a space with a marble");
          }
        } else if (toRow - fromRow == 2) {
          if (this.getSlotAt(fromRow + 1, fromCol).equals(SlotState.Marble)) {
            this.board.get(fromCol).set(fromRow, SlotState.Empty);
            this.board.get(fromCol).set(fromRow + 1, SlotState.Empty);
            this.board.get(toCol).set(toRow, SlotState.Marble);
            score--;
            System.out.println("Marble moved from (" + fromCol + ", " + fromRow + ") to (" + toCol + ", " + toRow + ")");
          } else {
            throw new IllegalArgumentException("Must jump over a space with a marble");
          }
        } else if (toRow - fromRow == -2) {
          if (this.getSlotAt(fromRow - 1, fromCol).equals(SlotState.Marble)) {
            this.board.get(fromCol).set(fromRow, SlotState.Empty);
            this.board.get(fromCol).set(fromRow - 1, SlotState.Empty);
            this.board.get(toCol).set(toRow, SlotState.Marble);
            score--;
            System.out.println("Marble moved from (" + fromCol + ", " + fromRow + ") to (" + toCol + ", " + toRow + ")");
          } else {
            throw new IllegalArgumentException("Must jump over a space with a marble");
          }
        } else {
          throw new IllegalArgumentException("The to and from positions must be exactly 2 " +
                  "positions away from each other");
        }
      } else {
        throw new IllegalArgumentException("The to position must be empty");
      }
    } else {
      throw new IllegalArgumentException("The from position must have a marble in its slot");
    }
  }

  // javadoc in interface
  @Override
  public int getBoardSize() {
    return (this.armThickness * 2) + 1;
  }

  // javadoc in interface
  @Override
  public SlotState getSlotAt(int row, int col) throws IllegalArgumentException {
    for (int rowPos = 0; rowPos < this.getBoardSize(); rowPos++) {
      for (int colPos = 0; colPos < this.getBoardSize(); colPos++) {
        if (rowPos == row && colPos == col) {
//          System.out.println("On an xy plane, getting slot at (" + colPos + ", " + rowPos + ")");
          return board.get(colPos).get(rowPos);
        }
      }
    }
    throw new IllegalArgumentException("Row and column positions must be within the board size");
  }


  // javadoc in interface
  @Override
  public int getScore() {
    return score;
  }

  // javadoc in interface
  @Override
  public boolean isGameOver() {
    for (int i = 0; i < getBoardSize(); i++) {
      for (int j = 0; j < getBoardSize(); j++) {
        if (i < getBoardSize() - 2) {
          if (isValidMove(i, j, i + 2, j)) {
            return false;
          }
        }
        if (j < getBoardSize() - 2) {
          if (isValidMove(i, j, i, j + 2)) {
            return false;
          }
        }
        if (i > 1) {
          if (isValidMove(i, j, i - 2, j)) {
            return false;
          }
        }
        if (j > 1) {
          if (isValidMove(i, j, i, j - 2)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  /*
   * for testing purposes, prints what state is in each slot of a board

  public void testingGridStates() {
    for (int rowPos = 0; rowPos < this.getBoardSize(); rowPos++) {
      for (int colPos = 0; colPos < this.getBoardSize(); colPos++) {
        System.out.println("Putting " + this.board.get(colPos).get(rowPos) + " at (" + colPos + ", " + rowPos + ")");
      }
    }
  }
   */
}


