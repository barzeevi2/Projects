package cs3500.marblesolitaire.model.hw02;

import java.util.ArrayList;
import java.util.List;

public class MarbleSolitaireMock implements MarbleSolitaireModel {
  private final List<String> log;

  public MarbleSolitaireMock(List<String> log) {
    this.log = log;
  }

  @Override
  public ArrayList<ArrayList<SlotState>> initBoard(int sRow, int sCol) {
    this.log.add(String.format("initBoard()"));
    return new ArrayList<ArrayList<SlotState>>();
  }

  public void initState(int rowPos, int colPos, ArrayList<SlotState> col, int sRow, int sCol) {
    this.log.add(String.format("initState()"));

  }

  @Override
  public boolean isInvalid(int rowPos, int colPos) {
    this.log.add(String.format("isInvalid()"));
    return true;
  }

  @Override
  public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
    this.log.add(String.format("isValidMove()"));
    return true;
  }

  @Override
  public void move(int fromRow, int fromCol, int toRow, int toCol) throws IllegalArgumentException {
    this.log.add(String.format("move(%d, %d, %d, %d)", fromRow, fromCol, toRow, toCol));
  }

  @Override
  public boolean isGameOver() {
    this.log.add(String.format("isGameOver()"));
    return false;
  }

  @Override
  public int getBoardSize() {
    this.log.add(String.format("getBoardSize()"));
    return 0;
  }

  @Override
  public SlotState getSlotAt(int row, int col) throws IllegalArgumentException {
    this.log.add(String.format("getSlotAt()"));
    return SlotState.Marble;
  }

  @Override
  public int getScore() {
    this.log.add(String.format("getScore()"));
    return 0;
  }
}
