package cs3500.marblesolitaire.model.hw04;

import java.util.ArrayList;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModelState;

public abstract class ASolitaireModel implements MarbleSolitaireModel {
  protected ArrayList<ArrayList<SlotState>> board;
  protected int armThickness;
  protected int score;

  public ASolitaireModel(int armThickness, int sRow, int sCol) {
    this.armThickness = armThickness;
    if (sRow >= this.getBoardSize() || sRow < 0 || sCol >= this.getBoardSize()
            || sCol < 0 || this.isInvalid(sCol, sRow)) {
      throw new IllegalArgumentException("Invalid empty cell position — Row: " + sRow + ", Col: " + sCol);
    } else {
      this.board = this.initBoard(sRow, sCol);
    }
  }

  @Override
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

  @Override
  public void initState(int rowPos, int colPos, ArrayList<SlotState> col, int sRow, int sCol) {
    if (this.isInvalid(rowPos, colPos)) {
      col.add(SlotState.Invalid);
    } else if (rowPos == sCol && colPos == sRow) {
      col.add(SlotState.Empty);
    } else {
      col.add(SlotState.Marble);
    }
  }

  @Override
  public abstract boolean isInvalid(int rowPos, int colPos);

  @Override
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

  @Override
  public int getScore() {
    return score;
  }

  public abstract int getBoardSize();
}
