package edu.wpi.teamA;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.wpi.teamA.database.ORMclasses.Edge;
import edu.wpi.teamA.pathfinding.BFS;
import edu.wpi.teamA.pathfinding.Graph;
import edu.wpi.teamA.pathfinding.GraphNode;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BFSTest {
  Graph graph = new Graph();
  GraphNode nodeA = new GraphNode(11, 0, 0, "", "");
  GraphNode nodeB = new GraphNode(12, 0, 0, "", "");
  GraphNode nodeC = new GraphNode(13, 0, 0, "", "");
  GraphNode nodeD = new GraphNode(14, 0, 0, "", "");
  GraphNode nodeE = new GraphNode(15, 0, 0, "", "");

  @BeforeEach
  protected void setUp() throws Exception {
    System.out.println("Setting it up!");
    GraphNode nodeA = new GraphNode(11, 0, 0, "", "");
    GraphNode nodeB = new GraphNode(12, 0, 0, "", "");
    GraphNode nodeC = new GraphNode(13, 0, 0, "", "");
    GraphNode nodeD = new GraphNode(14, 0, 0, "", "");
    GraphNode nodeE = new GraphNode(15, 0, 0, "", "");
    GraphNode nodeF = new GraphNode(16, 0, 0, "", "");

    graph.addNodeToGraph(nodeA);
    graph.addNodeToGraph(nodeB);
    graph.addNodeToGraph(nodeC);
    graph.addNodeToGraph(nodeD);
    graph.addNodeToGraph(nodeE);
    graph.addNodeToGraph(nodeF);
    Edge edgeAB = new Edge(nodeA.getNodeID(), nodeB.getNodeID());
    Edge edgeBC = new Edge(nodeB.getNodeID(), nodeC.getNodeID());
    Edge edgeCD = new Edge(nodeC.getNodeID(), nodeD.getNodeID());
    Edge edgeAE = new Edge(nodeA.getNodeID(), nodeE.getNodeID());
    Edge edgeDF = new Edge(nodeD.getNodeID(), nodeF.getNodeID());
    // Edge edgeAC = new Edge(nodeA.getNodeID(), nodeC.getNodeID());

    nodeA.addEdge(edgeAB);
    nodeB.addEdge(edgeAB);
    nodeB.addEdge(edgeBC);
    nodeC.addEdge(edgeBC);
    nodeA.addEdge(edgeAE);
    nodeE.addEdge(edgeAE);
    nodeC.addEdge(edgeCD);
    nodeD.addEdge(edgeCD);
    nodeF.addEdge(edgeDF);
    nodeD.addEdge(edgeDF);
    // nodeA.addEdge(edgeAC);
    // nodeC.addEdge(edgeAC);
  }

  @Test
  public void testSetPathBFS() {
    BFS bfs1 = new BFS(graph, 11, 13);
    ArrayList<Integer> expected_path = new ArrayList<Integer>();
    expected_path.add(11);
    expected_path.add(12);
    expected_path.add(13);
    ArrayList<Integer> actual_path = bfs1.getPath();
    assertEquals(expected_path, actual_path);

    BFS bfs2 = new BFS(graph, 13, 11);
    expected_path = new ArrayList<Integer>();
    expected_path.add(13);
    expected_path.add(12);
    expected_path.add(11);
    actual_path = bfs2.getPath();
    assertEquals(expected_path, actual_path);

    BFS bfs3 = new BFS(graph, 11, 15);
    expected_path = new ArrayList<Integer>();
    expected_path.add(11);
    expected_path.add(15);
    actual_path = bfs3.getPath();
    assertEquals(expected_path, actual_path);

    BFS bfs4 = new BFS(graph, 11, 16);
    expected_path = new ArrayList<Integer>();
    expected_path.add(11);
    expected_path.add(12);
    expected_path.add(13);
    expected_path.add(14);
    expected_path.add(16);
    actual_path = bfs4.getPath();
    assertEquals(expected_path, actual_path);
  }

  @Test
  public void testToStringBFS() {
    BFS bfs1 = new BFS(graph, 11, 13);
    String expectedString =
        "Start at node 11, then go to node 12, then go to node 13. You have reached your destination.";
    String actualString = bfs1.toString();
    assertEquals(expectedString, actualString);

    BFS bfs2 = new BFS(graph, 11, 11);
    expectedString = "Wow! You're already there! Good Job!";
    actualString = bfs2.toString();
    assertEquals(expectedString, actualString);
  }
}
