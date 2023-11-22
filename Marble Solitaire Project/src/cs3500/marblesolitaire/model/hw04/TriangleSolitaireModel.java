package cs3500.marblesolitaire.model.hw04;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModelState;

public class TriangleSolitaireModel extends ASolitaireModel {
  public TriangleSolitaireModel() {
    this(5, 0, 0);
  }

  public TriangleSolitaireModel(int dimensions) {
    this(dimensions, 0, 0);
  }

  public TriangleSolitaireModel(int sRow, int sCol) {
    this(5, sRow, sCol);
  }

  public TriangleSolitaireModel(int dimensions, int sRow, int sCol) {
    super(dimensions, sRow, sCol);
    if (dimensions <= 0) {
      throw new IllegalArgumentException("Dimension must be a positive integer");
    }
    for (int i = dimensions; i > 1; i--) {
      this.score += i;
    }
  }

  @Override
  public boolean isInvalid(int rowPos, int colPos) {
    return rowPos > colPos;
  }

  @Override
  public int getBoardSize() {
    return this.armThickness;
  }

  @Override
  public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
    if (this.getSlotAt(fromRow, fromCol).equals(SlotState.Marble)
            && (this.getSlotAt(toRow, toCol).equals(SlotState.Empty))) {
      if (toCol - fromCol == 2) {
        if (toRow - fromRow == 2) {
          return (this.getSlotAt(fromRow + 1, fromCol + 1).equals(SlotState.Marble));
        }
        return (this.getSlotAt(fromRow, fromCol + 1).equals(SlotState.Marble));
      } else if (toCol - fromCol == -2) {
        if (toRow - fromRow == -2) {
          return (this.getSlotAt(fromRow - 1, fromCol - 1).equals(SlotState.Marble));
        }
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

  @Override
  public void move(int fromRow, int fromCol, int toRow, int toCol) throws IllegalArgumentException {
    if (this.getSlotAt(fromRow, fromCol).equals(SlotState.Marble)) {
      if (this.getSlotAt(toRow, toCol).equals(SlotState.Empty)) {
        if (toCol - fromCol == 2) {
          if (toRow - fromRow == 2) {
            if (this.getSlotAt(fromRow + 1, fromCol + 1).equals(SlotState.Marble)) {
              this.board.get(fromCol).set(fromRow, SlotState.Empty);
              this.board.get(fromCol + 1).set(fromRow + 1, SlotState.Empty);
              this.board.get(toCol).set(toRow, SlotState.Marble);
              score--;
              System.out.println("Marble moved from (" + fromRow + ", " + fromCol + ") to (" + toRow + ", " + toCol + ")");
              return;
            }
          } else if (this.getSlotAt(fromRow, fromCol + 1).equals(SlotState.Marble)) {
            this.board.get(fromCol).set(fromRow, SlotState.Empty);
            this.board.get(fromCol + 1).set(fromRow, SlotState.Empty);
            this.board.get(toCol).set(toRow, SlotState.Marble);
            score--;
            System.out.println("Marble moved from (" + fromRow + ", " + fromCol + ") to (" + toRow + ", " + toCol + ")");
          } else {
            throw new IllegalArgumentException("Must jump over a space with a marble");
          }
        } else if (toCol - fromCol == -2) {
          if (toRow - fromRow == -2) {
            if (this.getSlotAt(fromRow - 1, fromCol - 1).equals(SlotState.Marble)) {
              this.board.get(fromCol).set(fromRow, SlotState.Empty);
              this.board.get(fromCol - 1).set(fromRow - 1, SlotState.Empty);
              this.board.get(toCol).set(toRow, SlotState.Marble);
              score--;
              System.out.println("Marble moved from (" + fromRow + ", " + fromCol + ") to (" + toRow + ", " + toCol + ")");
              return;
            }
          } else if (this.getSlotAt(fromRow, fromCol - 1).equals(SlotState.Marble)) {
            this.board.get(fromCol).set(fromRow, SlotState.Empty);
            this.board.get(fromCol - 1).set(fromRow, SlotState.Empty);
            this.board.get(toCol).set(toRow, SlotState.Marble);
            score--;
            System.out.println("Marble moved from (" + fromRow + ", " + fromCol + ") to (" + toRow + ", " + toCol + ")");
          } else {
            throw new IllegalArgumentException("Must jump over a space with a marble");
          }
        } else if (toRow - fromRow == 2) {
          if (this.getSlotAt(fromRow + 1, fromCol).equals(SlotState.Marble)) {
            this.board.get(fromCol).set(fromRow, SlotState.Empty);
            this.board.get(fromCol).set(fromRow + 1, SlotState.Empty);
            this.board.get(toCol).set(toRow, SlotState.Marble);
            score--;
            System.out.println("Marble moved from (" + fromRow + ", " + fromCol + ") to (" + toRow + ", " + toCol + ")");
          } else {
            throw new IllegalArgumentException("Must jump over a space with a marble");
          }
        } else if (toRow - fromRow == -2) {
          if (this.getSlotAt(fromRow - 1, fromCol).equals(SlotState.Marble)) {
            this.board.get(fromCol).set(fromRow, SlotState.Empty);
            this.board.get(fromCol).set(fromRow - 1, SlotState.Empty);
            this.board.get(toCol).set(toRow, SlotState.Marble);
            score--;
            System.out.println("Marble moved from (" + fromRow + ", " + fromCol + ") to (" + toRow + ", " + toCol + ")");
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
}
