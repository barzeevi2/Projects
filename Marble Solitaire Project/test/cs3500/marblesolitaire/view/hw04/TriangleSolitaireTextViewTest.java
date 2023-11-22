package cs3500.marblesolitaire.view.hw04;

import org.junit.Test;

import cs3500.marblesolitaire.model.hw02.EnglishSolitaireModel;
import cs3500.marblesolitaire.model.hw04.TriangleSolitaireModel;
import cs3500.marblesolitaire.view.TriangleSolitaireTextView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TriangleSolitaireTextViewTest {
  TriangleSolitaireModel baseModel;
  TriangleSolitaireModel model2;
  TriangleSolitaireModel model3;
  TriangleSolitaireModel model4;
  TriangleSolitaireTextView baseModelView;
  TriangleSolitaireTextView view2;
  TriangleSolitaireTextView view3;
  TriangleSolitaireTextView view4;

  String baseModelText;
  String view2Text;
  String view3Text;
  String view4Text;
  String baseModelAfterMove;

  public void init() {
    baseModel = new TriangleSolitaireModel();
    model2 = new TriangleSolitaireModel(2, 2);
    model3 = new TriangleSolitaireModel(7);
    model4 = new TriangleSolitaireModel(5, 4, 2);
    baseModelView = new TriangleSolitaireTextView(baseModel);
    view2 = new TriangleSolitaireTextView(model2);
    view3 = new TriangleSolitaireTextView(model3);
    view4 = new TriangleSolitaireTextView(model4);

    baseModelText = "    _        \n" +
            "   0 0      \n" +
            "  0 0 0    \n" +
            " 0 0 0 0  \n" +
            "0 0 0 0 0";

    view2Text = "    0        \n" +
            "   0 0      \n" +
            "  0 0 _    \n" +
            " 0 0 0 0  \n" +
            "0 0 0 0 0";

    view3Text = "      _            \n" +
            "     0 0          \n" +
            "    0 0 0        \n" +
            "   0 0 0 0      \n" +
            "  0 0 0 0 0    \n" +
            " 0 0 0 0 0 0  \n" +
            "0 0 0 0 0 0 0";

    view4Text = "    0        \n" +
            "   0 0      \n" +
            "  0 0 0    \n" +
            " 0 0 0 0  \n" +
            "0 0 _ 0 0";

    baseModelAfterMove = "    0        \n" +
            "   _ 0      \n" +
            "  0 _ _    \n" +
            " 0 0 0 0  \n" +
            "0 0 0 0 0";
  }

  @Test
  public void testNullModelState() {
    try {
      TriangleSolitaireTextView nullCase = new TriangleSolitaireTextView(null);
      fail("Tried to create a TriangleSolitaireTextView with a null modelState and " +
              "did not see an IllegalArgumentException");
    } catch(IllegalArgumentException e) {
      // pass
      System.out.println("Correct exception thrown: " + e);
    }
  }

  @Test
  public void testToString() {
    init();
    assertEquals(baseModelText, baseModelView.toString());
    assertEquals(view2Text, view2.toString());
    assertEquals(view3Text, view3.toString());
    assertEquals(view4Text, view4.toString());
    baseModel.move(2, 0, 0, 0);
    baseModel.move(2, 2, 2, 0);
    assertEquals(baseModelAfterMove, baseModelView.toString());
    baseModel.move(0, 0, 2, 2);
    System.out.println(baseModelView.toString());
  }
}