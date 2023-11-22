package cs3500.marblesolitaire.view;

import java.io.IOException;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModelState;
import cs3500.marblesolitaire.model.hw04.TriangleSolitaireModel;

public class TriangleSolitaireTextView extends MarbleSolitaireTextView implements MarbleSolitaireView {

  /**
   * @param modelState model of desired text view
   */
  public TriangleSolitaireTextView(MarbleSolitaireModelState modelState) {
    super(modelState);
  }

  /**
   * @return String of what the board should look like
   */
  @Override
  public String toString() {
    String boardInText = "";
    for (int i = 0; i < modelState.getBoardSize(); i++) {
      int spaces = modelState.getBoardSize() - (i + 1);
      for (int j = 0; j < modelState.getBoardSize(); j++) {
        while (spaces > 0) {
          boardInText += " ";
          spaces--;
        }
        if (modelState.getSlotAt(i, j).equals(MarbleSolitaireModelState.SlotState.Invalid)) {
          boardInText += " ";
        } else if (modelState.getSlotAt(i, j).equals(MarbleSolitaireModelState.SlotState.Empty)) {
          boardInText += "_";
        } else {
          boardInText += "0";
        }
        if (!(j == modelState.getBoardSize() - 1)) {
          boardInText += " ";
        }
      }
      if (!(i == modelState.getBoardSize() - 1)) {
        boardInText += "\n";
      }
    }
    return boardInText;
  }

  public static void main(String[] args) {
    MarbleSolitaireModel model = new TriangleSolitaireModel(6);
    MarbleSolitaireView e = new TriangleSolitaireTextView(model);
    System.out.println(e.toString());
    model.move(2, 0, 0, 0);
    System.out.println(e.toString());
  }
}
