package cs3500.marblesolitaire.controller;

import java.io.IOException;
import java.io.InputStreamReader;

import cs3500.marblesolitaire.model.hw02.EnglishSolitaireModel;
import cs3500.marblesolitaire.view.MarbleSolitaireTextView;

/**
 * Main class to run the GUI version of the game
 */

public class Driver {
  public static void main(String[] args) throws IOException {
    EnglishSolitaireModel model = new EnglishSolitaireModel();
    MarbleSolitaireControllerImpl controller = new MarbleSolitaireControllerImpl(model,
            new MarbleSolitaireTextView(model),
            new InputStreamReader(System.in));
    controller.playGame();
  }
}
