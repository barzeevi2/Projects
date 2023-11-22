package cs3500.marblesolitaire.model.hw04;

import java.util.ArrayList;

public class EuropeanSolitaireModel extends ASolitaireModel {
  public EuropeanSolitaireModel() {
    this(3, 3, 3);
  }

  public EuropeanSolitaireModel(int armThickness) {
    this(armThickness, armThickness, armThickness);
  }

  public EuropeanSolitaireModel(int sRow, int sCol) {
    this(3, sRow, sCol);
  }

  public EuropeanSolitaireModel(int armThickness, int sRow, int sCol) {
    super(armThickness, sRow, sCol);
    if (armThickness < 0 || (armThickness % 2) == 0) {
      throw new IllegalArgumentException("Arm thickness must be a positive odd number");
    }
    this.score = (((this.getBoardSize() * armThickness) + ((armThickness + 1) * armThickness)) - 1)
            + ((int) ((armThickness * armThickness) / 2));
  }

  @Override
  public boolean isInvalid(int rowPos, int colPos) {
    int invalidSquareSize = this.armThickness / 2;
    int doubleArmThick = 2 * armThickness;

    return (rowPos + colPos <= invalidSquareSize
            && colPos + rowPos <= invalidSquareSize)
            ||
            (rowPos <= invalidSquareSize
                    && (colPos > invalidSquareSize + this.armThickness + rowPos))
            ||
            ((rowPos > (invalidSquareSize + this.armThickness + colPos))
                    && colPos <= invalidSquareSize)
            ||
            (rowPos > (invalidSquareSize + this.armThickness + doubleArmThick - colPos)
                    && colPos > (invalidSquareSize + this.armThickness) + doubleArmThick - rowPos);
  }

  @Override
  public int getBoardSize() {
    return (this.armThickness * 2) + 1;
  }

}
