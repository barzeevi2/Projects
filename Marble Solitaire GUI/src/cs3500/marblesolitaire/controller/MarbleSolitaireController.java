package cs3500.marblesolitaire.controller;

import java.io.IOException;

/**
 * This interface represents a controller for this game
 */
public interface MarbleSolitaireController {

  /**
   * Plays a new game of marble solitaire
   *
   * @throws IllegalStateException if the controller is unable to successfully
   *                               read input or transmit output
   */
  void playGame() throws IllegalStateException, IOException;
}
