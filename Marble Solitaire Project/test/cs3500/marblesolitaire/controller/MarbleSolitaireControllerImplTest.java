package cs3500.marblesolitaire.controller;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import cs3500.marblesolitaire.model.hw02.EnglishSolitaireModel;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireMock;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.model.hw04.EuropeanSolitaireModel;
import cs3500.marblesolitaire.model.hw04.TriangleSolitaireModel;
import cs3500.marblesolitaire.view.MarbleSolitaireTextView;
import cs3500.marblesolitaire.view.MarbleSolitaireView;
import cs3500.marblesolitaire.view.TriangleSolitaireTextView;

import static org.junit.Assert.*;

public class MarbleSolitaireControllerImplTest {

//  public static void main(String[] args) throws IOException {
//    MarbleSolitaireModel model = new TriangleSolitaireModel();
//    MarbleSolitaireControllerImpl controller = new MarbleSolitaireControllerImpl(model,
//            new TriangleSolitaireTextView(model),
//            new InputStreamReader(System.in));
//    controller.playGame();
//  }
  private MarbleSolitaireModel model;
  private MarbleSolitaireView view;
  private Readable in;
  private Appendable out;
  private MarbleSolitaireController controller;

  @Before
  public void setUp() {
    model = new EnglishSolitaireModel();
    out = new StringBuilder();
    view = new MarbleSolitaireTextView(model, out);
  }

  @Test
  public void testNullConstructor() {
    try {
      MarbleSolitaireControllerImpl controller =
              new MarbleSolitaireControllerImpl(null, view, null);
      fail("Tried to create a controller with a null model and did not see an exception");
    } catch (IllegalArgumentException e) {
      // pass
      assertEquals("The model, view, and input cannot be null", e.getMessage());
    }
    try {
      MarbleSolitaireControllerImpl controller =
              new MarbleSolitaireControllerImpl(model, null, null);
      fail("Tried to create a controller with a null view and did not see an exception");
    } catch (IllegalArgumentException e) {
      // pass
      assertEquals("The model, view, and input cannot be null", e.getMessage());
    }
    try {
      MarbleSolitaireControllerImpl controller =
              new MarbleSolitaireControllerImpl(model, view, null);
      fail("Tried to create a controller with a null readable and did not see an exception");
    } catch (IllegalArgumentException e) {
      // pass
      assertEquals("The model, view, and input cannot be null", e.getMessage());
    }
  }

  @Test
  public void testInvalidInput() {
    in = new StringReader("-3");
    controller = new MarbleSolitaireControllerImpl(model, view, in);
    try {
      controller.playGame();
      assertEquals("    0 0 0    \n" +
              "    0 0 0    \n" +
              "0 0 0 0 0 0 0\n" +
              "0 0 0 _ 0 0 0\n" +
              "0 0 0 0 0 0 0\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "Score: 32\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Please re-input your desired position, as it was an invalid value", out.toString());
    } catch (Exception e) {
    }
    in = new StringReader("20");
    controller = new MarbleSolitaireControllerImpl(model, view, in);
    try {
      controller.playGame();
      assertEquals("    0 0 0    \n" +
              "    0 0 0    \n" +
              "0 0 0 0 0 0 0\n" +
              "0 0 0 _ 0 0 0\n" +
              "0 0 0 0 0 0 0\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "Score: 32\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Please re-input your desired position, as it was an invalid value", out.toString());
    } catch (Exception e) {
    }
  }

  @Test
  public void testValidMove() {
    in = new StringReader("4 2 4 4");
    controller = new MarbleSolitaireControllerImpl(model, view, in);
    try {
      controller.playGame();
      assertEquals("    0 0 0    \n" +
              "    0 0 0    \n" +
              "0 0 0 0 0 0 0\n" +
              "0 0 0 _ 0 0 0\n" +
              "0 0 0 0 0 0 0\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "Score: 32\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Marble moved from (1, 3) to (3, 3)\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "0 0 0 0 0 0 0\n" +
              "0 _ _ 0 0 0 0\n" +
              "0 0 0 0 0 0 0\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "Score: 31\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n", out.toString());
    } catch (Exception e) {
    }
  }

  @Test
  public void testInvalidMove() {
    in = new StringReader("4 2 5 3");
    controller = new MarbleSolitaireControllerImpl(model, view, in);
    try {
      controller.playGame();
      assertEquals("    0 0 0    \n" +
              "    0 0 0    \n" +
              "0 0 0 0 0 0 0\n" +
              "0 0 0 _ 0 0 0\n" +
              "0 0 0 0 0 0 0\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "Score: 32\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Invalid Move. Play Again. The to position must be empty\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "0 0 0 0 0 0 0\n" +
              "0 0 0 _ 0 0 0\n" +
              "0 0 0 0 0 0 0\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "Score: 32\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n", out.toString());
    } catch (Exception e) {
    }
  }

  @Test
  public void testControllerInputs() throws IOException {
    List<String> log = new ArrayList<>();
    MarbleSolitaireModel mockSolitaire = new MarbleSolitaireMock(log);

    //testing the methods to move marbles (controller passes and is passed correct values)
    Readable readable = new StringReader("4\n 2\n 4\n 4\n 4\nq");
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl
            (mockSolitaire,
                    new MarbleSolitaireTextView(mockSolitaire,
                            new StringBuilder()), readable);

    List<String> expectedLog = null;
    try {
      controller.playGame();
      expectedLog = new ArrayList<>();
      expectedLog.add("move(4, 2, 4, 4)");
    } catch (Exception e) {
      assertEquals(expectedLog, log);
    }

  }


  @Test
  public void testPlayGameQuit() {
    in = new StringReader("Q");
    controller = new MarbleSolitaireControllerImpl(model, view, in);
    try {
      controller.playGame();
      assertEquals("", out.toString());
    } catch (Exception e) {
    }
  }

  @Test
  public void testPlayGameOver() throws IllegalStateException {
    in = new StringReader("4 2 4 4 4 5 4 3 4 7 3 5 4 7 4 5 6 4 4 4 5 2 5 4 5 5 5 3 5 7 5 5 " +
            "4 4 4 6 3 6 5 6 5 5 5 7 2 5 4 5 3 3 3 5 3 5 2 5 3 5 2 5 4 5 2 5 1 5 3 5 1 3 3 3 " +
            "1 4 3 4 3 4 5 4 3 4 5 4 3 4 3 6 3 7 2 5 3 7 3 5 3 2 3 5 3 2 3 4 5 3 3 3 3 3 " +
            "3 5 1 5 7 3 7 5 7 3 5 3 7 5 5 5");
    controller = new MarbleSolitaireControllerImpl(model, view, in);
    try {
      controller.playGame();
      assertEquals("    0 0 0    \n" +
              "    0 0 0    \n" +
              "0 0 0 0 0 0 0\n" +
              "0 0 0 _ 0 0 0\n" +
              "0 0 0 0 0 0 0\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "Score: 32\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Marble moved from (1, 3) to (3, 3)\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "0 0 0 0 0 0 0\n" +
              "0 _ _ 0 0 0 0\n" +
              "0 0 0 0 0 0 0\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "Score: 31\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Marble moved from (4, 3) to (2, 3)\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "0 0 0 0 0 0 0\n" +
              "0 _ 0 _ _ 0 0\n" +
              "0 0 0 0 0 0 0\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "Score: 30\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Invalid Move. Play Again. The to position must be empty\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "0 0 0 0 0 0 0\n" +
              "0 _ 0 _ _ 0 0\n" +
              "0 0 0 0 0 0 0\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "Score: 30\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Marble moved from (6, 3) to (4, 3)\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "0 0 0 0 0 0 0\n" +
              "0 _ 0 _ 0 _ _\n" +
              "0 0 0 0 0 0 0\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "Score: 29\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Marble moved from (3, 5) to (3, 3)\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "0 0 0 0 0 0 0\n" +
              "0 _ 0 0 0 _ _\n" +
              "0 0 0 _ 0 0 0\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 28\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Marble moved from (1, 4) to (3, 4)\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "0 0 0 0 0 0 0\n" +
              "0 _ 0 0 0 _ _\n" +
              "0 _ _ 0 0 0 0\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 27\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Marble moved from (4, 4) to (2, 4)\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "0 0 0 0 0 0 0\n" +
              "0 _ 0 0 0 _ _\n" +
              "0 _ 0 _ _ 0 0\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 26\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Marble moved from (6, 4) to (4, 4)\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "0 0 0 0 0 0 0\n" +
              "0 _ 0 0 0 _ _\n" +
              "0 _ 0 _ 0 _ _\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 25\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Marble moved from (3, 3) to (5, 3)\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "0 0 0 0 0 0 0\n" +
              "0 _ 0 _ _ 0 _\n" +
              "0 _ 0 _ 0 _ _\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 24\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Marble moved from (5, 2) to (5, 4)\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "0 0 0 0 0 _ 0\n" +
              "0 _ 0 _ _ _ _\n" +
              "0 _ 0 _ 0 0 _\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 23\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Marble moved from (4, 4) to (6, 4)\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "0 0 0 0 0 _ 0\n" +
              "0 _ 0 _ _ _ _\n" +
              "0 _ 0 _ _ _ 0\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 22\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Marble moved from (4, 1) to (4, 3)\n" +
              "    0 0 0    \n" +
              "    0 0 _    \n" +
              "0 0 0 0 _ _ 0\n" +
              "0 _ 0 _ 0 _ _\n" +
              "0 _ 0 _ _ _ 0\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 21\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Marble moved from (2, 2) to (4, 2)\n" +
              "    0 0 0    \n" +
              "    0 0 _    \n" +
              "0 0 _ _ 0 _ 0\n" +
              "0 _ 0 _ 0 _ _\n" +
              "0 _ 0 _ _ _ 0\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 20\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Invalid Move. Play Again. The to and from positions must be exactly 2 positions away from each other\n" +
              "    0 0 0    \n" +
              "    0 0 _    \n" +
              "0 0 _ _ 0 _ 0\n" +
              "0 _ 0 _ 0 _ _\n" +
              "0 _ 0 _ _ _ 0\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 20\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Invalid Move. Play Again. The to and from positions must be exactly 2 positions away from each other\n" +
              "    0 0 0    \n" +
              "    0 0 _    \n" +
              "0 0 _ _ 0 _ 0\n" +
              "0 _ 0 _ 0 _ _\n" +
              "0 _ 0 _ _ _ 0\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 20\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Marble moved from (4, 3) to (4, 1)\n" +
              "    0 0 0    \n" +
              "    0 0 0    \n" +
              "0 0 _ _ _ _ 0\n" +
              "0 _ 0 _ _ _ _\n" +
              "0 _ 0 _ _ _ 0\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 19\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Marble moved from (4, 0) to (4, 2)\n" +
              "    0 0 _    \n" +
              "    0 0 _    \n" +
              "0 0 _ _ 0 _ 0\n" +
              "0 _ 0 _ _ _ _\n" +
              "0 _ 0 _ _ _ 0\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 18\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Marble moved from (2, 0) to (2, 2)\n" +
              "    _ 0 _    \n" +
              "    _ 0 _    \n" +
              "0 0 0 _ 0 _ 0\n" +
              "0 _ 0 _ _ _ _\n" +
              "0 _ 0 _ _ _ 0\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 17\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Marble moved from (3, 0) to (3, 2)\n" +
              "    _ _ _    \n" +
              "    _ _ _    \n" +
              "0 0 0 0 0 _ 0\n" +
              "0 _ 0 _ _ _ _\n" +
              "0 _ 0 _ _ _ 0\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 16\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Invalid Move. Play Again. Must jump over a space with a marble\n" +
              "    _ _ _    \n" +
              "    _ _ _    \n" +
              "0 0 0 0 0 _ 0\n" +
              "0 _ 0 _ _ _ _\n" +
              "0 _ 0 _ _ _ 0\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 16\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Invalid Move. Play Again. Must jump over a space with a marble\n" +
              "    _ _ _    \n" +
              "    _ _ _    \n" +
              "0 0 0 0 0 _ 0\n" +
              "0 _ 0 _ _ _ _\n" +
              "0 _ 0 _ _ _ 0\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 16\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Marble moved from (3, 2) to (5, 2)\n" +
              "    _ _ _    \n" +
              "    _ _ _    \n" +
              "0 0 0 _ _ 0 0\n" +
              "0 _ 0 _ _ _ _\n" +
              "0 _ 0 _ _ _ 0\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 15\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Marble moved from (6, 2) to (4, 1)\n" +
              "    _ _ _    \n" +
              "    _ _ 0    \n" +
              "0 0 0 _ _ _ _\n" +
              "0 _ 0 _ _ _ _\n" +
              "0 _ 0 _ _ _ 0\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 14\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Invalid Move. Play Again. The from position must have a marble in its slot\n" +
              "    _ _ _    \n" +
              "    _ _ 0    \n" +
              "0 0 0 _ _ _ _\n" +
              "0 _ 0 _ _ _ _\n" +
              "0 _ 0 _ _ _ 0\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 14\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Invalid Move. Play Again. The to and from positions must be exactly 2 positions away from each other\n" +
              "    _ _ _    \n" +
              "    _ _ 0    \n" +
              "0 0 0 _ _ _ _\n" +
              "0 _ 0 _ _ _ _\n" +
              "0 _ 0 _ _ _ 0\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 14\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Marble moved from (1, 2) to (3, 2)\n" +
              "    _ _ _    \n" +
              "    _ _ 0    \n" +
              "0 _ _ 0 _ _ _\n" +
              "0 _ 0 _ _ _ _\n" +
              "0 _ 0 _ _ _ 0\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 13\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Marble moved from (2, 4) to (2, 2)\n" +
              "    _ _ _    \n" +
              "    _ _ 0    \n" +
              "0 _ 0 0 _ _ _\n" +
              "0 _ _ _ _ _ _\n" +
              "0 _ _ _ _ _ 0\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 12\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Marble moved from (2, 2) to (4, 2)\n" +
              "    _ _ _    \n" +
              "    _ _ 0    \n" +
              "0 _ _ _ 0 _ _\n" +
              "0 _ _ _ _ _ _\n" +
              "0 _ _ _ _ _ 0\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 11\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Invalid Move. Play Again. The from position must have a marble in its slot\n" +
              "    _ _ _    \n" +
              "    _ _ 0    \n" +
              "0 _ _ _ 0 _ _\n" +
              "0 _ _ _ _ _ _\n" +
              "0 _ _ _ _ _ 0\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 11\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Invalid Move. Play Again. The to position must be empty\n" +
              "    _ _ _    \n" +
              "    _ _ 0    \n" +
              "0 _ _ _ 0 _ _\n" +
              "0 _ _ _ _ _ _\n" +
              "0 _ _ _ _ _ 0\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 11\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move TO\n" +
              "Waiting for user input...\n" +
              "Invalid Move. Play Again. The from position must have a marble in its slot\n" +
              "    _ _ _    \n" +
              "    _ _ 0    \n" +
              "0 _ _ _ 0 _ _\n" +
              "0 _ _ _ _ _ _\n" +
              "0 _ _ _ _ _ 0\n" +
              "    0 _ 0    \n" +
              "    0 0 0    \n" +
              "Score: 11\n" +
              "\n" +
              "Enter the row position you would like to move FROM, with the first row being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the column position you would like to move FROM, with the first column being at position 1.\n" +
              "Waiting for user input...\n" +
              "Enter the row position you would like to move TO\n" +
              "Waiting for user input...\n", out.toString());
    } catch (Exception e) {
    }
  }


}