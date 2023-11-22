package cs3500.marblesolitaire.model.hw02;

// @ Jason Ie
import java.util.ArrayList;

import cs3500.marblesolitaire.model.hw04.ASolitaireModel;

public class EnglishSolitaireModel extends ASolitaireModel{

  // Default constructor with arm thickness 3
  public EnglishSolitaireModel() {
    this(3, 3, 3);
  }

  // Constructor with arm thickness 3, Empty slot at (sRow, sCol)
  public EnglishSolitaireModel(int sRow, int sCol) throws IllegalArgumentException {
    this(3, sRow, sCol);
  }

  // Constructor with given arm thickness
  public EnglishSolitaireModel(int armThickness) throws IllegalArgumentException {
    this(armThickness, armThickness, armThickness);
    if (armThickness < 0 || (armThickness % 2) == 0) {
      throw new IllegalArgumentException("Arm thickness must be a positive odd number");
    }
  }

  // Finally a fourth constructor should take the arm thickness, row and column of the empty slot in that order.
  // It should throw an IllegalArgumentException if the arm thickness is not a positive odd number, or the empty cell position is invalid.
  public EnglishSolitaireModel(int armThickness, int sRow, int sCol) {
    super(armThickness, sRow, sCol);
    if (armThickness < 0 || (armThickness % 2) == 0) {
      throw new IllegalArgumentException("Arm thickness must be a positive odd number");
    }
    this.score = ((this.getBoardSize() * armThickness) + ((armThickness + 1) * armThickness)) - 1;
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

  @Override
  public int getBoardSize() {
    return (this.armThickness * 2) + 1;
  }
}


