package cs3500.marblesolitaire.model.hw04;

import org.junit.Test;

import java.util.ArrayList;

import cs3500.marblesolitaire.model.hw02.EnglishSolitaireModel;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModelState;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TriangleSolitaireModelTest {

  // model with dimension 5 and the empty slot at (0, 0)
  TriangleSolitaireModel baseModel;

  // model with dimension 5 and the empty slot at (2, 2)
  TriangleSolitaireModel emptySlotAt22;

  // model with dimension 7 and the empty slot at (0, 0)
  TriangleSolitaireModel dimension7;

  // model with dimension 6 and the empty slot at (4, 3)
  TriangleSolitaireModel dim8Empty64;

  // Array to test the values in the first row of the baseModel board
  MarbleSolitaireModelState.SlotState[] row0;

  public void init() {
    baseModel = new TriangleSolitaireModel();
    emptySlotAt22 = new TriangleSolitaireModel(2, 2);
    dimension7 = new TriangleSolitaireModel(7);
    dim8Empty64 = new TriangleSolitaireModel(8, 6, 4);
    row0 = new MarbleSolitaireModelState.SlotState[]{MarbleSolitaireModelState.SlotState.Empty};
  }

  @Test
  public void testInvalidEmptyCellException() {
    try {
      TriangleSolitaireModel emptySlotAtInvalid = new TriangleSolitaireModel(0, 1);
      fail();
    } catch (IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
    try {
      TriangleSolitaireModel negativeEmptySlot = new TriangleSolitaireModel(-3, 3);
      fail();
    } catch (IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
    try {
      TriangleSolitaireModel emptySlotOutsideBoard = new TriangleSolitaireModel(8, 5);
      fail();
    } catch (IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
  }

  @Test
  public void testNegativeArmException() {
    try {
      TriangleSolitaireModel negativeArmThickness = new TriangleSolitaireModel(-5);
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
    assertEquals(5, baseModel.initBoard(0, 0).size());
    for (int i = 0; i < 1; i++) {
      assertEquals(5, baseModel.initBoard(3, 3).get(i).size());
      assertEquals(row0[i], baseModel.initBoard(0, 0).get(0).get(i));
    }
  }

  @Test
  public void testIsInvalid() {
    init();
    assertEquals(false, baseModel.isInvalid(0, 0));
    assertEquals(false, baseModel.isInvalid(0, 1));
    assertEquals(true, baseModel.isInvalid(1, 0));
    assertEquals(false, baseModel.isInvalid(2, 2));
    assertEquals(false, baseModel.isInvalid(3, 3));
    assertEquals(true, baseModel.isInvalid(4, 2));
    assertEquals(false, baseModel.isInvalid(2, 4));

    assertEquals(false, emptySlotAt22.isInvalid(0, 1));
    assertEquals(false, emptySlotAt22.isInvalid(2, 2));
    assertEquals(true, dimension7.isInvalid(9, 1));
    assertEquals(false, dimension7.isInvalid(3, 4));
    assertEquals(true, dim8Empty64.isInvalid(9, 8));
    assertEquals(false, dim8Empty64.isInvalid(3, 4));
  }

  @Test
  public void testIsValidMove() {
    init();
    assertEquals(false, baseModel.isValidMove(1, 3, 3, 3));
    assertEquals(true, baseModel.isValidMove(2, 0, 0, 0));
    assertEquals(false, baseModel.isValidMove(1, 2, 2, 2));
    assertEquals(false, baseModel.isValidMove(1, 3, 3, 4));
  }

  @Test
  public void testMove() {
    init();
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, baseModel.getSlotAt(0, 0));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, baseModel.getSlotAt(2, 2));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, baseModel.getSlotAt(1, 1));
    baseModel.move(2, 2, 0, 0);
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, baseModel.getSlotAt(0, 0));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, baseModel.getSlotAt(2, 2));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, baseModel.getSlotAt(1, 1));
  }


  @Test
  public void testToPositionNotEmpty() {
    init();
    try {
      baseModel.move(1, 3, 2, 3);
      fail("Tried to move marble at (3, 1) to (3, 2) when (3, 2) is not empty and " +
              "did not see an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
    try {
      dim8Empty64.move(5, 6, 3, 6);
      fail("Tried to move marble at (6, 5) to (6, 3) when (6, 3) is not empty and " +
              "did not see an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
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
    } catch (IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
    try {
      dim8Empty64.move(3, 4, 3, 6);
      fail("Tried to move marble at (4, 3) to (6, 3) with no marble at (4, 3) and " +
              "did not see an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
  }

  @Test
  public void testGetBoardSize() {
    init();
    assertEquals(5, baseModel.getBoardSize());
    assertEquals(7, dimension7.getBoardSize());
    assertEquals(11, new TriangleSolitaireModel(11).getBoardSize());
  }

  @Test
  public void testGetSlotAt() {
    init();
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid, baseModel.getSlotAt(0, 1));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, baseModel.getSlotAt(0, 0));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, baseModel.getSlotAt(1, 1));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, baseModel.getSlotAt(3, 3));
  }

  @Test
  public void testGetSlotAtOutOfBounds() {
    init();
    try {
      baseModel.getSlotAt(-1, 4);
      fail("Tried to get slot at (4, -1) when -1 is out of bounds and " +
              "did not see an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
    try {
      dimension7.getSlotAt(12, 9);
      fail("Tried to get slot at (9, 12) when 12 is out of bounds and " +
              "did not see an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
    try {
      dimension7.getSlotAt(0, 14);
      fail("Tried to get slot at (14, 0) when 14 is out of bounds and " +
              "did not see an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
  }

  @Test
  public void testGetScore() {
    init();
    assertEquals(14, baseModel.getScore());
    baseModel.move(2, 0, 0, 0);
    assertEquals(13, baseModel.getScore());
    baseModel.move(2, 2, 2, 0);
    baseModel.move(3, 0, 1, 0);
    baseModel.move(3, 2, 3, 0);
    assertEquals(10, baseModel.getScore());
    assertEquals(27, dimension7.getScore());
  }

  @Test
  public void testGameOver() {
    init();
    assertEquals(false, baseModel.isGameOver());
    baseModel.move(2, 0, 0, 0);
    baseModel.move(2, 2, 2, 0);
    baseModel.move(3, 0, 1, 0);
    baseModel.move(4, 4, 2, 2);
    baseModel.move(4, 2, 2, 0);
    assertEquals(false, baseModel.isGameOver());
    baseModel.move(4, 0, 4, 2);
    baseModel.move(1, 0, 3, 0);
    baseModel.move(4, 3, 2, 1);
    assertEquals(false, baseModel.isGameOver());
    baseModel.move(1, 1, 3, 1);
    baseModel.move(3, 0, 3, 2);
    assertEquals(true, baseModel.isGameOver());
  }
}
