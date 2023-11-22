package cs3500.marblesolitaire.model.hw04;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import cs3500.marblesolitaire.model.hw02.EnglishSolitaireModel;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModelState;
import cs3500.marblesolitaire.view.MarbleSolitaireTextView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class EuropeanSolitaireModelTest {
  EuropeanSolitaireModel baseModel;

  // model with arm thickness 3 and the empty slot at (2, 2)
  EuropeanSolitaireModel emptySlotAt22;

  // model with arm thickness 5 and the empty slot at (5, 5)
  EuropeanSolitaireModel armThickness5;

  // model with arm thickness 5 and the empty slot at (4, 3)
  EuropeanSolitaireModel arm5Empty43;

  // Array to test the values in the first row of the baseModel board
  MarbleSolitaireModelState.SlotState[] row0;

  public void init() {
    baseModel = new EuropeanSolitaireModel();
    emptySlotAt22 = new EuropeanSolitaireModel(2, 2);
    armThickness5 = new EuropeanSolitaireModel(5);
    arm5Empty43 = new EuropeanSolitaireModel(5, 3, 4);
    row0 = new MarbleSolitaireModelState.SlotState[]{MarbleSolitaireModelState.SlotState.Invalid,
            MarbleSolitaireModelState.SlotState.Marble,
            MarbleSolitaireModelState.SlotState.Marble,
            MarbleSolitaireModelState.SlotState.Marble,
            MarbleSolitaireModelState.SlotState.Marble,
            MarbleSolitaireModelState.SlotState.Marble,
            MarbleSolitaireModelState.SlotState.Invalid};
  }

  @Test
  public void testInvalidEmptyCellException() {
    try {
      EuropeanSolitaireModel emptySlotAtInvalid = new EuropeanSolitaireModel(0, 1);
      fail();
    } catch (IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
    try {
      EuropeanSolitaireModel negativeEmptySlot = new EuropeanSolitaireModel(-3, 3);
      fail();
    } catch (IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
    try {
      EuropeanSolitaireModel emptySlotOutsideBoard = new EuropeanSolitaireModel(8, 5);
      fail();
    } catch (IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
  }

  @Test
  public void testPositiveOddArmException() {
    try {
      EuropeanSolitaireModel negativeArmThickness = new EuropeanSolitaireModel(-5);
      fail();
    } catch (IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
    try {
      EuropeanSolitaireModel evenArmThickness = new EuropeanSolitaireModel(4);
      fail();
    } catch (IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
  }

  @Test
  public void testBigConstructor() {
    try {
      EuropeanSolitaireModel invalidArmThickness = new EuropeanSolitaireModel(-5, 3, 4);
      fail();
    } catch (IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
    try {
      EuropeanSolitaireModel invalidEmpty = new EuropeanSolitaireModel(5, -3, 4);
      fail();
    } catch (IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
  }

  @Test
  public void testInitBoard() {
    init();
    assertEquals(7, baseModel.initBoard(3, 3).size());
    for (int i = 0; i < 7; i++) {
      assertEquals(7, baseModel.initBoard(3, 3).get(i).size());
      assertEquals(row0[i], baseModel.initBoard(3, 3).get(1).get(i));
    }
  }

  @Test
  public void testInitState() {
    init();
    for (int rowPos = 0; rowPos < baseModel.getBoardSize(); rowPos++) {
      ArrayList<MarbleSolitaireModelState.SlotState> col = new ArrayList<MarbleSolitaireModelState.SlotState>();
      for (int colPos = 0; colPos < baseModel.getBoardSize(); colPos++) {
        baseModel.initState(rowPos, colPos, col, 3, 3);
        assertEquals(baseModel.initBoard(3, 3).get(colPos).get(rowPos), col.get(colPos));
      }
    }
  }

  @Test
  public void testIsInvalid() {
    init();
    assertEquals(true, baseModel.isInvalid(0, 0));
    assertEquals(true, baseModel.isInvalid(0, 1));
    assertEquals(true, baseModel.isInvalid(1, 0));
    assertEquals(false, baseModel.isInvalid(2, 2));
    assertEquals(false, baseModel.isInvalid(3, 3));
    assertEquals(false, baseModel.isInvalid(4, 2));
    assertEquals(false, baseModel.isInvalid(2, 4));

    assertEquals(true, emptySlotAt22.isInvalid(0, 1));
    assertEquals(false, emptySlotAt22.isInvalid(2, 2));
    assertEquals(true, armThickness5.isInvalid(9, 1));
    assertEquals(false, armThickness5.isInvalid(3, 4));
    assertEquals(false, arm5Empty43.isInvalid(9, 8));
    assertEquals(false, arm5Empty43.isInvalid(3, 4));
  }

  @Test
  public void testIsValidMove() {
    init();
    assertEquals(true, baseModel.isValidMove(1, 3, 3, 3));
    assertEquals(true, baseModel.isValidMove(3, 1, 3, 3));
    assertEquals(true, baseModel.isValidMove(5, 3, 3, 3));
    assertEquals(false, baseModel.isValidMove(1, 2, 2, 2));
    assertEquals(false, baseModel.isValidMove(1, 3, 3, 4));
    assertEquals(false, baseModel.isValidMove(6, 3, 2, 5));
    assertEquals(false, baseModel.isValidMove(2, 4, 4, 2));

    assertEquals(true, emptySlotAt22.isValidMove(2, 0, 2, 2));
    assertEquals(false, emptySlotAt22.isValidMove(2, 2, 4, 6));
    assertEquals(true, armThickness5.isValidMove(3, 5, 5, 5));
    assertEquals(false, armThickness5.isValidMove(3, 5, 4, 5));
    assertEquals(true, arm5Empty43.isValidMove(3, 6, 3, 4));
    assertEquals(false, arm5Empty43.isValidMove(3, 6, 3, 3));
  }

  @Test
  public void testMove() {
    init();
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, baseModel.getSlotAt(3, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, baseModel.getSlotAt(2, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, baseModel.getSlotAt(1, 3));
    baseModel.move(1, 3, 3, 3);
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, baseModel.getSlotAt(3, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, baseModel.getSlotAt(2, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, baseModel.getSlotAt(1, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, baseModel.getSlotAt(2, 5));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, baseModel.getSlotAt(2, 4));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, baseModel.getSlotAt(2, 3));
    baseModel.move(2, 5, 2, 3);
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, baseModel.getSlotAt(2, 5));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, baseModel.getSlotAt(2, 4));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, baseModel.getSlotAt(2, 3));
    ;  }

  @Test
  public void testNoMarbleInMiddle() {
    init();
    baseModel.move(1, 3, 3, 3);
    try {
      baseModel.move(0, 3, 2, 3);
      fail("Tried to move marble at (3, 0) to (3, 2) with no marble at (3, 1) and " +
              "did not see an IllegalArgumentException");
    } catch(IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
    arm5Empty43.move(3, 6, 3, 4);
    try {
      arm5Empty43.move(3, 4, 3, 6);
      fail("Tried to move marble at (4, 3) to (6, 3) with no marble at (5, 3) and " +
              "did not see an IllegalArgumentException");
    } catch(IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
  }

  @Test
  public void testNot2PositionsAway() {
    init();
    baseModel.move(1, 3, 3, 3);
    try {
      baseModel.move(4, 3, 1, 3);
      fail("Tried to move marble at (3, 4) to (3, 1) which is not exactly 2 positions away and " +
              "did not see an IllegalArgumentException");
    } catch(IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
    try {
      baseModel.move(0, 3, 1, 3);
      fail("Tried to move marble at (3, 0) to (3, 1) which is not exactly 2 positions away and " +
              "did not see an IllegalArgumentException");
    } catch(IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
  }

  @Test
  public void testToPositionNotEmpty() {
    init();
    try {
      baseModel.move(1, 3, 2, 3);
      fail("Tried to move marble at (3, 1) to (3, 2) when (3, 2) is not empty and " +
              "did not see an IllegalArgumentException");
    } catch(IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
    try {
      arm5Empty43.move(5, 6, 3, 6);
      fail("Tried to move marble at (6, 5) to (6, 3) when (6, 3) is not empty and " +
              "did not see an IllegalArgumentException");
    } catch(IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
  }

  @Test
  public void testFromPositionNotMarble() {
    init();
    try {
      baseModel.move(0, 1, 2, 3);
      fail("Tried to move marble at (1, 0) to (3, 2) with no marble at (1, 0) and " +
              "did not see an IllegalArgumentException");
    } catch(IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
    try {
      arm5Empty43.move(3, 4, 3, 6);
      fail("Tried to move marble at (4, 3) to (6, 3) with no marble at (4, 3) and " +
              "did not see an IllegalArgumentException");
    } catch(IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
  }

  @Test
  public void testGetBoardSize() {
    init();
    assertEquals(7, baseModel.getBoardSize());
    assertEquals(11, armThickness5.getBoardSize());
    assertEquals(15, new EnglishSolitaireModel(7).getBoardSize());
  }

  @Test
  public void testGetSlotAt() {
    init();
    for (int i = 0; i < baseModel.getBoardSize(); i++) {
      for (int j = 0; j < baseModel.getBoardSize(); j++) {
        assertEquals(baseModel.initBoard(3, 3).get(i).get(j),
                baseModel.getSlotAt(i, j));
      }
    }
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid, baseModel.getSlotAt(0, 1));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, baseModel.getSlotAt(1, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, baseModel.getSlotAt(2, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, baseModel.getSlotAt(3, 3));
    baseModel.move(1, 3, 3, 3);
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, baseModel.getSlotAt(1, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, baseModel.getSlotAt(2, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, baseModel.getSlotAt(3, 3));
  }

  @Test
  public void testGetSlotAtOutOfBounds() {
    init();
    try {
      baseModel.getSlotAt(-1, 4);
      fail("Tried to get slot at (4, -1) when -1 is out of bounds and " +
              "did not see an IllegalArgumentException");
    } catch(IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
    try {
      armThickness5.getSlotAt(12, 9);
      fail("Tried to get slot at (9, 12) when 12 is out of bounds and " +
              "did not see an IllegalArgumentException");
    } catch(IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
    try {
      armThickness5.getSlotAt(0, 14);
      fail("Tried to get slot at (14, 0) when 14 is out of bounds and " +
              "did not see an IllegalArgumentException");
    } catch(IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
  }

  @Test
  public void testGetScore() {
    init();
    assertEquals(36, baseModel.getScore());
    baseModel.move(1, 3, 3, 3);
    assertEquals(35, baseModel.getScore());
    baseModel.move(2, 5, 2, 3);
    baseModel.move(0, 4, 2, 4);
    baseModel.move(2, 3, 2, 5);
    assertEquals(32, baseModel.getScore());
    assertEquals(96, armThickness5.getScore());
    armThickness5.move(3, 5, 5, 5);
    armThickness5.move(3, 7, 3, 5);
    assertEquals(94, armThickness5.getScore());
  }

  @Test
  public void testGameOver() {
    init();
    assertEquals(false, baseModel.isGameOver());
    baseModel.move(3, 1, 3, 3);
    baseModel.move(5, 2, 3, 2);
    baseModel.move(4, 0, 4, 2);
    baseModel.move(4, 3, 4, 1);
    baseModel.move(4, 5, 4, 3);
    baseModel.move(6, 4, 4, 4);
    baseModel.move(3, 4, 5, 4);
    baseModel.move(6, 2, 6, 4);
    baseModel.move(6, 4, 4, 4);
    baseModel.move(2, 2, 4, 2);
    baseModel.move(0, 2, 2, 2);
    baseModel.move(1, 4, 3, 4);
    baseModel.move(3, 4, 5, 4);
    baseModel.move(5, 4, 5, 2);
    baseModel.move(5, 2, 3, 2);
    baseModel.move(3, 2, 1, 2);
    baseModel.move(2, 0, 4, 0);
    baseModel.move(4, 0, 4, 2);
    baseModel.move(4, 2, 4, 4);
    baseModel.move(2, 6, 2, 4);
    baseModel.move(2, 3, 2, 5);
    baseModel.move(4, 6, 2, 6);
    baseModel.move(2, 6, 2, 4);
    baseModel.move(0, 4, 0, 2);
    baseModel.move(0, 2, 2, 2);
    baseModel.move(2, 1, 2, 3);
    baseModel.move(2, 3, 2, 5);
    baseModel.move(2, 5, 4, 5);
    baseModel.move(4, 5, 4, 3);
    baseModel.move(4, 3, 2, 3);
    assertEquals(false, baseModel.isGameOver());
    baseModel.move(1, 3, 3, 3);
    assertEquals(true, baseModel.isGameOver());
    init();
    assertEquals(false, baseModel.isGameOver());
    baseModel.move(3, 1, 3, 3);
    baseModel.move(5, 2, 3, 2);
    baseModel.move(4, 0, 4, 2);
    baseModel.move(4, 3, 4, 1);
    baseModel.move(4, 5, 4, 3);
    baseModel.move(6, 4, 4, 4);
    baseModel.move(3, 4, 5, 4);
    baseModel.move(6, 2, 6, 4);
    baseModel.move(6, 4, 4, 4);
    baseModel.move(2, 2, 4, 2);
    baseModel.move(0, 2, 2, 2);
    baseModel.move(1, 4, 3, 4);
    baseModel.move(3, 4, 5, 4);
    baseModel.move(5, 4, 5, 2);
    baseModel.move(5, 2, 3, 2);
    baseModel.move(3, 2, 1, 2);
    baseModel.move(2, 0, 4, 0);
    baseModel.move(4, 0, 4, 2);
    baseModel.move(4, 2, 4, 4);
    baseModel.move(2, 6, 2, 4);
    baseModel.move(2, 3, 2, 5);
    baseModel.move(4, 6, 2, 6);
    baseModel.move(2, 6, 2, 4);
    baseModel.move(0, 4, 0, 2);
    baseModel.move(1, 2, 1, 4);
    assertEquals(false, baseModel.isGameOver());
    baseModel.move(2, 4, 0, 4);
    baseModel.move(1, 1, 3, 1);
    assertEquals(true, baseModel.isGameOver());
    init();
    baseModel.move(3, 1, 3, 3);
    baseModel.move(1, 2, 3, 2);
    assertEquals(false, baseModel.isGameOver());
  }
}
