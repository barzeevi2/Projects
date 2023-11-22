package cs3500.marblesolitaire.view;
import java.io.IOException;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModelState;

public class MarbleSolitaireTextView implements MarbleSolitaireView {
  MarbleSolitaireModelState modelState;
  Appendable out;

  /**
   * @param modelState model of desired text view
   */
  public MarbleSolitaireTextView(MarbleSolitaireModelState modelState) {
    this(modelState, System.out);
  }

  /**
   * @param modelState model of desired text view
   * @param out abstraction of desired destination
   */
  public MarbleSolitaireTextView(MarbleSolitaireModelState modelState, Appendable out) {
    if (modelState == null) {
      throw new IllegalArgumentException("Provided model is null");
    }
    this.modelState = modelState;
    if (out == null) {
      throw new IllegalArgumentException("Provided appendable is null");
    }
    this.out = out;
  }

  /**
   * @return String of what the board should look like
   */
  @Override
  public String toString() {
    String boardInText = "";
    for (int i = 0; i < modelState.getBoardSize(); i++) {
      for (int j = 0; j < modelState.getBoardSize(); j++) {
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

  /**
   * Render the board to the provided data destination. The board should be rendered exactly
   * in the format produced by the toString method above
   *
   * @throws IOException if transmission of the board to the provided data destination fails
   */
  @Override
  public void renderBoard() throws IOException {
    this.out.append(this.toString());
  }

  /**
   * Render a specific message to the provided data destination.
   *
   * @param message the message to be transmitted
   * @throws IOException if transmission of the board to the provided data destination fails
   */
  @Override
  public void renderMessage(String message) throws IOException {
    if (message.toString() == null) {
      throw new IOException("Message cannot be null");
    }
    this.out.append(message);
  }
}
