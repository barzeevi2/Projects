package cs3500.marblesolitaire.view.hw04;
import org.junit.Test;

import cs3500.marblesolitaire.model.hw04.EuropeanSolitaireModel;
import cs3500.marblesolitaire.view.MarbleSolitaireTextView;

import static org.junit.Assert.*;

public class EuropeanSolitaireTextViewTest {
  EuropeanSolitaireModel baseModel;
  EuropeanSolitaireModel model2;
  EuropeanSolitaireModel model3;
  EuropeanSolitaireModel model4;
  MarbleSolitaireTextView baseModelView;
  MarbleSolitaireTextView view2;
  MarbleSolitaireTextView view3;
  MarbleSolitaireTextView view4;

  String baseModelText;
  String view2Text;
  String view3Text;
  String view4Text;
  String baseModelAfterMove;

  public void init() {
    baseModel = new EuropeanSolitaireModel();
    model2 = new EuropeanSolitaireModel(2, 2);
    model3 = new EuropeanSolitaireModel(5);
    model4 = new EuropeanSolitaireModel(5, 3, 4);
    baseModelView = new MarbleSolitaireTextView(baseModel);
    view2 = new MarbleSolitaireTextView(model2);
    view3 = new MarbleSolitaireTextView(model3);
    view4 = new MarbleSolitaireTextView(model4);

    baseModelText = "    0 0 0    \n" +
            "  0 0 0 0 0  \n" +
            "0 0 0 0 0 0 0\n" +
            "0 0 0 _ 0 0 0\n" +
            "0 0 0 0 0 0 0\n" +
            "  0 0 0 0 0  \n" +
            "    0 0 0    ";

    view2Text = "    0 0 0    \n" +
            "  0 0 0 0 0  \n" +
            "0 0 _ 0 0 0 0\n" +
            "0 0 0 0 0 0 0\n" +
            "0 0 0 0 0 0 0\n" +
            "  0 0 0 0 0  \n" +
            "    0 0 0    ";

    view3Text = "      0 0 0 0 0      \n" +
            "    0 0 0 0 0 0 0    \n" +
            "  0 0 0 0 0 0 0 0 0  \n" +
            "0 0 0 0 0 0 0 0 0 0 0\n" +
            "0 0 0 0 0 0 0 0 0 0 0\n" +
            "0 0 0 0 0 _ 0 0 0 0 0\n" +
            "0 0 0 0 0 0 0 0 0 0 0\n" +
            "0 0 0 0 0 0 0 0 0 0 0\n" +
            "  0 0 0 0 0 0 0 0 0  \n" +
            "    0 0 0 0 0 0 0    \n" +
            "      0 0 0 0 0      ";

    view4Text = "      0 0 0 0 0      \n" +
            "    0 0 0 0 0 0 0    \n" +
            "  0 0 0 0 0 0 0 0 0  \n" +
            "0 0 0 0 _ 0 0 0 0 0 0\n" +
            "0 0 0 0 0 0 0 0 0 0 0\n" +
            "0 0 0 0 0 0 0 0 0 0 0\n" +
            "0 0 0 0 0 0 0 0 0 0 0\n" +
            "0 0 0 0 0 0 0 0 0 0 0\n" +
            "  0 0 0 0 0 0 0 0 0  \n" +
            "    0 0 0 0 0 0 0    \n" +
            "      0 0 0 0 0      ";

    baseModelAfterMove = "    0 0 0    \n" +
            "  0 0 _ 0 0  \n" +
            "0 _ _ 0 0 0 0\n" +
            "0 0 0 0 0 0 0\n" +
            "0 0 0 0 0 0 0\n" +
            "  0 0 0 0 0  \n" +
            "    0 0 0    ";
  }

  @Test
  public void testToString() {
    init();
    assertEquals(baseModelText, baseModelView.toString());
    assertEquals(view2Text, view2.toString());
    assertEquals(view3Text, view3.toString());
    assertEquals(view4Text, view4.toString());
    baseModel.move(1, 3, 3, 3);
    baseModel.move(2, 1, 2, 3);
    assertEquals(baseModelAfterMove, baseModelView.toString());
  }
}