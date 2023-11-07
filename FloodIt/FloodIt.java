import java.util.ArrayList;
import tester.Tester;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;
import java.util.Random;

// represents an abstract cell class, either an actual cell, or a border cell
abstract class ACell {

  // the four adjacent cells to this one
  ACell left;
  ACell top;
  ACell right;
  ACell bottom;

  // changes a cell to flooded
  abstract void flood();

  // returns a world image that represents a cell
  abstract WorldImage drawCell();

  // checks if two cells are the same color
  abstract boolean sameColor(ACell other);

  // checks if this color is equal to the other color
  abstract boolean sameColorHelp(Cell other);

  // floods and re-colors the cells needed after a click happens
  abstract void flooder(ACell clickedCell, ACell firstCell, ArrayList<ACell> visited,
      Color originalColor);

  // checks if this is a cell
  abstract boolean isCell();

  // checks if current state is flooded
  abstract boolean isFlooded();

  // gets this color
  abstract Color getColor();

}

//Represents a single square of the game area
class Cell extends ACell {
  // In logical coordinates, with the origin at the top-left corner of the screen
  int x;
  int y;
  Color color;
  boolean flooded;

  Cell(int x, int y, Color color, boolean flooded, ACell left, ACell top, ACell right,
      ACell bottom) {

    this.x = x;
    this.y = y;
    this.color = color;
    this.flooded = flooded;
    this.left = left;
    this.top = top;
    this.right = right;
    this.bottom = bottom;

    // mutating the nearby cells' to refer back to this new cell
    this.left.right = this;
    this.right.left = this;
    this.bottom.top = this;
    this.top.bottom = this;
  }

  // changes a cell to flooded
  public void flood() {
    this.flooded = true;
  }

  // returns a world image that represents a cell
  public WorldImage drawCell() {
    return new RectangleImage(20, 20, OutlineMode.SOLID, this.color);
  }

  // checks if two cells are the same color
  public boolean sameColor(ACell other) {
    return other.sameColorHelp(this);
  }

  // checks if this color is equal to the other color
  public boolean sameColorHelp(Cell other) {
    return (other.color == this.color);
  }

  // takes in the clicked cell, the first cell, an empty list of visited cells,
  // , and the original color before flood
  // floods all of the cells that need to be flooded and changes their color
  public void flooder(ACell clickedCell, ACell firstCell, ArrayList<ACell> visited,
      Color originalColor) {

    // floods the current cell and changes its color
    this.flooded = true;
    this.color = clickedCell.getColor();

    // checks each direction- if not visited yet AND same color as original color
    // recurses on the direction
    if (this.left.getColor().equals(originalColor) && !visited.contains(this.left)) {
      visited.add(this.left);
      this.left.flooder(clickedCell, firstCell, visited, originalColor);
    }

    if (this.right.getColor().equals(originalColor) && !visited.contains(this.right)) {
      visited.add(this.right);
      this.right.flooder(clickedCell, firstCell, visited, originalColor);
    }

    if (this.top.getColor().equals(originalColor) && !visited.contains(this.top)) {
      visited.add(this.top);
      this.top.flooder(clickedCell, firstCell, visited, originalColor);
    }

    if (this.bottom.getColor().equals(originalColor) && !visited.contains(this.bottom)) {
      visited.add(this.bottom);
      this.bottom.flooder(clickedCell, firstCell, visited, originalColor);
    }
  }

  // checks if this is a cell
  public boolean isCell() {
    return true;
  }

  // checks if current state is flooded
  public boolean isFlooded() {
    return this.flooded;
  }

  // gets this color
  public Color getColor() {
    return this.color;
  }
}

//represents a border cell, a cell that is not interactive
//and is at the edges of the board
class BorderCell extends ACell {

  BorderCell() {
    this.left = this;
    this.top = this;
    this.right = this;
    this.bottom = this;
  }

  // changes a cell to flooded, does nothing in this class
  public void flood() {
    // border cells do not have a flooded boolean, and can't be flooded
  }

  // returns a world image that represents a cell, just a tiny square in this
  // class
  public WorldImage drawCell() {
    return new RectangleImage(20, 20, OutlineMode.SOLID, Color.white);
  }

  // checks if two cells are the same color
  public boolean sameColor(ACell other) {
    return false; // has no color
  }

  // checks if this color is equal to the other color
  public boolean sameColorHelp(Cell other) {
    return false; // has no color
  }

  public void flooder(ACell clickedCell, ACell firstCell, ArrayList<ACell> visited,
      Color originalColor) {
    // does nothing- can't flood a border cell
  }

  // checks if this is a cell
  public boolean isCell() {
    return false;
  }

  // gets this color
  public Color getColor() {
    return Color.white; // returns irrelevant color
  }

  // checks if current state is flooded
  public boolean isFlooded() {
    return false; // can't be flooded
  }
}

// represents our game state
class FloodItWorld extends World {
  // All the cells of the game
  ArrayList<ArrayList<ACell>> board;
  boolean flooding = false;
  ACell firstCell;
  ACell clickedCell;
  int stepsAllowed;
  int stepsUsed;
  int size;
  int colors;
  int timer;

  //our original pure random constructor used for real games
  FloodItWorld(int size, int colors) {
    // setting a limit for size / color amount
    if (colors > 8 || colors < 3) {
      throw new IllegalArgumentException("Invalid colors amount!");
    }

    else if (size < 2 || size > 26) {
      throw new IllegalArgumentException("Invalid size!");
    }

    else { // calls initBoard to make the board using size and colors amount
      this.board = initBoard(size, colors);

      this.firstCell = this.board.get(1).get(1);
      this.clickedCell = this.board.get(0).get(0);
      this.stepsAllowed = size * 2 - 3;
      this.stepsUsed = 0;
      this.size = size;
      this.colors = colors;
      timer = 0;
    }
  }

  // constructor with an int seed for a seeded random for testing
  FloodItWorld(int size, int colors, int seed) {
    // setting a limit for size / color amount
    if (colors > 8 || colors < 3) {
      throw new IllegalArgumentException("Invalid colors amount!");
    } else if (size < 2 || size > 26) {
      throw new IllegalArgumentException("Invalid size!");
    }

    else { // calls initBoard to make the board using size and colors amount
      this.board = initBoard(size, colors, seed);
      this.firstCell = this.board.get(1).get(1);
      this.clickedCell = this.board.get(0).get(0);
      this.stepsAllowed = size * 2 - 3;
      this.stepsUsed = 0;
      this.size = size;
      this.colors = colors;
      timer = 0;
    }
  }

  //our original board generator that produces a board for a real game
  public ArrayList<ArrayList<ACell>> initBoard(int size, int colors) {

    // our skeleton board that will eventually become the board
    ArrayList<ArrayList<ACell>> boardSkeleton = new ArrayList<ArrayList<ACell>>();
    // fills a board of size + 2 with border cells, surrounding our eventual
    // actual cells with board cells
    fillWithBorderCells(boardSkeleton, size + 2);
    // making a color list of different colors based on the amount chosen
    ArrayList<Color> colorList = this.colorList(colors);
    Random r = new Random();

    for (int i = 1; i < size; i++) { // fills the board's none border cells with actual cells

      for (int j = 1; j < size; j++) {
        int colorNumber = r.nextInt(colors); // chooses a random color
        ACell incCell = new Cell(i - 1, j - 1, colorList.get(colorNumber), false,
            boardSkeleton.get(i).get(j - 1), boardSkeleton.get(i - 1).get(j),
            boardSkeleton.get(i).get(j + 1), boardSkeleton.get(i + 1).get(j));

        // adding the inc cell to its place instead of border cell
        boardSkeleton.get(i).add(j, incCell);
        boardSkeleton.get(i).remove(j + 1); // removing the border cell that got pushed
      }
    }
    boardSkeleton.get(1).get(1).flood(); // floods first cell
    return boardSkeleton;
  }

  // generates a "fake" random board, USED ONLY FOR TESTING!!!
  // this is used in our testing constructor that uses a seeded random generator
  // only for testing
  public ArrayList<ArrayList<ACell>> initBoard(int size, int colors, int seed) {

    // our skeleton board that will eventually become the board
    ArrayList<ArrayList<ACell>> boardSkeleton = new ArrayList<ArrayList<ACell>>();

    // fills a board of size + 2 with border cells, surrounding our eventual
    // actual cells with board cells
    fillWithBorderCells(boardSkeleton, size + 2);

    // making a color list of different colors based on the amount chosen
    ArrayList<Color> colorList = this.colorList(colors);
    Random r = new Random(seed);

    for (int i = 1; i < size; i++) { // fills the board's none border cells with actual cells

      for (int j = 1; j < size; j++) {
        int colorNumber = r.nextInt(colors); // chooses a random color
        ACell incCell = new Cell(i - 1, j - 1, colorList.get(colorNumber), false,
            boardSkeleton.get(i).get(j - 1), boardSkeleton.get(i - 1).get(j),
            boardSkeleton.get(i).get(j + 1), boardSkeleton.get(i + 1).get(j));

        // adding the inc cell to its place instead of border cell
        boardSkeleton.get(i).add(j, incCell);
        boardSkeleton.get(i).remove(j + 1); // removing the border cell that got pushed
      }
    }
    boardSkeleton.get(1).get(1).flood(); // floods first cell
    return boardSkeleton;
  }

  // takes in a board skeleton and fills it with border cells
  void fillWithBorderCells(ArrayList<ArrayList<ACell>> boardSkeleton, int size) {
    for (int i = 0; i < size; i++) {
      ArrayList<ACell> row = new ArrayList<ACell>();

      for (int j = 0; j < size; j++) {
        ACell incCell = new BorderCell();
        row.add(incCell);
      }
      boardSkeleton.add(row);
    }
  }

  // takes in a number and returns a list of that amount of colors in it
  ArrayList<Color> colorList(int num) {
    ArrayList<Color> list = new ArrayList<Color>();
    list.add(Color.blue);
    list.add(Color.red);
    list.add(Color.yellow);

    if (num == 4) {
      list.add(Color.pink);
    } else if (num == 5) {
      list.add(Color.pink);
      list.add(Color.green);
    } else if (num == 6) {
      list.add(Color.pink);
      list.add(Color.green);
      list.add(Color.orange);
    } else if (num == 7) {
      list.add(Color.pink);
      list.add(Color.green);
      list.add(Color.orange);
      list.add(Color.magenta);
    } else if (num == 8) {
      list.add(Color.pink);
      list.add(Color.green);
      list.add(Color.orange);
      list.add(Color.magenta);
      list.add(Color.cyan);
    }
    return list;
  }

  // goes over our game board, one cell at a time, and invokes drawCell on each
  // cell to return a drawn out game board
  public WorldScene makeScene() {
    WorldScene game = new WorldScene(this.board.size() * 20, this.board.size() * 20 + 75);

    for (int i = 1; i < this.board.size() - 2; i++) {
      for (int j = 1; j < this.board.size() - 2; j++) {
        game.placeImageXY(this.board.get(i).get(j).drawCell(), i * 20, j * 20);
        game.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), i * 20,
            j * 20);
      }
    }
    // showing steps
    game.placeImageXY(new TextImage(
        Integer.toString(this.stepsUsed) + "/" + Integer.toString(this.stepsAllowed) + "  Steps",
        size + 6, Color.black), size * 10, size * 20 + 33);
    // showing the name of the game
    game.placeImageXY(new TextImage("FloodIt!", size + 10, Color.MAGENTA.darker()), size * 10,
        size * 20 + 5);
    // showing the timer
    game.placeImageXY(new TextImage(Integer.toString(this.timer) + " seconds", size + 6, Color.red),
        size * 10, size * 20 + 53);
    // showing the possibility of restarting
    game.placeImageXY(new TextImage("press 'r' to restart", size + 3, Color.DARK_GRAY), size * 10,
        size * 20 + 75);
    return game;
  }

  // updates world every tick
  public void onTick() {
    // increments timer
    this.timer++;
    // if we are flooding, call flooder, which floods whatever needs to be flooded
    if (this.flooding) {
      ArrayList<ACell> visited = new ArrayList<ACell>();
      firstCell.flooder(clickedCell, firstCell, visited, firstCell.getColor());
    }
    // checks if all cells are flooded (same color) game ends in victory if true.
    boolean allFlooded = true;
    for (int i = 1; i < this.board.size() - 2; i++) {
      for (int j = 1; j < this.board.size() - 2; j++) {
        if (!board.get(i).get(j).sameColor(firstCell)) {
          allFlooded = false;
        }
      }
    }
    if (allFlooded) {
      this.endOfWorld("You Win!");
    }
    // if all steps were used, game ends in loss.
    else if (this.stepsUsed == this.stepsAllowed) {
      this.endOfWorld("You Lose!");
    }
    // stopping the flood.
    else {
      this.flooding = false;
    }
  }

  // updates world when mouse is pressed
  public void onMousePressed(Posn pos) {
    // checking that the click is indeed on the board and not somewhere else
    // on the window
    if ((pos.x + 10) / 20 < this.board.size() && (pos.y + 10) / 20 < this.board.size()) {
      this.clickedCell = this.board.get((pos.x + 10) / 20).get((pos.y + 10) / 20);
      if (!this.clickedCell.sameColor(this.firstCell) && clickedCell.isCell()) {
        // enters "flooding" mode
        this.flooding = true;
        // increments steps used
        this.stepsUsed++;
      }
    }
  }

  // when r is pressed, resets the game and all counters
  public void onKeyEvent(String key) {
    if (key.equals("r")) {
      this.board = initBoard(this.size, this.colors);
      this.stepsUsed = 0;
      this.firstCell = this.board.get(1).get(1);
      this.clickedCell = this.board.get(0).get(0);
      this.timer = 0;
    }
  }

  // returns the end scene of the game
  public WorldScene lastScene(String msg) {
    WorldScene game = new WorldScene(this.board.size() * 20, this.board.size() * 20 + 75);

    for (int i = 1; i < this.board.size() - 2; i++) {
      for (int j = 1; j < this.board.size() - 2; j++) {
        game.placeImageXY(this.board.get(i).get(j).drawCell(), i * 20, j * 20);
        game.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), i * 20,
            j * 20);
      }
    }
    if (msg.equals("You Lose!")) {
      game.placeImageXY(new TextImage("You Lose!", 20, Color.black), size * 10, size * 20 + 33);
      game.placeImageXY(
          new TextImage(Integer.toString(this.timer) + " seconds", size + 6, Color.black),
          size * 10, size * 20 + 53);
      game.placeImageXY(new TextImage("FloodIt!", size + 10, Color.MAGENTA.darker()), size * 10,
          size * 20 + 5);
    }

    if (msg.equals("You Win!")) {
      game.placeImageXY(new TextImage("You Win!", 20, Color.black), size * 10, size * 20 + 33);
      game.placeImageXY(
          new TextImage(Integer.toString(this.timer) + " seconds", size + 6, Color.black),
          size * 10, size * 20 + 53);
      game.placeImageXY(new TextImage("FloodIt!", size + 10, Color.MAGENTA.darker()), size * 10,
          size * 20 + 5);
    }

    return game;
  }

}

class ExamplesFloodIt {

  // when running examples, two game consoles will open- one small one for
  // drawScene testing
  // and 1 big one (which you can change its size and is called on line) that
  // represents
  // the "real" game.

  int size = 14;
  FloodItWorld g = new FloodItWorld(size, 6);
  FloodItWorld world4 = new FloodItWorld(6, 4, 3);

  // just some cell examples for tests coordinates and nearby cells don't make
  // sense
  ACell pureBorderCell; // will only point to itself with its near cells
  ACell bCell;
  // These were created as Cell and not ACell for testing, so we can access fields
  // such as cell1.flooded
  Cell cell1;
  Cell cell2;
  Cell cell3;
  Cell cell4;
  Cell cell5;

  ArrayList<Color> colorList;

  FloodItWorld world;
  WorldScene scene;
  int worldSize;

  // initializes data
  void initData() {
    bCell = new BorderCell();
    pureBorderCell = new BorderCell();
    cell1 = new Cell(0, 1, Color.blue, false, bCell, bCell, bCell, bCell);
    cell2 = new Cell(0, 0, Color.red, false, cell1, bCell, bCell, bCell);
    cell3 = new Cell(1, 3, Color.yellow, true, bCell, cell1, cell2, bCell);
    cell4 = new Cell(1, 2, Color.green, false, bCell, cell1, cell2, cell3);
    cell5 = new Cell(3, 3, Color.blue, true, bCell, bCell, bCell, bCell);
    colorList = new ArrayList<Color>();
    colorList.add(Color.blue);
    colorList.add(Color.red);
    colorList.add(Color.yellow);
    worldSize = 4;
    world = new FloodItWorld(worldSize, 4, 3);
    scene = new WorldScene(120, 195);

  }

  //tests sameColor
  void testSameColor(Tester t) {
    t.checkExpect(cell1.sameColor(cell2), false);
    t.checkExpect(cell1.sameColor(cell5), true);
  }

  //tests sameColorHelp
  void testSameColorHelp(Tester t) {
    t.checkExpect(cell1.sameColorHelp(cell2), false);
    t.checkExpect(cell1.sameColorHelp(cell5), true);
  }

  //tests isCell
  void testIsCell(Tester t) {
    t.checkExpect(cell1.isCell(), true);
    t.checkExpect(cell3.isCell(), true);
    t.checkExpect(bCell.isCell(), false);
    t.checkExpect(pureBorderCell.isCell(), false);
  }

  //tests isFlooded
  void testIsFlooded(Tester t) {
    t.checkExpect(cell1.isFlooded(), false);
    t.checkExpect(cell2.isFlooded(), false);
    t.checkExpect(cell5.isFlooded(), true);
  }

  //tests getColor
  void testGetColor(Tester t) {
    t.checkExpect(cell1.getColor(), Color.blue);
    t.checkExpect(cell2.getColor(), Color.red);
    t.checkExpect(cell3.getColor(), Color.yellow);
  }

  // runs the game
  void testGame(Tester t) {
    g.bigBang(size * 20, size * 20 + 85, 1);
  }

  // tests that the method produces a correctly sized board of all border cells
  void testFillWithBorder(Tester t) {
    initData();
    ArrayList<ArrayList<ACell>> boardSkeleton = new ArrayList<ArrayList<ACell>>();
    g.fillWithBorderCells(boardSkeleton, 4);

    t.checkExpect(boardSkeleton.size(), 4);
    t.checkExpect(boardSkeleton.get(3).size(), 4);
    t.checkExpect(boardSkeleton.get(1).get(2), pureBorderCell);
    t.checkExpect(boardSkeleton.get(0).get(3), pureBorderCell);
    t.checkExpect(boardSkeleton.get(3).get(0), pureBorderCell);
    t.checkExpect(boardSkeleton.get(2).get(1), pureBorderCell);
  }

  // tests that the method flood floods a cell
  void testFlood(Tester t) {
    initData();

    t.checkExpect(cell1.flooded, false);
    t.checkExpect(cell2.flooded, false);
    t.checkExpect(cell3.flooded, true);

    cell1.flood();
    cell2.flood();
    cell3.flood();

    t.checkExpect(cell1.flooded, true);
    t.checkExpect(cell2.flooded, true);
    t.checkExpect(cell3.flooded, true);
  }

  // tests that the draw cell method draws the correct cell
  void testDrawCell(Tester t) {
    initData();
    t.checkExpect(bCell.drawCell(), new RectangleImage(20, 20, OutlineMode.SOLID, Color.white));

    t.checkExpect(cell1.drawCell(), new RectangleImage(20, 20, OutlineMode.SOLID, Color.blue));

    t.checkExpect(cell2.drawCell(), new RectangleImage(20, 20, OutlineMode.SOLID, Color.red));

    t.checkExpect(cell3.drawCell(), new RectangleImage(20, 20, OutlineMode.SOLID, Color.yellow));
  }

  // Sample board and game state
  ArrayList<ArrayList<ACell>> board1 = new ArrayList<ArrayList<ACell>>();
  ArrayList<ArrayList<ACell>> board2 = new ArrayList<ArrayList<ACell>>();
  FloodItWorld game1 = new FloodItWorld(3, 3, 1234);
  FloodItWorld game2 = new FloodItWorld(4, 4, 5678);

  void testInitBoard(Tester t) {
    // Testing if the board is generated correctly
    board1 = game1.initBoard(3, 3, 3);
    board2 = game2.initBoard(4, 4, 3);
    t.checkExpect(board1.get(1).get(1).getColor(), new Color(255, 255, 0)); // Top left cell color
    t.checkExpect(board1.get(2).get(2).getColor(), new Color(255, 0, 0)); // Center cell color
    t.checkExpect(board2.get(1).get(1).getColor(), new Color(255, 255, 0)); // Top left cell color
    t.checkExpect(board2.get(3).get(3).getColor(), new Color(0, 0, 255)); // Bottom right cell color

    // Testing if the cells are correctly linked
    t.checkExpect(board1.get(1).get(2).left, board1.get(1).get(1));
    // Left cell of the top-left cell^
    t.checkExpect(board1.get(1).get(2).right, board1.get(1).get(3));
    // Right cell of the top-left cell^
    t.checkExpect(board2.get(3).get(3).top, board2.get(2).get(3));
    // Top cell of the center cell^
    t.checkExpect(board2.get(3).get(3).right, board2.get(3).get(4));
    // Right cell of the center cell^
    t.checkExpect(board2.get(3).get(3).bottom, board2.get(4).get(3));
    // Bottom cell of the center cell^
    t.checkExpect(board2.get(1).get(1).top, board2.get(0).get(1));

  }

  // tests that colorList returns a correct list of colors
  void testColorList(Tester t) {
    initData();
    t.checkExpect(g.colorList(3), colorList);

    colorList.add(Color.pink);
    t.checkExpect(g.colorList(4), colorList);

    colorList.add(Color.green);
    t.checkExpect(g.colorList(5), colorList);

    colorList.add(Color.orange);
    t.checkExpect(g.colorList(6), colorList);

    colorList.add(Color.magenta);
    t.checkExpect(g.colorList(7), colorList);

    colorList.add(Color.cyan);
    t.checkExpect(g.colorList(8), colorList);
  }

  //testing that flooding is working well
  void testFlooder(Tester t) {
    game1 = new FloodItWorld(3, 3, 1234);
    ArrayList<ACell> visited = new ArrayList<ACell>();
    //checking that top left cell is originally yellow
    t.checkExpect(game1.board.get(1).get(1).getColor(), Color.yellow);
    //checking that second yellow cell below is originally not flooded and yellow
    t.checkExpect(game1.board.get(1).get(2).isFlooded(), false);
    t.checkExpect(game1.board.get(1).get(2).getColor(), Color.yellow);
    //making sure the clicked cell is indeed blue
    t.checkExpect(game1.board.get(2).get(2).getColor(), Color.blue);

    //using the flooder on the first cell, forwarding a clicked cell (blue cell)
    //the first cell (yellow cell), an empty list of visited cells, 
    //and an original color (yellow)
    game1.board.get(1).get(1).flooder(game1.board.get(2).get(2),
        game1.board.get(1).get(1), visited, Color.yellow);

    //checking that top left cell is now blue
    t.checkExpect(game1.board.get(1).get(1).getColor(), Color.blue);
    //checking that second yellow cell below is now flooded and blue
    t.checkExpect(game1.board.get(1).get(2).isFlooded(), true);
    t.checkExpect(game1.board.get(1).get(2).getColor(), Color.blue);
  }

  // tests the exceptions that may be thrown from our constructor
  void testExceptions(Tester t) {
    t.checkConstructorException(new IllegalArgumentException("Invalid colors amount!"),
        "FloodItWorld", 8, 12);

    t.checkConstructorException(new IllegalArgumentException("Invalid colors amount!"),
        "FloodItWorld", 8, 2);

    t.checkConstructorException(new IllegalArgumentException("Invalid size!"),
        "FloodItWorld", 32, 4);

    t.checkConstructorException(new IllegalArgumentException("Invalid size!"),
        "FloodItWorld", 1, 4);
  }

  // tests that our makeScene produces the right scene, uses the seeded random
  // constructor so we can reliably test the same board over and over again.
//  void testMakeScene(Tester t) {
//    initData();
//
//    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.yellow), 20, 20);
//    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 20, 20);
//    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.yellow), 20, 40);
//    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 20, 40);
//    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.blue), 20, 60);
//    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 20, 60);
//    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.pink), 40, 20);
//    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 40, 20);
//    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.blue), 40, 40);
//    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 40, 40);
//    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.yellow), 40, 60);
//    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 40, 60);
//    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.pink), 60, 20);
//    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 60, 20);
//    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.yellow), 60, 40);
//    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 60, 40);
//    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.blue), 60, 60);
//    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 60, 60);
//
//    scene.placeImageXY(new TextImage("0/5  Steps", worldSize + 6, Color.black), 40, 113);
//
//    // showing the name of the game
//    scene.placeImageXY(new TextImage("FloodIt!", worldSize + 10, Color.MAGENTA.darker()), 40,
//        worldSize * 20 + 5);
//
//    // showing the timer
//    scene.placeImageXY(new TextImage("0 seconds", worldSize + 6, Color.red), worldSize * 10,
//        worldSize * 20 + 53);
//
//    // showing the possibility of restarting
//    scene.placeImageXY(new TextImage("press 'r' to restart", worldSize + 3, Color.DARK_GRAY),
//        worldSize * 10, worldSize * 20 + 75);
//
//    t.checkExpect(world.makeScene(), scene);
//    world.bigBang(worldSize * 20, worldSize * 20 + 85, 1);
//  }

  // tests onMousePressed
  void testOnMousePressed(Tester t) {
    initData();
    world.onMousePressed(new Posn(60, 60));

    t.checkExpect(world.flooding, true);
    t.checkExpect(world.stepsUsed, 1);
    t.checkExpect(world.clickedCell.getColor(), Color.blue);

  }

  // test onTick
  void testOnTick(Tester t) {
    initData();

    world.onTick();
    t.checkExpect(world.timer, 1);
    t.checkExpect(world.flooding, false);
    t.checkExpect(world.firstCell, world.firstCell);
    world.stepsUsed = 5;
    world.stepsAllowed = 5;

  }

  // tests onKey
  void testOnKey(Tester t) {
    initData();
    world.stepsUsed = 7;
    world.timer = 30;
    world.onKeyEvent("r");
    t.checkExpect(world.stepsUsed, 0);
    t.checkExpect(world.timer, 0);
    t.checkExpect(world.clickedCell, pureBorderCell);

  }

  // tests lastScene
  void testLastScene(Tester t) {

    // LOSING
    initData();

    world.lastScene("You Lose!");

    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.yellow), 20, 20);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 20, 20);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.yellow), 20, 40);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 20, 40);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.blue), 20, 60);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 20, 60);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.pink), 40, 20);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 40, 20);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.blue), 40, 40);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 40, 40);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.yellow), 40, 60);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 40, 60);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.pink), 60, 20);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 60, 20);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.yellow), 60, 40);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 60, 40);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.blue), 60, 60);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 60, 60);
    scene.placeImageXY(new TextImage("You Lose!", 20, Color.black), worldSize * 10, 113);
    scene.placeImageXY(new TextImage("0 seconds", 10, Color.black), worldSize * 10,
        worldSize * 20 + 53);
    scene.placeImageXY(new TextImage("FloodIt!", 14, Color.MAGENTA.darker()), worldSize * 10,
        worldSize * 20 + 5);

    // test
    t.checkExpect(world.lastScene("You Lose!"), scene);

    // WINNNING
    initData();

    world.lastScene("You Win!");

    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.yellow), 20, 20);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 20, 20);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.yellow), 20, 40);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 20, 40);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.blue), 20, 60);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 20, 60);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.pink), 40, 20);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 40, 20);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.blue), 40, 40);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 40, 40);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.yellow), 40, 60);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 40, 60);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.pink), 60, 20);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 60, 20);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.yellow), 60, 40);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 60, 40);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.blue), 60, 60);
    scene.placeImageXY(new RectangleImage(20, 20, OutlineMode.OUTLINE, Color.BLACK), 60, 60);
    scene.placeImageXY(new TextImage("You Win!", 20, Color.black), worldSize * 10,
        worldSize * 20 + 33);
    scene.placeImageXY(new TextImage("0 seconds", worldSize + 6, Color.black), worldSize * 10,
        worldSize * 20 + 53);
    scene.placeImageXY(new TextImage("FloodIt!", worldSize + 10, Color.MAGENTA.darker()),
        worldSize * 10, worldSize * 20 + 5);

    // test
    t.checkExpect(world.lastScene("You Win!"), scene);
  }

}
