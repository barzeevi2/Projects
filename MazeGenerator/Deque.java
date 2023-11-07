//import tester.*;
//import java.util.function.Predicate;
//
//
//
//// represents an abstarct node in a deque, can either be a sentinel or a node
//abstract class ANode<T> {
//  ANode<T> next;
//  ANode<T> prev;
//
//  //counts the number of nodes in a "chain of nodes
//  //not including the sentinel node.
//  abstract int size();
//
//  //removes the first node from a "chain" of nodes (deque), throws an exception if used
//  // on sentinel
//  abstract T removeFromHead();
//
//  //removes the last node from a "chain" of nodes (deque), throws an exception if used
//  // on sentinel
//  abstract T removeFromTail();
//
//  //returns the data of a node if its a node, throws an exception if used on sentinel
//  //used only for comfort of testing, would never be called on sentinel!
//  abstract T getData();
//
//  // returns the sentinel if used on it, if used on node, looks for the first
//  // node that returns true from the predicate given, and returns it
//  abstract ANode<T> find(Predicate<T> pred);
//  
//  // re-attaches two given nodes to reference eachother
//  abstract void reattach(ANode<T> first, ANode<T> second);
//}
//
//
////represents the sentinel node in a deque, aka the header
//class Sentinel<T> extends ANode<T> {
//  
//  //constructs a new sentinel, setting its next and prev fields
//  //to this sentinel
//  Sentinel() {
//    this.next = this;
//    this.prev = this;
//  }
//
//
//  public int size() {
//    return 0;
//  }
//
//  public T removeFromHead() {
//    throw new RuntimeException("Can't remove from an empty deque!");
//  }
//
//  //returns the data of a node if its a node, throws an exception if used on sentinel
//  //used only for comfort of testing, would never be called on sentinel!
//  public T getData() {
//    throw new RuntimeException("A sentinel has no data!");
//  }
//
//  public T removeFromTail() {
//    throw new RuntimeException("Can't remove from an empty deque!");
//  }
//
//  public ANode<T> find(Predicate<T> pred) {
//    return this;
//  }
//  
//  //re-attaches two given nodes to reference eachother
//  //ideally should only exist in Node<T>, made abstract for testing purposes!
//  public void reattach(ANode<T> first, ANode<T> second) {
//    first.next = second;
//    second.prev = first;
//  }
//}
//
//
////represents an actual node in a deque, has data first and prev
//class Node<T> extends ANode<T> {
//  T data;
//
//  //constructs a new node given only data, sets next and prev to null
//  Node(T data) {
//    this.data = data;
//    this.next = null;
//    this.prev = null;
//  }
//
//
//
//  //constructs a new node given data, a next node and a prev node.
//  //throws an exception if either of the given nodes is null
//  Node(T data, ANode<T> next, ANode<T> prev) {
//    if (next == null || prev == null) {
//      throw new IllegalArgumentException("Provied illegal null nodes!");
//    }
//    else {
//      this.data = data;
//      this.next = next;
//      this.prev = prev;
//      next.prev = this;
//      prev.next = this;
//    }
//  }
//
//  public int size() {
//    return 1 + this.next.size();
//  }
//
//  public T removeFromHead() {
//    T val = this.data;
//    this.reattach(this.prev, this.next); // "re-attaches" the broken link
//    return val;
//  }
//
//  //returns the data of a node if its a node, throws an exception if used on sentinel
//  //used only for comfort of testing, would never be called on sentinel!
//  public T getData() {
//    return this.data;
//  }
//
//  public T removeFromTail() {
//    T val = this.data;
//    this.reattach(this.prev, this.next); // "re-attaches" the broken link
//    return val;
//  }
//
//  public ANode<T> find(Predicate<T> pred) {
//    if (pred.test(this.data)) {
//      return this;
//    }
//    else {
//      return this.next.find(pred);
//    }
//  }
//
//  // re-attaches two given nodes to reference eachother
//  public void reattach(ANode<T> first, ANode<T> second) {
//    first.next = second;
//    second.prev = first;
//  }
//}
//
////represents a deque (double-ended queue)
//class Deque<T> {
//  Sentinel<T> header;
//
//  //constructs a new deque with a new sentinel
//  Deque() {
//    this.header = new Sentinel<T>();
//  }
//
//  //constructs a new deque using a given sentinel
//  Deque(Sentinel<T> header) {
//    this.header = header;
//  }
//
//
//  //counts the number of nodes in a list Deque, not including the header node. 
//  public int size() {
//    return this.header.next.size();
//  }
//
//  //consumes a value of type T and inserts it at the front of the list.
//  public void addAtHead(T data) {
//    new Node<T>(data, this.header.next, this.header);
//  }
//
//  //consumes a value of type T and inserts it at the tail of this list.
//  public void addAtTail(T data) {
//    new Node<T>(data, this.header, this.header.prev);
//  }
//
//  //removes the first node from this Deque. (throws exception if empty deque)
//  public T removeFromHead() {
//    return this.header.next.removeFromHead();
//  }
//
//  //removes the last node from this Deque. (throws exception if empty deque)
//  public T removeFromTail() {
//    return this.header.prev.removeFromTail();
//  }
//
//  //takes a Predicate<T> and produces the first node in this Deque
//  //for which the given predicate returns true
//  public ANode<T> find(Predicate<T> pred) {
//    return this.header.next.find(pred);
//  }
//}
//
//
////represents a predicate class for testing find
//class StartsWithC implements Predicate<String> {
//  public boolean test(String str) {
//    return str.substring(0, 1).equals("c");
//  }
//}
//
//
//class ExamplesDeque {
//  Deque<String> deque1;
//  Deque<String> deque2;
//  Deque<String> deque3;
//  Deque<Integer> deque4;
//
//  //all the below declarations are not just declarations and are assignments
//  //in order to avoid having these nodes be null to begin with, which
//  // will end in an exception
//  Sentinel<String> sent2 = new Sentinel<String>();
//  ANode<String> node1 = new Node<String>("abc");
//  ANode<String> node2 = new Node<String>("bcd");
//  ANode<String> node3 = new Node<String>("cde");
//  ANode<String> node4 = new Node<String>("def");
//
//
//
//  Sentinel<String> sent3 = new Sentinel<String>();
//  ANode<String> node5 = new Node<String>("rook");
//  ANode<String> node6 = new Node<String>("pawn");
//  ANode<String> node7 = new Node<String>("queen");
//  ANode<String> node8 = new Node<String>("knight");
//
//  Sentinel<Integer> sent4 = new Sentinel<Integer>();
//  ANode<Integer> node9 = new Node<Integer>(10);
//  ANode<Integer> node10 = new Node<Integer>(7);
//  ANode<Integer> node11 = new Node<Integer>(12);
//
//  //initializing data
//  //deque1 = empty deque
//  //deque2 = the (sentinel -> abc -> bcd -> cde -> def ->sentinel) deque
//  //deque3 = the (sentinel -> rook -> pawn -> queen -> knight ->sentinel) deque
//  //deque 4 = the (sentinel -> 10 -> 7 -> 12 ->sentinel) deque
//  void initData() {
//    deque1 = new Deque<String>();
//    sent2 = new Sentinel<String>();
//    node1 = new Node<String>("abc", node2, sent2);
//    node2 = new Node<String>("bcd", node3, node1);
//    node3 = new Node<String>("cde", node4, node2);
//    node4 = new Node<String>("def", sent2, node3);
//    deque2 = new Deque<String>(sent2);
//
//    sent3 = new Sentinel<String>();
//    node5 = new Node<String>("rook", node6, sent3);
//    node6 = new Node<String>("pawn", node7, node5);
//    node7 = new Node<String>("queen", node8, node6);
//    node8 = new Node<String>("knight", sent3, node7);
//    deque3 = new Deque<String>(sent3);
//
//    sent4 = new Sentinel<Integer>();
//    node9 = new Node<Integer>(10, node10, sent4);
//    node10 = new Node<Integer>(7, node11, node9);
//    node11 = new Node<Integer>(12, sent4, node10);
//    deque4 = new Deque<Integer>(sent4);
//  }
//
//  //tests size
//  void testSize(Tester t) {
//    initData();
//
//    t.checkExpect(deque1.size(),0);
//    t.checkExpect(deque2.size(),4);
//    t.checkExpect(deque3.size(),4);
//    t.checkExpect(deque4.size(),3);
//  }
//
//  //tests addathead
//  void testAddAtHead(Tester t) {
//    initData();
//    t.checkExpect(deque1.size(),0);
//    t.checkExpect(deque2.size(),4);
//    deque1.addAtHead("abc");
//    t.checkExpect(deque1.size(),1); // size went up
//    t.checkExpect(deque1.header.next.getData(), "abc"); //first element changed
//    deque2.addAtHead("wow");
//    t.checkExpect(deque2.size(),5); // size went up
//    t.checkExpect(deque2.header.next.getData(), "wow"); //first element changed
//  }
//
//
//  //tests addattail
//  void testAddAtTail(Tester t) {
//    initData();
//    t.checkExpect(deque1.size(),0);
//    t.checkExpect(deque2.size(),4);
//    deque1.addAtTail("abc");
//    t.checkExpect(deque1.size(),1); // size went up
//    t.checkExpect(deque1.header.prev.getData(), "abc"); //last element changed
//    deque2.addAtTail("wow");
//    t.checkExpect(deque2.size(),5); // size went up
//    t.checkExpect(deque2.header.prev.getData(), "wow"); //last element changed
//  }
//
//  //tests removefromhead
//  void testRemoveFromHead(Tester t) {
//    initData();
//    t.checkExpect(deque2.size(),4);
//    t.checkExpect(deque4.size(),3);
//
//    String removed = deque2.removeFromHead();
//    t.checkExpect(deque2.size(),3); // size went down
//    t.checkExpect(removed.equals("abc"), true); //removed data is returned correctly
//
//    int removed2 = deque4.removeFromHead();
//    t.checkExpect(deque4.size(),2); // size went down
//    t.checkExpect(removed2 == 10, true); //removed data is returned correctly
//
//  }
//
//  //tests removefromtail
//  void testRemoveFromTail(Tester t) {
//    initData();
//    t.checkExpect(deque2.size(),4);
//    t.checkExpect(deque4.size(),3);
//
//    String removed = deque2.removeFromTail();
//    t.checkExpect(deque2.size(),3); // size went down
//    t.checkExpect(removed.equals("def"), true); //removed data is returned correctly
//
//    int removed2 = deque4.removeFromTail();
//    t.checkExpect(deque4.size(),2); // size went down
//    t.checkExpect(removed2 == 12, true); //removed data is returned correctly
//
//  }
//
//  //tests find
//  void testFind(Tester t) {
//    initData();
//    ANode<String> item = deque2.find(new StartsWithC());
//    t.checkExpect(item, node3);
//    ANode<String> item2 = deque3.find(new StartsWithC());
//    t.checkExpect(item2, sent3);
//  }
//  
//  //tests reattach
//  void testReattach(Tester t) {
//    initData();
//    
//    t.checkExpect(node2.next, node3);
//    t.checkExpect(node4.prev, node3);
//    node3.reattach(node3.prev, node3.next);
//    t.checkExpect(node2.next, node4);
//    t.checkExpect(node4.prev, node2);
//    
//    t.checkExpect(node9.next, node10);
//    t.checkExpect(node11.prev, node10);
//    node10.reattach(node10.prev, node10.next);
//    t.checkExpect(node9.next, node11);
//    t.checkExpect(node11.prev, node9);
//
//  }
//  
//  //tests getData
//  void testGetData(Tester t) {
//    initData();
//    t.checkExpect(node1.getData(), "abc");
//    t.checkExpect(node5.getData(), "rook");
//    t.checkExpect(node10.getData(), 7);
//    
//  }
//  
//  //tests ALL the possible exceptions
//  void testExceptions(Tester t) {
//    t.checkException(new RuntimeException("Can't remove from an empty deque!"),
//        deque1, "removeFromHead");
//    t.checkException(new RuntimeException("Can't remove from an empty deque!"),
//        deque1, "removeFromTail");
//    t.checkException(new RuntimeException("A sentinel has no data!"),
//        sent2, "getData");
//    t.checkConstructorException(new IllegalArgumentException("Provied illegal null nodes!"),
//        "Node", "ahhh", null, node1);
//    t.checkConstructorException(new IllegalArgumentException("Provied illegal null nodes!"),
//        "Node", "woooo", node3, null);
//    t.checkConstructorException(new IllegalArgumentException("Provied illegal null nodes!"),
//        "Node", "akali", null, null);
//  }
//
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
