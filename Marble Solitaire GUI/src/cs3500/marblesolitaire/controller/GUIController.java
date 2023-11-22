package cs3500.marblesolitaire.controller;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import srcLab.cs3500.marblesolitaire.view.MarbleSolitaireGuiView;

/**
 * this class represents a gui controller
 */
public class GUIController implements Features{

  MarbleSolitaireModel model;
  MarbleSolitaireGuiView view;

  private int fromRow, fromCol, toRow, toCol;

  /**
   * controller constructor
   * @param model the given marble solitaire model state
   * @param view the given marble solitaire gui view
   */
  public GUIController(MarbleSolitaireModel model, MarbleSolitaireGuiView view) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    this.model = model;
    if (view == null) {
      throw new IllegalArgumentException("View cannot be null");
    }
    this.view = view;
    this.view.setFeatures(this);
    this.view.refresh();
    fromRow = -1;
    fromCol = -1;
    toRow = -1;
    toCol = -1;
  }

  /**
   * handles the input from the user to make a move
   * @param row entered row
   * @param col entered column
   */
  @Override
  public void input(int row, int col) {
    if (!this.model.isGameOver()) {
      if (row >= 0 && col >= 0) {
        if (fromRow == -1) {
          fromRow = row;
          fromCol = col;
        } else {
          toRow = row;
          toCol = col;
          try {
            model.move(fromRow, fromCol, toRow, toCol);
            view.refresh();
            view.renderMessage("Successful move: Moved from\nrow: " + fromRow + ", column: " + fromCol +
                    ", to\nrow: " + toRow + ", column: " + toCol + System.lineSeparator());
            fromRow = fromCol = toRow = toCol = -1;
          } catch (IllegalArgumentException e) {
            view.renderMessage("Move unsuccessful:\n" + e.getMessage() + System.lineSeparator());
            fromRow = fromCol = toRow = toCol = -1;
          }
        }
      }
    }
    else {
      this.view.renderMessage("Game Over!");
    }
  }
}
