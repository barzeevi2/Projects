package srcLab.cs3500.marblesolitaire.view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import cs3500.marblesolitaire.controller.Features;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModelState;

/**
 * this class represents a custom panel for the game board
 */
public class BoardPanel extends JPanel implements IBoardPanel{
  private MarbleSolitaireModelState modelState;
  private Image emptySlot, marbleSlot, blankSlot;
  private final int cellDimension;
  private int originX,originY;

  private Features features;

  /**
   *
   * @param state the given marble solitaire game state
   * @throws IllegalStateException if no images exist
   */
  public BoardPanel(MarbleSolitaireModelState state) throws IllegalStateException {
    super();
    this.modelState = state;
    this.setBackground(Color.WHITE);
    this.cellDimension = 50;
    try {
      emptySlot = ImageIO.read(new FileInputStream("res/empty.png"));
      emptySlot = emptySlot.getScaledInstance(cellDimension, cellDimension, Image.SCALE_DEFAULT);

      marbleSlot = ImageIO.read(new FileInputStream("res/marble.png"));
      marbleSlot = marbleSlot.getScaledInstance(cellDimension, cellDimension, Image.SCALE_DEFAULT);

      blankSlot = ImageIO.read(new FileInputStream("res/blank.png"));
      blankSlot = blankSlot.getScaledInstance(cellDimension, cellDimension, Image.SCALE_DEFAULT);

      this.setPreferredSize(
              new Dimension((this.modelState.getBoardSize() + 4) * cellDimension
                      , (this.modelState.getBoardSize() + 4) * cellDimension));
    } catch (IOException e) {
      throw new IllegalStateException("Icons not found!");
    }

  }

  /**
   * paints the board
   * @param g the <code>Graphics</code> object to protect
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    
    originX = (int) (this.getPreferredSize().getWidth() / 2 - this.modelState.getBoardSize() * cellDimension / 2);
    originY = (int) (this.getPreferredSize().getHeight() / 2 - this.modelState.getBoardSize() * cellDimension / 2);


    for (int i = 0; i < this.modelState.getBoardSize(); i ++) {
      for (int j = 0; j < this.modelState.getBoardSize(); j ++) {
        switch (this.modelState.getSlotAt(i, j)) {
          case Invalid:
            g.drawImage(blankSlot, originX + j * cellDimension, originY + i * cellDimension, null);
            break;
          case Marble:
            g.drawImage(marbleSlot, originX + j * cellDimension, originY + i * cellDimension, null);
            break;
          case Empty:
            g.drawImage(emptySlot, originX + j * cellDimension, originY + i * cellDimension, null);
            break;
        }
      }
    }
    //your code to the draw the board should go here. 
    //The originX and originY is the top-left of where the cell (0,0) should start
    //cellDimension is the width (and height) occupied by every cell
    
  }


  /**
   * sets the features controller to the given one and adds a mouse listener to it
   * @param features the given features gui controller
   */
  @Override
  public void setFeatures(Features features) {
    this.features = features;
    this.addMouseListener(new BoardMouseListener());
  }

  /**
   * class represents a mouse listener for the game board
   */
  private class BoardMouseListener extends MouseAdapter {

    /**
     * displays the proper move on mouse click
     *
     * @param e the event to be processed
     */
    public void mousePressed(MouseEvent e) {
      // find out row and column
      int row = (e.getY() - originY) / BoardPanel.this.cellDimension;
      int col = (e.getX() - originY) / BoardPanel.this.cellDimension;
      BoardPanel.this.features.input(row, col);
    }
  }
}
