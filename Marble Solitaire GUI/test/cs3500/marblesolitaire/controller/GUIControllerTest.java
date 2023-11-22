package cs3500.marblesolitaire.controller;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cs3500.marblesolitaire.model.hw02.EnglishSolitaireModel;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.view.GuiViewMock;
import srcLab.cs3500.marblesolitaire.view.MarbleSolitaireGuiView;

import static org.junit.Assert.*;

public class GUIControllerTest {
  private MarbleSolitaireModel model;
  private MarbleSolitaireGuiView mockView;
  private GUIController controller;
  private final List<String> log = new ArrayList<>();


  @Before
  public void setUp() {
    model = new EnglishSolitaireModel();
    mockView = new GuiViewMock(log);
    controller = new GUIController(model, mockView);
  }

  @Test
  public void testNullParameters() {
    setUp();
    try {
      Features nullModel = new GUIController(null, mockView);
      fail("No exception thrown, when Illegal Argument was expected");
    } catch (IllegalArgumentException e) {
      // pass
      assertEquals("Model cannot be null", e.getMessage());
    }
    try {
      Features nullView = new GUIController(model, null);
      fail("No exception thrown, when Illegal Argument was expected");
    } catch (IllegalArgumentException e) {
      // pass
      assertEquals("View cannot be null", e.getMessage());
    }
  }

  @Test
  public void testInput() {
    setUp();
    controller.input(3, 1);
    controller.input(3, 3);

    List<String> expectedOutput = new ArrayList<>();
    expectedOutput.add(
            "refresh(), refresh(), refresh(), renderMessage(Successful move: Moved from\n" +
            "row: 3, column: 1, to\n" +
            "row: 3, column: 3\n" +
            ")");
    assertEquals(expectedOutput.toString(), log.toString());
  }
}

//"called setFeatures(), " +