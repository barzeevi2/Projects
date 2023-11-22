package cs3500.marblesolitaire.controller;

import java.io.IOException;
import java.util.Scanner;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.view.MarbleSolitaireTextView;
import cs3500.marblesolitaire.view.MarbleSolitaireView;

import static java.lang.Integer.parseInt;

/**
 * This class represents a marble solitaire controller implementation
 */
public class MarbleSolitaireControllerImpl implements MarbleSolitaireController {

  private MarbleSolitaireModel model;
  private MarbleSolitaireView view;
  private Scanner in;
  private boolean quits;

  /**
   * @param model specified marble solitaire model
   * @param view specified marble solitaire view
   * @param in readable input
   */
  public MarbleSolitaireControllerImpl(MarbleSolitaireModel model, MarbleSolitaireView view,
                                       Readable in) {
    if (model == null || view == null || in == null) {
      throw new IllegalArgumentException("The model, view, and input cannot be null");
    }
    this.model = model;
    this.view = new MarbleSolitaireTextView(model);
    this.in = new Scanner(in);
    this.quits = false;
  }

  /**
   * Simply ends the transmission with a new line
   *
   * @throws IOException if the message passed to renderMessage is null
   */
  private void newLine() throws IllegalStateException {
    try {
      view.renderMessage(System.lineSeparator());
    } catch (IOException e) {
    }
  }

  /**
   * Renders the board
   *
   * @throws IllegalStateException if board is unable to be rendered
   */
  private void renderBoardAndScore() throws IllegalStateException {
    try {
      view.renderBoard();
      newLine();
      view.renderMessage("Score: " + model.getScore()); // transmit score from view
    } catch (IOException e) {
      throw new IllegalStateException("Board unable to be rendered");
    }
  }

  /**
   * Renders the given message
   *
   * @param msg desired message to be rendered
   * @throws IllegalStateException if message is unable to be rendered
   */
  private void renderMessage(String msg) throws IllegalStateException {
    try {
      view.renderMessage(msg);
    } catch (IOException e) {
      throw new IllegalStateException("Message unable to be rendered");
    }
  }

  /**
   * Determines whether the input is valid
   *
   * @param input user input provided
   * @return true if the input is valid, false if not
   */
  private boolean isValidInput(String input) {
    if (input.equalsIgnoreCase("q")) {
      this.quits = true;
      return true;
    }
    if (parseInt(input) > 0 && parseInt(input) <= model.getBoardSize()) {
      return true;
    }
    this.renderMessage("Please re-input your desired position, as it was an invalid value");
    newLine();
    return false;
  }

  /**
   * Plays a new game of marble solitaire
   *
   * @throws IllegalStateException if the controller is unable to read input or transmit output
   */
  @Override
  public void playGame() throws IllegalStateException {
    int[] positions = new int[4];
    while (!model.isGameOver()) {
      this.renderBoardAndScore();
      newLine();
      this.renderMessage("Enter the row position you would like to move FROM, " +
              "with the first row being at position 1.\n");
      this.renderMessage("Waiting for user input...");
      newLine();
      String fromRow = in.next();
      while (!isValidInput(fromRow)) {
        fromRow = in.next();
      }
      if (quits) {
        break;
      }
      positions[0] = parseInt(fromRow) - 1;


      this.renderMessage("Enter the column position you would like to move FROM, " +
              "with the first column being at position 1.\n");
      this.renderMessage("Waiting for user input...");
      newLine();
      String fromCol = in.next();
      while (!isValidInput(fromCol)) {
        fromCol = in.next();
      }
      if (quits) {
        break;
      }
      positions[1] = parseInt(fromCol) - 1;


      this.renderMessage("Enter the row position you would like to move TO\n");
      this.renderMessage("Waiting for user input...");
      newLine();
      String toRow = in.next();
      while (!isValidInput(toRow)) {
        toRow = in.next();
      }
      if (quits) {
        break;
      }
      positions[2] = parseInt(toRow) - 1;

      this.renderMessage("Enter the column position you would like to move TO\n");
      this.renderMessage("Waiting for user input...");
      newLine();
      String toCol = in.next();
      while (!isValidInput(toCol)) {
        toCol = in.next();
      }
      if (quits) {
        break;
      }
      positions[3] = parseInt(toCol) - 1;

      try {
        model.move(positions[0], positions[1], positions[2], positions[3]);
      } catch (IllegalArgumentException e) {
        this.renderMessage("Invalid Move. Play Again. " + e.getMessage());
        newLine();
      }
    }

    if (quits) {
      try {
        view.renderMessage("Game Quit!\nState of game when quit:\n");
        view.renderBoard();
        view.renderMessage("\nScore: " + model.getScore());
      } catch (Exception e) {
        System.out.println("Error on game quit");
      }
    } else {
      try {
        view.renderMessage("Game Over\n");
        view.renderBoard();
        view.renderMessage("\nScore: " + model.getScore());
      } catch (Exception e) {
        System.out.println("Error on game over");
      }
    }

  }
}
