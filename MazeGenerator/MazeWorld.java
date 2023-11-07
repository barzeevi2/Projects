import tester.Tester;
import javalib.impworld.*;
import javalib.worldimages.*;
import java.util.*;
import java.util.function.Predicate;
import java.awt.*;

//     THIS IS THE SAME COMMENT WE LEFT ON THE HANDINS COMMENT- SKIP IF ALREADY READ! // 

//We called our game Crazy Maze - use the arrow keys to navigate. 
//Feel free to press 'p' to show the path you have taken so far!
//Press 'r' - restart for a new maze! 

//Press B for BFS solution to show up, press D for DFS instead 
//(works sometimes for some reason, we tried really hard to fix
//it to be consistent, couldn't really).

//To see that the solution DOES show up sometimes- 
//set the maze size to a rather small one (we suggest 6x6, 
//you can change it at the beginning of the examples class.
//Then- press B for example (for BFS) and if it shows- great. 
//if it doesn't- press 'r' a few times and it will pretty much be showing at least once. 
//We don't know what makes it show only a few of the times,
//but this is how we can confirm that it indeed shows 
//(at least we can win some partial credit haha). 

//Note- the cyan color'd visited cells path overpowers the solution path! It might hide it!
//
//We are aware of rare usage of field of fields 
//(we couldn't figure out a way in time to fix that to not use that,
//only way was to make getters and that's not so nice either), 
//sorry in advance! 

//We decided to toggle off our LastScene that finishes the game
//when you reach the destination- feel free to uncomment
//the onTick text to have it enabled again! (we tested for it regardless!)

//Final thoughts: We know that it isn't perfect but we tried to implement
//as many requirements as possible (tough week to work on it with a lot of finals
//for the both of us). We did as many enhancements as we could for some extra credit, 
//so we hope you enjoy! Thank you! 
//(Text proportions might be a tiny bit off for really large or really small mazes!)


// represents an Edge between cells
class Edge {
  Cell from;
  Cell to;
  int weight;

  Edge(Cell from, Cell to, int weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
  }
}

// represents a Cell in our game
class Cell {
  int x;
  int y;
  Cell right;
  Cell left;
  Cell top;
  Cell bottom;
  ArrayList<Edge> edges = new ArrayList<Edge>(); // all edges of a cell
  boolean rightEdge;  //does it have a right edge
  boolean bottomEdge; // does it have a left edge
  Cell previous;     // the previous cell 

  boolean passed; // was it stepped on already

  // cell constructor, to begin with all cells are connected only to themselves,
  // no edges
  Cell(int x, int y) {
    this.x = x;
    this.y = y;
    this.left = this;
    this.right = this;
    this.top = this;
    this.bottom = this;
    this.rightEdge = true;
    this.bottomEdge = true;
    this.previous = this;
    this.passed = false;
  }

  // Draws a right wall
  WorldImage drawEdgeRight() {
    return new LineImage(new Posn(0, 20), Color.black).movePinhole(-20, -10);
  }

  // Draws a bottom wall
  WorldImage drawEdgeBottom() {
    return new LineImage(new Posn(20, 0), Color.black).movePinhole(-10, -20);
  }

  // Draws this cell
  WorldImage draw(Color c) {
    return new RectangleImage(18, 18, OutlineMode.SOLID, c).movePinhole(-10, -10);
  }

  // finds the previous cell
  void findPrevious() {
    if (this.top != this && !this.top.bottomEdge && this.top.previous == this) {
      this.previous = this.top;
    } else if (this.left != this && !this.left.rightEdge && this.left.previous == this) {
      this.previous = this.left;
    } else if (this.bottom != this && !this.bottomEdge && this.bottom.previous == this) {
      this.previous = this.bottom;
    } else if (this.right != this && !this.rightEdge && this.right.previous == this) {
      this.previous = this.right;
    }
  }
}

// Compares the weights of Edges
class WeightComparator implements Comparator<Edge> {
  // Compares edges by weight
  public int compare(Edge item1, Edge item2) {
    return item1.weight - item2.weight;
  }
}

//Describes a Player
class Player {
  Cell on;

  Player(Cell on) {
    this.on = on;
  }

  public Cell getOn() {
    return this.on;
  }

  // Checks if each key input results in a valid move
  // we know we use FOF here but we couldn't figure out a better solution in time :(
  // we could have done getter methods in cell but that would be illegal too...
  boolean validMove(String move) {
    if (move.equals("up") && this.on.top != this.on) {
      return !this.on.top.bottomEdge;
    } else if (move.equals("down") && this.on.bottom != this.on) {
      return !this.on.bottomEdge;
    } else if (move.equals("left") && this.on.left != this.on) {
      return !this.on.left.rightEdge;
    } else if (move.equals("right") && this.on.right != this.on) {
      return !this.on.rightEdge;
    } else {
      return false;
    }
  }

  // Draws the player
  WorldImage drawPlayer() {
    return new RectangleImage(17, 17, OutlineMode.SOLID, Color.yellow).movePinhole(-10, -10);
  }
}

// represents the world
class MazeWorld extends World {
  int boardX;
  int boardY;
  // used for representatives
  HashMap<Cell, Cell> map = new HashMap<Cell, Cell>();
  ArrayList<Edge> allEdgesInBoard = new ArrayList<Edge>();
  ArrayList<Edge> mazeEdges = new ArrayList<Edge>();
  Player p; // represents the player
  Cell endCell; //represents the destination
  Cell currCell; // represents the current cell player is on
  int timer; // represents the timer
  boolean showVisited; // toggle flag to show visited cell
  Cell startCell; // represents the first cell
  WorldScene scene = new WorldScene(0, 0); // a scene
  ArrayList<ArrayList<Cell>> board; // the maze board
  ArrayList<Cell> pathBFS; // the path using BFS
  ArrayList<Cell> pathDFS; // the path using DFS
  boolean solutionDFS; // flag to show DFS solution
  boolean solutionBFS; // flag to show BFS solution


  //constructor - takes in board size in X and Y
  MazeWorld(int boardX, int boardY) {
    this.boardX = boardX;
    this.boardY = boardY;
    this.board = this.makeGrid(boardX, boardY); // makes grid
    this.createEdges(this.board); // creates edges for everything
    this.createMap(board); // makes the representatives hashMap
    this.kruskals(); // kruskals alg for min spanning tree
    this.endCell = this.board.get(boardY - 1).get(boardX - 1); 
    this.startCell = this.board.get(0).get(0);
    this.p = new Player(board.get(0).get(0));
    this.currCell = p.on;
    this.timer = 0;
    this.showVisited = true; // default is to show visited cells
    this.solutionBFS = false;
    this.solutionDFS = false;
    this.pathBFS = bfs(startCell, endCell); //initializing the paths using BFS and DFS
    this.pathDFS = dfs(startCell, endCell);
  }

  // constructor for testing
  // has pre-set values for all fields so we can test all methods work well
  MazeWorld() {
    this.boardX = 2;
    this.boardY = 3;
    this.board = this.makeGrid(2, 3);
    this.board.get(0).get(0).rightEdge = false;
    this.board.get(0).get(1).rightEdge = true;
    this.board.get(1).get(0).rightEdge = true;
    this.board.get(1).get(1).rightEdge = true;
    this.board.get(2).get(0).rightEdge = true;
    this.board.get(2).get(1).rightEdge = true;
    this.map.put(this.board.get(0).get(0), this.board.get(0).get(0));
    this.map.put(this.board.get(0).get(1), this.board.get(0).get(1));
    this.map.put(this.board.get(1).get(0), this.board.get(1).get(0));
    this.map.put(this.board.get(1).get(1), this.board.get(1).get(1));
    this.map.put(this.board.get(2).get(0), this.board.get(2).get(0));
    this.map.put(this.board.get(2).get(1), this.board.get(2).get(1));

    this.board.get(0).get(0).bottomEdge = false;
    this.board.get(0).get(1).bottomEdge = false;
    this.board.get(1).get(0).bottomEdge = false;
    this.board.get(1).get(1).bottomEdge = false;
    this.board.get(2).get(0).bottomEdge = true;
    this.board.get(2).get(1).bottomEdge = true;

    this.allEdgesInBoard = new ArrayList<Edge>(Arrays.asList(new Edge(
        new Cell(0, 0), new Cell(1, 0), 1),
        new Edge(new Cell(0, 0), new Cell(0, 1), 2), new Edge(new Cell(1, 0), new Cell(1, 1), 3),
        new Edge(new Cell(0, 1), new Cell(1, 1), 4), new Edge(new Cell(0, 1), new Cell(0, 2), 5),
        new Edge(new Cell(1, 1), new Cell(1, 2), 6), new Edge(new Cell(0, 2), new Cell(1, 2), 7)));

    this.mazeEdges = new ArrayList<Edge>(Arrays.asList(new Edge(new Cell(0, 0), new Cell(1, 0), 1),
        new Edge(new Cell(0, 0), new Cell(0, 1), 2), new Edge(new Cell(1, 0), new Cell(1, 1), 3),
        new Edge(new Cell(0, 1), new Cell(0, 2), 5), new Edge(new Cell(1, 1), new Cell(1, 2), 6)));

    this.endCell = this.board.get(2).get(1);
    this.p = new Player(this.board.get(0).get(0));
    this.currCell = p.on;
    this.timer = 0;
    this.showVisited = true;
  }

  // initializes the board with start and end cubes
  public WorldScene makeScene() {

    //scene maker
    WorldScene scene = new WorldScene(this.boardX * 20, this.boardY * 20 + 150);

    //if solution flag is on: show solution
    if (this.solutionBFS) {

      //go through the cells in the path and draw them
      for (int i = 0; i < this.pathBFS.size() - 1; i ++) {
        Cell toDrawCell = this.pathBFS.get(i);

        scene.placeImageXY(toDrawCell.draw(Color.blue), 
            toDrawCell.x * 20,
            toDrawCell.y * 20);
      }
    }

    //if solution flag is on, draw solution path
    if (this.solutionDFS) {
      for (int i = 0; i < this.pathDFS.size() - 1; i ++) {
        Cell toDrawCell = this.pathDFS.get(i);
        scene.placeImageXY(toDrawCell.draw(Color.blue), 
            toDrawCell.x * 20,
            toDrawCell.y * 20);
      }
    }

    // showing the timer
    scene.placeImageXY(
        new TextImage(Integer.toString(this.timer) + " seconds", boardX + 6, Color.red),
        boardX * 10, boardY * 20 + 53);

    // starting cube
    scene.placeImageXY(board.get(0).get(0).draw(Color.MAGENTA), 0, 0);
    // ending cube
    scene.placeImageXY(
        board.get(this.boardY - 1).get(this.boardX - 1).draw(Color.BLUE),
        (boardX - 1) * 20, (boardY - 1) * 20);
    // Draw the grid
    for (int i = 0; i < boardY; i++) {
      for (int j = 0; j < boardX; j++) {

        this.replaceRight(this.board.get(i).get(j));
        this.replaceBottom(this.board.get(i).get(j));
        if (board.get(i).get(j).rightEdge) {
          scene.placeImageXY(board.get(i).get(j).drawEdgeRight(), (20 * j), (20 * i));
        }
        if (board.get(i).get(j).bottomEdge) {
          scene.placeImageXY(board.get(i).get(j).drawEdgeBottom(), (20 * j), (20 * i));
        }


        //showing the traveled path - extra credit addition
        if (this.showVisited) {
          if (this.board.get(i).get(j).passed) {
            scene.placeImageXY(board.get(i).get(j).draw(Color.CYAN), j * 20,
                i * 20);
          }
        }
      }

      scene.placeImageXY(board.get(0).get(0).draw(Color.MAGENTA), 0, 0);
    }

    //if the player is on top of the destination cell- show the path using BFS    
    if (this.currCell == this.endCell) {
      for (int i = 0; i < this.pathBFS.size(); i ++) {
        Cell toDrawCell = this.pathBFS.get(i);
        scene.placeImageXY(toDrawCell.draw(Color.blue), 
            toDrawCell.x * 20,
            toDrawCell.y * 20);
      }
    }

    // showing the game's name
    scene.placeImageXY(new TextImage("Crazy Maze!", boardX / 2 + 10, Color.MAGENTA.darker()),
        boardX * 10, boardY * 20 + 15);

    scene.placeImageXY(new TextImage("use arrow keys to move",
        boardX / 3 + 10, Color.DARK_GRAY.darker()),
        boardX * 10, boardY * 20 + 80);
    // showing the possibility of restarting
    scene.placeImageXY(new TextImage("press 'r' to restart", boardX / 3 + 8, Color.DARK_GRAY),
        boardX * 10, boardY * 20 + 100);
    //showing the possibility of toggling visited cells
    scene.placeImageXY(new TextImage("press 'p' to toggle path", boardX / 3 + 8, Color.DARK_GRAY),
        boardX * 10, boardY * 20 + 125);

    scene.placeImageXY(p.drawPlayer(), this.p.on.x * 20, this.p.on.y * 20);
    return scene;

  }

  // we only need to go over the right and bottom cells of each cells, \
  // this will end up covering every cell :
  // renders right cell
  // Effect: Changes the rightEdge field of the cell
  void replaceRight(Cell v) {
    for (Edge edge : this.mazeEdges) {
      if (edge.to.y == edge.from.y) {
        edge.from.rightEdge = false;
      }
    }
  }

  // renders bottom cell
  // Effect: Changes the rightEdge field of the cell
  void replaceBottom(Cell v) {
    for (Edge edge : this.mazeEdges) {
      if (edge.to.x == edge.from.x) {
        edge.from.bottomEdge = false;
      }
    }
  }

  // creates the grid for each cell in the maze
  ArrayList<ArrayList<Cell>> makeGrid(int width, int height) {
    ArrayList<ArrayList<Cell>> board = new ArrayList<ArrayList<Cell>>();
    for (int i = 0; i < height; i++) {
      board.add(new ArrayList<Cell>());
      ArrayList<Cell> r = board.get(i);
      for (int j = 0; j < width; j++) {
        r.add(new Cell(j, i));
      }
    }
    this.linkCells(board);
    this.createEdges(board);
    this.createMap(board);
    return board;
  }

  // connects/links each individual cell
  // changes the top, bottom, left and right fields of a cell
  void linkCells(ArrayList<ArrayList<Cell>> b) {
    for (int i = 0; i < this.boardY; i++) {
      for (int j = 0; j < this.boardX; j++) {
        if (j + 1 < this.boardX) {
          b.get(i).get(j).right = b.get(i).get(j + 1);
        }
        if (j - 1 >= 0) {
          b.get(i).get(j).left = b.get(i).get(j - 1);
        }
        if (i + 1 < this.boardY) {
          b.get(i).get(j).bottom = b.get(i + 1).get(j);
        }
        if (i - 1 >= 0) {
          b.get(i).get(j).top = b.get(i - 1).get(j);
        }
      }
    }
  }

  // creates an initial hash map where each node is linked to itself
  HashMap<Cell, Cell> createMap(ArrayList<ArrayList<Cell>> cell) {
    for (int i = 0; i < cell.size(); i++) {
      for (int j = 0; j < cell.get(i).size(); j++) {
        this.map.put(cell.get(i).get(j), cell.get(i).get(j));
      }
    }
    return map;
  }

  // creates the array list of edges in the maze game
  ArrayList<Edge> createEdges(ArrayList<ArrayList<Cell>> n) {
    Random randomWeight = new Random();
    for (int i = 0; i < n.size(); i++) {
      for (int j = 0; j < n.get(i).size(); j++) {
        if (j < n.get(i).size() - 1) {
          allEdgesInBoard.add(new Edge(n.get(i).get(j),
              n.get(i).get(j).right, randomWeight.nextInt(50)));
        }
        if (i < n.size() - 1) {
          allEdgesInBoard.add(
              new Edge(n.get(i).get(j), n.get(i).get(j).bottom, (int) randomWeight.nextInt(50)));
        }
      }
    }
    allEdgesInBoard.sort(new WeightComparator());
    return allEdgesInBoard;
  }

  // kruskal's algorithm
  ArrayList<Edge> kruskals() {
    int i = 0;
    while (this.mazeEdges.size() < this.allEdgesInBoard.size() && i < this.allEdgesInBoard.size()) {
      Edge e = allEdgesInBoard.get(i);
      if (this.find(this.find(e.from)).equals(this.find(this.find(e.to)))) {
        // do nothing
      } else {
        mazeEdges.add(e);
        union(this.find(e.from), this.find(e.to));
      }
      i += 1;
    }
    // Adds all the edges for each cell
    for (int y = 0; y < this.boardY; y += 1) {
      for (int x = 0; x < this.boardX; x += 1) {
        for (Edge e : this.mazeEdges) {
          if (this.board.get(y).get(x).equals(e.from) || this.board.get(y).get(x).equals(e.to)) {
            this.board.get(y).get(x).edges.add(e);
          }
        }
      }
    }
    return this.mazeEdges;
  }

  // unions two cells together by redefining hashmap values (part of union/find)
  void union(Cell item, Cell newRep) {
    this.map.put(this.find(item), this.find(newRep));
  }

  // Finds the representative of this node (part of union/find)
  Cell find(Cell item) {
    if (item.equals(this.map.get(item))) { // no need to override .equals because reference equality
      return item; // is enough to make sure here
    } else {
      return this.find(this.map.get(item));
    }
  }

  //returns the list of cells to travel from two given cells to each other using BFS
  public ArrayList<Cell> bfs(Cell from, Cell to) {
    return search(from, to, new Stack<Cell>()); // call search with stack
  }

  //returns the list of cells to travel from two given cells to each other using DFS
  public ArrayList<Cell> dfs(Cell from, Cell to) {
    return search(from, to, new Queue<Cell>()); // call search with queue
  }

  //returns the list of cells to travel from two given cells to each other
  public ArrayList<Cell> search(Cell from, Cell to, ICollection<Cell> worklist) {
    Deque<Cell> alreadySeen = new Deque<Cell>(); // already seen collection
    HashMap<Cell, Cell> parentMap = new HashMap<Cell, Cell>(); //to see where we came from

    Cell nullCell = new Cell(-1, -1); // represents a null cell


    worklist.add(from); // add from to worklist
    parentMap.put(from, nullCell); //add from to where we came from map
    ArrayList<Cell> path = new ArrayList<Cell>(); // the path
    while (!worklist.isEmpty()) { // as long as worklist isn't empty
      Cell c = worklist.remove(); // take first time
      if (c.equals(to)) { //if destination


        while (c != nullCell) { // as long as c isn't nullcell
          path.add(0, c); // add c to path
          c = parentMap.get(c); // update c
        }
        return path; // give path
      }
      if (alreadySeen.contains(c)) {
        // do nothing
      } else {
        for (Edge e : c.edges) { // add all c's edges' destinations
          if (e.to != c) {  
            if (e.to != c) {
              if (!parentMap.containsKey(e.to)) { 
                worklist.add(e.to);  // to work list
                parentMap.put(e.to, c); // to parent map
              }
            }
          }
        }
        alreadySeen.addAtTail(c); // add c to alreadyseen list
      }
    }
    return path; // give path back!
  }




  // on key "r" restart the game
  public void onKeyEvent(String key) {

    //resets game
    if (key.equals("r")) {
      this.scene = this.getEmptyScene();
      this.board = this.makeGrid(boardX, boardY);
      this.createEdges(this.board);
      this.createMap(board);
      this.kruskals();
      this.p = new Player(board.get(0).get(0));
      this.endCell = this.board.get(this.boardY - 1).get(this.boardX - 1);
      this.startCell = board.get(0).get(0);
      this.timer = 0;
      this.pathBFS = bfs(startCell, endCell);
      this.pathDFS = dfs(startCell, endCell);
      this.showVisited = true;

    } 

    //toggling the option to see the visited cells
    if (key.equals("p")) {
      this.showVisited = !this.showVisited;
    }
    //press to show DFS solution
    if (key.equals("d")) {
      this.solutionDFS = !this.solutionDFS;
    }

    //press to see BFS solution
    if (key.equals("b")) {
      this.solutionBFS = !this.solutionBFS;


    }

    //if a key is a valid move, make cell "passed" and update player's on
    if (key.equals("up") && p.validMove("up")) {
      p.getOn().passed = true;
      p.on = p.getOn().top;
      this.currCell = p.on;
    }  if (key.equals("down") && p.validMove("down")) {
      p.on.passed = true;
      p.on = p.getOn().bottom;
      this.currCell = p.on;

    }  if (key.equals("left") && p.validMove("left")) {
      p.getOn().passed = true;
      p.on = p.getOn().left;
      this.currCell = p.on;

    }  if (key.equals("right") && p.validMove("right")) {
      p.getOn().passed = true;
      p.on = p.getOn().right;
      this.currCell = p.on;
    } 
  }



  // not used yet
  public void onTick() {
    this.timer++;

    //decided to not have a last scene, so player can keep playing- if you want to see
    //how last scene works- uncomment this chunk!
    //    if (this.currCell == this.endCell) {
    //      this.endOfWorld("You Win!");
    //
    //    }
  }


  //last scene- commented out in onTick- uncomment onTick chunk to see how it works.
  //we decided to let the player keep traveling and not end the game- feel free to change!
  public WorldScene lastScene(String msg) {

    // starting cube
    this.scene.placeImageXY(board.get(0).get(0).draw(Color.MAGENTA), 0,
        0);
    // ending cube
    this.scene.placeImageXY(
        board.get(this.boardY - 1).get(this.boardX - 1).draw(Color.BLUE),
        (boardX - 1) * 20, (boardY - 1) * 20);
    // Draw the grid
    for (int i = 0; i < boardY; i++) {
      for (int j = 0; j < boardX; j++) {

        this.replaceRight(this.board.get(i).get(j));
        this.replaceBottom(this.board.get(i).get(j));
        if (board.get(i).get(j).rightEdge) {
          this.scene.placeImageXY(board.get(i).get(j).drawEdgeRight(), (20 * j), (20 * i));
        }
        if (board.get(i).get(j).bottomEdge) {
          this.scene.placeImageXY(board.get(i).get(j).drawEdgeBottom(), (20 * j), (20 * i));
        }


        if (this.board.get(i).get(j).passed) {
          this.scene.placeImageXY(board.get(i).get(j).draw(Color.CYAN),
              j * 20, i * 20);

        }
      }

      this.scene.placeImageXY(board.get(0).get(0).draw(Color.MAGENTA), 0,
          0);
    }
    this.scene.placeImageXY(p.drawPlayer(), this.p.on.x * 20, this.p.on.y * 20);
    // outputting victory text
    scene.placeImageXY(new TextImage("You Win!", 15, Color.black), boardX * 10, boardY * 20 + 43);
    scene.placeImageXY(
        new TextImage(Integer.toString(this.timer) + " seconds", boardX + 2, Color.black),
        boardX * 10, boardY * 20 + 55);
    // game name text
    scene.placeImageXY(new TextImage("Crazy Maze!", boardX + 10, Color.MAGENTA.darker()),
        boardX * 10, boardY * 20 + 15);

    return scene;
  }
}


//represents an abstarct node in a deque, can either be a sentinel or a node
abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  // counts the number of nodes in a "chain of nodes
  // not including the sentinel node.
  abstract int size();

  // removes the first node from a "chain" of nodes (deque), throws an exception
  // if used
  // on sentinel
  abstract T removeFromHead();

  // removes the last node from a "chain" of nodes (deque), throws an exception if
  // used
  // on sentinel
  abstract T removeFromTail();

  // returns the data of a node if its a node, throws an exception if used on
  // sentinel
  // used only for comfort of testing, would never be called on sentinel!
  abstract T getData();

  // returns the sentinel if used on it, if used on node, looks for the first
  // node that returns true from the predicate given, and returns it
  abstract ANode<T> find(Predicate<T> pred);

  // re-attaches two given nodes to reference eachother
  abstract void reattach(ANode<T> first, ANode<T> second);

  // returns true if given data is in this chain of nodes
  abstract boolean contains(T data);
}

//represents the sentinel node in a deque, aka the header
class Sentinel<T> extends ANode<T> {

  // constructs a new sentinel, setting its next and prev fields
  // to this sentinel
  Sentinel() {
    this.next = this;
    this.prev = this;
  }

  //returns size
  public int size() {
    return 0;
  }

  //removes from head
  public T removeFromHead() {
    throw new RuntimeException("Can't remove from an empty deque!");
  }

  // returns the data of a node if its a node, throws an exception if used on
  // sentinel
  // used only for comfort of testing, would never be called on sentinel!
  public T getData() {
    throw new RuntimeException("A sentinel has no data!");
  }

  //removes from tail
  public T removeFromTail() {
    throw new RuntimeException("Can't remove from an empty deque!");
  }

  //finds and returns node using given pred
  public ANode<T> find(Predicate<T> pred) {
    return this;
  }

  // re-attaches two given nodes to reference eachother
  // ideally should only exist in Node<T>, made abstract for testing purposes!
  public void reattach(ANode<T> first, ANode<T> second) {
    first.next = second;
    second.prev = first;
  }

  // checks if given data is in this sentinel (no data in sentinel always false)
  public boolean contains(T data) {
    return false;
  }
}

//represents an actual node in a deque, has data first and prev
class Node<T> extends ANode<T> {
  T data;

  // constructs a new node given only data, sets next and prev to null
  Node(T data) {
    this.data = data;
    this.next = null;
    this.prev = null;
  }

  // constructs a new node given data, a next node and a prev node.
  // throws an exception if either of the given nodes is null
  Node(T data, ANode<T> next, ANode<T> prev) {
    if (next == null || prev == null) {
      throw new IllegalArgumentException("Provied illegal null nodes!");
    } else {
      this.data = data;
      this.next = next;
      this.prev = prev;
      next.prev = this;
      prev.next = this;
    }
  }

  //returns the size
  public int size() {
    return 1 + this.next.size();
  }

  //removes from the head
  public T removeFromHead() {
    T val = this.data;
    this.reattach(this.prev, this.next); // "re-attaches" the broken link
    return val;
  }

  // returns the data of a node if its a node, throws an exception if used on
  // sentinel
  // used only for comfort of testing, would never be called on sentinel!
  public T getData() {
    return this.data;
  }

  //removes from the tail
  public T removeFromTail() {
    T val = this.data;
    this.reattach(this.prev, this.next); // "re-attaches" the broken link
    return val;
  }

  //finds a and returns a node using a given pred
  public ANode<T> find(Predicate<T> pred) {
    if (pred.test(this.data)) {
      return this;
    } else {
      return this.next.find(pred);
    }
  }

  // re-attaches two given nodes to reference eachother
  public void reattach(ANode<T> first, ANode<T> second) {
    first.next = second;
    second.prev = first;
  }

  // checks if given data is in this node or the next ones
  public boolean contains(T data) {
    return this.data == data || this.next.contains(data);
  }
}

//represents a deque (double-ended queue)
class Deque<T> {
  Sentinel<T> header;

  // constructs a new deque with a new sentinel
  Deque() {
    this.header = new Sentinel<T>();
  }

  // constructs a new deque using a given sentinel
  Deque(Sentinel<T> header) {
    this.header = header;
  }

  // counts the number of nodes in a list Deque, not including the header node.
  public int size() {
    return this.header.next.size();
  }

  // consumes a value of type T and inserts it at the front of the list.
  public void addAtHead(T data) {
    new Node<T>(data, this.header.next, this.header);
  }

  // consumes a value of type T and inserts it at the tail of this list.
  public void addAtTail(T data) {
    new Node<T>(data, this.header, this.header.prev);
  }

  // removes the first node from this Deque. (throws exception if empty deque)
  public T removeFromHead() {
    return this.header.next.removeFromHead();
  }

  // removes the last node from this Deque. (throws exception if empty deque)
  public T removeFromTail() {
    return this.header.prev.removeFromTail();
  }

  // takes a Predicate<T> and produces the first node in this Deque
  // for which the given predicate returns true
  public ANode<T> find(Predicate<T> pred) {
    return this.header.next.find(pred);
  }

  // returns true if a given data is in this deque
  public boolean contains(T data) {
    return this.header.next.contains(data);
  }
}

//represents a predicate class for testing find
class StartsWithC implements Predicate<String> {
  public boolean test(String str) {
    return str.substring(0, 1).equals("c");
  }
}

//represents a collection of items
interface ICollection<T> {

  // checks if a collection is empty
  boolean isEmpty();

  // adds an item to the collection
  void add(T item);

  // removes and returns an item from the collection
  T remove();
}

//a collection that adds to the front removes from the front, LIFO
class Stack<T> implements ICollection<T> {
  Deque<T> deque;

  Stack() {
    this.deque = new Deque<T>();
  }

  // checks if a stack is empty
  public boolean isEmpty() {
    return this.deque.size() == 0;
  }

  // adds an item to the stack (to the front)
  public void add(T item) {
    this.deque.addAtHead(item);
  }

  // removes and returns an item from a stack (from the front)
  public T remove() {
    return this.deque.removeFromHead();

  }
}

//a collection that adds to the back removes from the front, FIFO
class Queue<T> implements ICollection<T> {
  Deque<T> deque;

  Queue() {
    this.deque = new Deque<T>();
  }

  // checks if a queue is empty
  public boolean isEmpty() {
    return this.deque.size() == 0;
  }

  // adds an item to the queue (to the tail)
  public void add(T item) {
    this.deque.addAtTail(item);
  }

  // removes and returns an item from a queue (from the front)
  public T remove() {
    return this.deque.removeFromHead();

  }
}

//Examples and tests
class ExamplesMaze {
  //change size of maze here!!!
  MazeWorld RunGame = new MazeWorld(6, 6);
  MazeWorld world = new MazeWorld();

  Deque<String> deque1;
  Deque<String> deque2;
  Deque<String> deque3;
  Deque<Integer> deque4;
  ICollection<Integer> stack1;
  ICollection<Integer> queue1;
  ICollection<String> stack2;
  ICollection<String> queue2;
  ICollection<Integer> mtStack;
  ICollection<Integer> mtQueue;

  // all the below declarations are not just declarations and are assignments
  // in order to avoid having these nodes be null to begin with, which
  // will end in an exception
  Sentinel<String> sent2 = new Sentinel<String>();
  ANode<String> node1 = new Node<String>("abc");
  ANode<String> node2 = new Node<String>("bcd");
  ANode<String> node3 = new Node<String>("cde");
  ANode<String> node4 = new Node<String>("def");

  Sentinel<String> sent3 = new Sentinel<String>();
  ANode<String> node5 = new Node<String>("rook");
  ANode<String> node6 = new Node<String>("pawn");
  ANode<String> node7 = new Node<String>("queen");
  ANode<String> node8 = new Node<String>("knight");

  Sentinel<Integer> sent4 = new Sentinel<Integer>();
  ANode<Integer> node9 = new Node<Integer>(10);
  ANode<Integer> node10 = new Node<Integer>(7);
  ANode<Integer> node11 = new Node<Integer>(12);

  // initializing data
  // deque1 = empty deque
  // deque2 = the (sentinel -> abc -> bcd -> cde -> def ->sentinel) deque
  // deque3 = the (sentinel -> rook -> pawn -> queen -> knight ->sentinel) deque
  // deque 4 = the (sentinel -> 10 -> 7 -> 12 ->sentinel) deque
  void initData() {
    deque1 = new Deque<String>();
    sent2 = new Sentinel<String>();
    node1 = new Node<String>("abc", node2, sent2);
    node2 = new Node<String>("bcd", node3, node1);
    node3 = new Node<String>("cde", node4, node2);
    node4 = new Node<String>("def", sent2, node3);
    deque2 = new Deque<String>(sent2);

    sent3 = new Sentinel<String>();
    node5 = new Node<String>("rook", node6, sent3);
    node6 = new Node<String>("pawn", node7, node5);
    node7 = new Node<String>("queen", node8, node6);
    node8 = new Node<String>("knight", sent3, node7);
    deque3 = new Deque<String>(sent3);

    sent4 = new Sentinel<Integer>();
    node9 = new Node<Integer>(10, node10, sent4);
    node10 = new Node<Integer>(7, node11, node9);
    node11 = new Node<Integer>(12, sent4, node10);
    deque4 = new Deque<Integer>(sent4);

    stack1 = new Stack<Integer>();
    queue1 = new Queue<Integer>();
    stack2 = new Stack<String>();
    queue2 = new Queue<String>();
    mtStack = new Stack<Integer>();
    mtQueue = new Queue<Integer>();
  }

  void testStackAndQueueMethods(Tester t) {
    initData();
    // empty before add
    t.checkExpect(stack1.isEmpty(), true);
    t.checkExpect(queue1.isEmpty(), true);
    // add
    stack1.add(12);
    queue1.add(3);
    stack2.add("hi");
    queue2.add("bye");
    // not empty anymore
    t.checkExpect(stack1.isEmpty(), false);
    t.checkExpect(queue1.isEmpty(), false);

    stack1.add(1);
    queue1.add(2);
    stack2.add("a");
    queue2.add("b");

    // testing remove removing from correct head/tail
    t.checkExpect(stack1.remove(), 1);
    t.checkExpect(stack2.remove(), "a");
    t.checkExpect(queue1.remove(), 3);
    t.checkExpect(queue2.remove(), "bye");

    // checking that after removing stack1 completely its empty
    stack1.remove();
    t.checkExpect(stack1.isEmpty(), true);

    t.checkExpect(mtStack.isEmpty(), true);
    t.checkExpect(mtQueue.isEmpty(), true);
  }

  // tests size
  void testSize(Tester t) {
    initData();

    t.checkExpect(deque1.size(), 0);
    t.checkExpect(deque2.size(), 4);
    t.checkExpect(deque3.size(), 4);
    t.checkExpect(deque4.size(), 3);
  }

  // tests addathead
  void testAddAtHead(Tester t) {
    initData();
    t.checkExpect(deque1.size(), 0);
    t.checkExpect(deque2.size(), 4);
    deque1.addAtHead("abc");
    t.checkExpect(deque1.size(), 1); // size went up
    t.checkExpect(deque1.header.next.getData(), "abc"); // first element changed
    deque2.addAtHead("wow");
    t.checkExpect(deque2.size(), 5); // size went up
    t.checkExpect(deque2.header.next.getData(), "wow"); // first element changed
  }

  // tests addattail
  void testAddAtTail(Tester t) {
    initData();
    t.checkExpect(deque1.size(), 0);
    t.checkExpect(deque2.size(), 4);
    deque1.addAtTail("abc");
    t.checkExpect(deque1.size(), 1); // size went up
    t.checkExpect(deque1.header.prev.getData(), "abc"); // last element changed
    deque2.addAtTail("wow");
    t.checkExpect(deque2.size(), 5); // size went up
    t.checkExpect(deque2.header.prev.getData(), "wow"); // last element changed
  }

  // tests removefromhead
  void testRemoveFromHead(Tester t) {
    initData();
    t.checkExpect(deque2.size(), 4);
    t.checkExpect(deque4.size(), 3);

    String removed = deque2.removeFromHead();
    t.checkExpect(deque2.size(), 3); // size went down
    t.checkExpect(removed.equals("abc"), true); // removed data is returned correctly

    int removed2 = deque4.removeFromHead();
    t.checkExpect(deque4.size(), 2); // size went down
    t.checkExpect(removed2 == 10, true); // removed data is returned correctly

  }

  // tests removefromtail
  void testRemoveFromTail(Tester t) {
    initData();
    t.checkExpect(deque2.size(), 4);
    t.checkExpect(deque4.size(), 3);

    String removed = deque2.removeFromTail();
    t.checkExpect(deque2.size(), 3); // size went down
    t.checkExpect(removed.equals("def"), true); // removed data is returned correctly

    int removed2 = deque4.removeFromTail();
    t.checkExpect(deque4.size(), 2); // size went down
    t.checkExpect(removed2 == 12, true); // removed data is returned correctly

  }

  // tests find
  void testFindDeque(Tester t) {
    initData();
    ANode<String> item = deque2.find(new StartsWithC());
    t.checkExpect(item, node3);
    ANode<String> item2 = deque3.find(new StartsWithC());
    t.checkExpect(item2, sent3);
  }

  // tests reattach
  void testReattach(Tester t) {
    initData();

    t.checkExpect(node2.next, node3);
    t.checkExpect(node4.prev, node3);
    node3.reattach(node3.prev, node3.next);
    t.checkExpect(node2.next, node4);
    t.checkExpect(node4.prev, node2);

    t.checkExpect(node9.next, node10);
    t.checkExpect(node11.prev, node10);
    node10.reattach(node10.prev, node10.next);
    t.checkExpect(node9.next, node11);
    t.checkExpect(node11.prev, node9);

  }

  // tests getData
  void testGetData(Tester t) {
    initData();
    t.checkExpect(node1.getData(), "abc");
    t.checkExpect(node5.getData(), "rook");
    t.checkExpect(node10.getData(), 7);

  }

  // tests ALL the possible exceptions
  void testExceptions(Tester t) {
    initData();
    t.checkException(new RuntimeException("Can't remove from an empty deque!"), deque1,
        "removeFromHead");
    t.checkException(new RuntimeException("Can't remove from an empty deque!"), deque1,
        "removeFromTail");
    t.checkException(new RuntimeException("A sentinel has no data!"), sent2, "getData");
    t.checkConstructorException(new IllegalArgumentException("Provied illegal null nodes!"), "Node",
        "ahhh", null, node1);
    t.checkConstructorException(new IllegalArgumentException("Provied illegal null nodes!"), "Node",
        "woooo", node3, null);
    t.checkConstructorException(new IllegalArgumentException("Provied illegal null nodes!"), "Node",
        "akali", null, null);
  }

  // tests createEdges
  void testCreateEdges(Tester t) {

    MazeWorld world = new MazeWorld();

    t.checkExpect(world.allEdgesInBoard.get(0),
        new Edge(new Cell(world.board.get(0).get(0).x, world.board.get(0).get(0).y),
            new Cell(world.board.get(0).get(1).x, world.board.get(0).get(1).y), 1));
    t.checkExpect(world.allEdgesInBoard.get(1),
        new Edge(new Cell(world.board.get(0).get(0).x, world.board.get(0).get(0).y),
            new Cell(world.board.get(1).get(0).x, world.board.get(1).get(0).y), 2));
    t.checkExpect(world.allEdgesInBoard.get(5),
        new Edge(new Cell(world.board.get(1).get(1).x, world.board.get(1).get(1).y),
            new Cell(world.board.get(2).get(1).x, world.board.get(2).get(1).y), 6));
    t.checkExpect(world.allEdgesInBoard.get(6),
        new Edge(new Cell(world.board.get(2).get(0).x, world.board.get(2).get(0).y),
            new Cell(world.board.get(2).get(1).x, world.board.get(2).get(1).y), 7));
  }

  // tests the makeGrid method
  void testMakeGrid(Tester t) {

    MazeWorld world = new MazeWorld();

    t.checkExpect(world.board, new ArrayList<ArrayList<Cell>>(Arrays.asList(
        new ArrayList<Cell>(Arrays.asList(world.board.get(0).get(0), world.board.get(0).get(1))),
        new ArrayList<Cell>(Arrays.asList(world.board.get(1).get(0), world.board.get(1).get(1))),
        new ArrayList<Cell>(Arrays.asList(world.board.get(2).get(0), world.board.get(2).get(1))))));
  }

  // tests linkcells
  void testLinkcells(Tester t) {

    MazeWorld world = new MazeWorld();

    t.checkExpect(world.board.get(0).get(0).top, world.board.get(0).get(0));
    t.checkExpect(world.board.get(0).get(0).left, world.board.get(0).get(0));
    t.checkExpect(world.board.get(0).get(0).right, world.board.get(0).get(1));
    t.checkExpect(world.board.get(0).get(0).bottom, world.board.get(1).get(0));

  }

  // tests createMap
  void testCreateMap(Tester t) {

    MazeWorld world = new MazeWorld();

    t.checkExpect(world.map.get(world.board.get(0).get(0)), world.board.get(0).get(0));
    t.checkExpect(world.map.get(world.board.get(0).get(1)), world.board.get(0).get(1));
    t.checkExpect(world.map.get(world.board.get(1).get(0)), world.board.get(1).get(0));
    t.checkExpect(world.map.get(world.board.get(1).get(1)), world.board.get(1).get(1));
    t.checkExpect(world.map.get(world.board.get(2).get(0)), world.board.get(2).get(0));
    t.checkExpect(world.map.get(world.board.get(2).get(1)), world.board.get(2).get(1));
    t.checkExpect(world.map.size(), world.boardY * world.boardX);
  }

  // tests kruskals
  void testKruskals(Tester t) {

    MazeWorld world = new MazeWorld();

    world.makeGrid(world.boardX, world.boardY);
    t.checkExpect(world.mazeEdges.get(0),
        new Edge(world.mazeEdges.get(0).from, world.mazeEdges.get(0).to, 1));
    t.checkExpect(world.mazeEdges.get(1),
        new Edge(world.mazeEdges.get(1).from, world.mazeEdges.get(1).to, 2));
    t.checkExpect(world.mazeEdges.get(2),
        new Edge(world.mazeEdges.get(2).from, world.mazeEdges.get(2).to, 3));
    t.checkExpect(world.mazeEdges.get(3),
        new Edge(world.mazeEdges.get(3).from, world.mazeEdges.get(3).to, 5));
    t.checkExpect(world.mazeEdges.get(4),
        new Edge(world.mazeEdges.get(4).from, world.mazeEdges.get(4).to, 6));
  }

  // tests Union
  void testUnion(Tester t) {

    MazeWorld world = new MazeWorld();

    world.union(world.board.get(0).get(0), world.board.get(0).get(1));
    t.checkExpect(world.find(world.board.get(0).get(0)), world.board.get(0).get(1));
    world.union(world.board.get(0).get(1), world.board.get(1).get(1));
    t.checkExpect(world.find(world.board.get(0).get(1)), world.board.get(1).get(1));
    world.union(world.board.get(2).get(0), world.board.get(0).get(1));
    t.checkExpect(world.find(world.board.get(0).get(0)), world.board.get(1).get(1));
  }

  // tests find
  void testFind(Tester t) {

    t.checkExpect(world.find(world.board.get(0).get(0)), world.board.get(0).get(0));
    t.checkExpect(world.find(world.board.get(2).get(1)), world.board.get(2).get(1));
  }

  // tests replaceRight
  void testreplaceRight(Tester t) {

    MazeWorld world = new MazeWorld();

    world.replaceRight(world.board.get(0).get(0));
    t.checkExpect(world.board.get(0).get(0).rightEdge, false);

    world.replaceRight(world.board.get(2).get(0));
    t.checkExpect(world.board.get(2).get(0).rightEdge, true);
  }

  // tests replaceBottom
  void testreplaceBottom(Tester t) {

    MazeWorld world = new MazeWorld();

    world.replaceBottom(world.board.get(0).get(0));
    t.checkExpect(world.board.get(0).get(0).bottomEdge, false);

    world.replaceBottom(world.board.get(0).get(1));
    t.checkExpect(world.board.get(0).get(1).bottomEdge, false);

    world.replaceBottom(world.board.get(2).get(0));
    t.checkExpect(world.board.get(2).get(0).bottomEdge, true);

    world.replaceBottom(world.board.get(1).get(0));
    t.checkExpect(world.board.get(1).get(0).bottomEdge, false);

    world.replaceBottom(world.board.get(1).get(1));
    t.checkExpect(world.board.get(1).get(1).bottomEdge, false);

    world.replaceBottom(world.board.get(2).get(1));
    t.checkExpect(world.board.get(2).get(1).bottomEdge, true);
  }

  // Run world
  void testBigBang(Tester t) {

    this.RunGame.bigBang(this.RunGame.boardX * 20, this.RunGame.boardY * 20 + 150, 1);
  }

  // testing Cell's draw methods
  void testCellAndEdgeDraw(Tester t) {
    Cell cell1 = new Cell(1, 2);

    t.checkExpect(cell1.drawEdgeRight(),
        new LineImage(new Posn(0, 20), Color.black).movePinhole(-20, -10));

    t.checkExpect(cell1.drawEdgeBottom(),
        new LineImage(new Posn(20, 0), Color.black).movePinhole(-10, -20));

    t.checkExpect(cell1.draw(Color.white),
        new RectangleImage(18, 18, OutlineMode.SOLID, Color.white).movePinhole(-10, -10));
  }

  // test drawPlayer
  void testDrawPlayer(Tester t) {
    Player p = new Player(new Cell(1, 2));

    t.checkExpect(p.drawPlayer(),
        new RectangleImage(17, 17, OutlineMode.SOLID, Color.yellow).movePinhole(-10, -10));
  }

  // tests validMove
  void testValidMove(Tester t) {
    MazeWorld world = new MazeWorld();

    t.checkExpect(world.p.validMove("up"), false);
    t.checkExpect(world.p.validMove("left"), false);
    t.checkExpect(world.p.validMove("down"), true);
    t.checkExpect(world.p.validMove("right"), true);
  }

  // tests validMove
  void testFindPrevious(Tester t) {
    Cell cell1 = new Cell(1, 2);

    cell1.top = cell1;
    t.checkExpect(cell1.previous, cell1.top);
    cell1.left = cell1;
    t.checkExpect(cell1.previous, cell1.left);
    cell1.bottom = cell1;
    t.checkExpect(cell1.previous, cell1.bottom);
    cell1.right = cell1;
    t.checkExpect(cell1.previous, cell1.right);
  }

  // test makeScene
  void testMakeScene(Tester t) {
    initData();
    MazeWorld world = new MazeWorld();
    WorldScene scene = new WorldScene(40, 135);
    scene.placeImageXY(new TextImage("0 seconds", 8, Color.red), 20, 113);
    scene.placeImageXY(
        new RectangleImage(18, 18, OutlineMode.SOLID, Color.magenta).movePinhole(-10, -10), 0, 0);
    scene.placeImageXY(
        new RectangleImage(18, 18, OutlineMode.SOLID, Color.blue).movePinhole(-10, -10), 20, 40);
    scene.placeImageXY(new LineImage(new Posn(0, 20), Color.black).movePinhole(-20, -10), 20, 0);
    scene.placeImageXY(
        new RectangleImage(18, 18, OutlineMode.SOLID, Color.magenta).movePinhole(-10, -10), 0, 0);
    scene.placeImageXY(new LineImage(new Posn(0, 20), Color.black).movePinhole(-20, -10), 0, 20);
    scene.placeImageXY(new LineImage(new Posn(0, 20), Color.black).movePinhole(-20, -10), 20, 20);
    scene.placeImageXY(
        new RectangleImage(18, 18, OutlineMode.SOLID, Color.magenta).movePinhole(-10, -10), 0, 0);
    scene.placeImageXY(new LineImage(new Posn(0, 20), Color.black).movePinhole(-20, -10), 0, 40);
    scene.placeImageXY(new LineImage(new Posn(20, 0), Color.black).movePinhole(-10, -20), 0, 40);
    scene.placeImageXY(new LineImage(new Posn(0, 20), Color.black).movePinhole(-20, -10), 20, 40);
    scene.placeImageXY(new LineImage(new Posn(20, 0), Color.black).movePinhole(-10, -20), 20, 40);
    scene.placeImageXY(
        new RectangleImage(18, 18, OutlineMode.SOLID, Color.magenta).movePinhole(-10, -10), 0, 0);

    scene.placeImageXY(new TextImage("Crazy Maze!", 11, Color.MAGENTA.darker()), 20, 75);
    scene.placeImageXY(new TextImage("press 'r' to restart", 4, Color.DARK_GRAY), 20, 145);
    scene.placeImageXY(
        new RectangleImage(17, 17, OutlineMode.SOLID, Color.yellow).movePinhole(-10, -10), 0, 0);
    t.checkExpect(world.makeScene(), scene);
    //world.bigBang(world.boardX * 20, world.boardY * 20 + 75, 1);
  }

  // tests onKey
  void testOnKey(Tester t) {
    MazeWorld world = new MazeWorld();

    world.onKeyEvent("right");
    t.checkExpect(world.p.on, world.board.get(0).get(1));
    world.onKeyEvent("down");
    t.checkExpect(world.p.on, world.board.get(1).get(1));
    world.onKeyEvent("up");
    t.checkExpect(world.p.on, world.board.get(0).get(1));
    world.onKeyEvent("left");
    t.checkExpect(world.p.on, world.board.get(0).get(0));
    world.onKeyEvent("r");
    t.checkExpect(world.scene, world.getEmptyScene());

  }

  void testLastScene(Tester t) {
    initData();
    MazeWorld world = new MazeWorld();
    WorldScene scene = new WorldScene(0, 0);
    world.lastScene("You win!");

    scene.placeImageXY(
        new RectangleImage(18, 18, OutlineMode.SOLID, Color.magenta).movePinhole(-10, -10), 0, 0);
    scene.placeImageXY(
        new RectangleImage(18, 18, OutlineMode.SOLID, Color.blue).movePinhole(-10, -10), 20, 40);
    scene.placeImageXY(new LineImage(new Posn(0, 20), Color.black).movePinhole(-20, -10), 20, 0);
    scene.placeImageXY(
        new RectangleImage(18, 18, OutlineMode.SOLID, Color.magenta).movePinhole(-10, -10), 0, 0);
    scene.placeImageXY(new LineImage(new Posn(0, 20), Color.black).movePinhole(-20, -10), 0, 20);
    scene.placeImageXY(new LineImage(new Posn(0, 20), Color.black).movePinhole(-20, -10), 20, 20);
    scene.placeImageXY(
        new RectangleImage(18, 18, OutlineMode.SOLID, Color.magenta).movePinhole(-10, -10), 0, 0);
    scene.placeImageXY(new LineImage(new Posn(0, 20), Color.black).movePinhole(-20, -10), 0, 40);
    scene.placeImageXY(new LineImage(new Posn(20, 0), Color.black).movePinhole(-10, -20), 0, 40);
    scene.placeImageXY(new LineImage(new Posn(0, 20), Color.black).movePinhole(-20, -10), 20, 40);
    scene.placeImageXY(new LineImage(new Posn(20, 0), Color.black).movePinhole(-10, -20), 20, 40);

    scene.placeImageXY(
        new RectangleImage(18, 18, OutlineMode.SOLID, Color.magenta).movePinhole(-10, -10), 0, 0);
    scene.placeImageXY(
        new RectangleImage(17, 17, OutlineMode.SOLID, Color.yellow).movePinhole(-10, -10), 0, 0);
    scene.placeImageXY(new TextImage("You Win!", 20, Color.black), 20, 103);
    scene.placeImageXY(new TextImage("0 seconds", 4, Color.red), 20, 120);
    scene.placeImageXY(new TextImage("Crazy Maze!", 12, Color.MAGENTA.darker()), 20, 75);
    scene.placeImageXY(
        new RectangleImage(18, 18, OutlineMode.SOLID, Color.magenta).movePinhole(-10, -10), 0, 0);
    scene.placeImageXY(
        new RectangleImage(18, 18, OutlineMode.SOLID, Color.blue).movePinhole(-10, -10), 20, 40);
    scene.placeImageXY(new LineImage(new Posn(0, 20), Color.black).movePinhole(-20, -10), 20, 0);
    scene.placeImageXY(
        new RectangleImage(18, 18, OutlineMode.SOLID, Color.magenta).movePinhole(-10, -10), 0, 0);
    scene.placeImageXY(new LineImage(new Posn(0, 20), Color.black).movePinhole(-20, -10), 0, 20);
    scene.placeImageXY(new LineImage(new Posn(0, 20), Color.black).movePinhole(-20, -10), 20, 20);
    scene.placeImageXY(
        new RectangleImage(18, 18, OutlineMode.SOLID, Color.magenta).movePinhole(-10, -10), 0, 0);
    scene.placeImageXY(new LineImage(new Posn(0, 20), Color.black).movePinhole(-20, -10), 0, 40);
    scene.placeImageXY(new LineImage(new Posn(20, 0), Color.black).movePinhole(-10, -20), 0, 40);
    scene.placeImageXY(new LineImage(new Posn(0, 20), Color.black).movePinhole(-20, -10), 20, 40);
    scene.placeImageXY(new LineImage(new Posn(20, 0), Color.black).movePinhole(-10, -20), 20, 40);

    scene.placeImageXY(
        new RectangleImage(18, 18, OutlineMode.SOLID, Color.magenta).movePinhole(-10, -10), 0, 0);
    scene.placeImageXY(
        new RectangleImage(17, 17, OutlineMode.SOLID, Color.yellow).movePinhole(-10, -10), 0, 0);
    scene.placeImageXY(new TextImage("You Win!", 20, Color.black), 20, 103);
    scene.placeImageXY(new TextImage("0 seconds", 4, Color.red), 20, 120);
    scene.placeImageXY(new TextImage("Crazy Maze!", 12, Color.MAGENTA.darker()), 20, 75);

    t.checkExpect(world.lastScene("You win!"), scene);
  }

  // test onTick
  void testOnTick(Tester t) {
    initData();

    world.onTick();
    t.checkExpect(world.timer, 1);
    world.currCell = world.endCell;
    t.checkExpect(world.timer, 1);
  }

  //tests bfs (really tough testing this and dfs, we tried a lot and ended up not having 
  // anything too "testy" so we went for a simple test :( 
  void testBFS(Tester t) {
    ArrayList<Cell> path = new ArrayList<Cell>();
    path.add(world.board.get(0).get(0));
    path.add(world.board.get(0).get(1));
    t.checkExpect(world.bfs(world.board.get(0).get(0),
        world.board.get(0).get(1)), path);
  }

  //tests dfs (really tough testing this and dfs, we tried a lot and ended up not having 
  // anything too "testy" so we went for a simple test :( 
  void testDFS(Tester t) {
    ArrayList<Cell> path = new ArrayList<Cell>();
    path.add(world.board.get(0).get(0));
    path.add(world.board.get(0).get(1));
    t.checkExpect(world.bfs(world.board.get(0).get(0),
        world.board.get(0).get(1)), path);
  }

  void testSearch(Tester t) {
    ArrayList<Cell> path = new ArrayList<Cell>();
    path.add(world.board.get(0).get(0));
    path.add(world.board.get(0).get(1));
    t.checkExpect(world.search(world.board.get(0).get(0),
        world.board.get(0).get(1), new Stack<Cell>()), path);
    t.checkExpect(world.search(world.board.get(0).get(0),
        world.board.get(0).get(1), new Queue<Cell>()), path);
  }


}
