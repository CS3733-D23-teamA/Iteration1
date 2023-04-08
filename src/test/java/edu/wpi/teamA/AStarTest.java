package edu.wpi.teamA;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.wpi.teamA.database.ORMclasses.Edge;
import edu.wpi.teamA.pathfinding.AStar;
import edu.wpi.teamA.pathfinding.Graph;
import edu.wpi.teamA.pathfinding.GraphNode;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AStarTest {
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
    GraphNode nodeB = new GraphNode(12, 0, 5, "", "");
    GraphNode nodeC = new GraphNode(13, 5, 5, "", "");
    GraphNode nodeD = new GraphNode(14, 50, 50, "", "");
    GraphNode nodeE = new GraphNode(15, 10, 10, "", "");
    GraphNode nodeF = new GraphNode(16, 3, 2, "", "");

    graph.addNodeToGraph(nodeA);
    graph.addNodeToGraph(nodeB);
    graph.addNodeToGraph(nodeC);
    graph.addNodeToGraph(nodeD);
    graph.addNodeToGraph(nodeE);
    graph.addNodeToGraph(nodeF);
    Edge edgeAB = new Edge(nodeA.getNodeID(), nodeB.getNodeID());
    Edge edgeBC = new Edge(nodeB.getNodeID(), nodeC.getNodeID());
    Edge edgeCE = new Edge(nodeC.getNodeID(), nodeE.getNodeID());
    Edge edgeAD = new Edge(nodeA.getNodeID(), nodeD.getNodeID());
    Edge edgeDE = new Edge(nodeD.getNodeID(), nodeE.getNodeID());
    Edge edgeBF = new Edge(nodeB.getNodeID(), nodeF.getNodeID());
    // Edge edgeAC = new Edge(nodeA.getNodeID(), nodeC.getNodeID());

    nodeA.addEdge(edgeAB);
    nodeB.addEdge(edgeAB);
    nodeB.addEdge(edgeBC);
    nodeC.addEdge(edgeBC);
    nodeC.addEdge(edgeCE);
    nodeE.addEdge(edgeCE);
    nodeA.addEdge(edgeAD);
    nodeD.addEdge(edgeAD);
    nodeD.addEdge(edgeDE);
    nodeE.addEdge(edgeDE);
    nodeB.addEdge(edgeBF);
    nodeF.addEdge(edgeBF);
    // nodeA.addEdge(edgeAC);
    // nodeC.addEdge(edgeAC);
  }

  @Test
  public void testSetPathAStar() {
    AStar aStar1 = new AStar(graph, 11, 13);
    ArrayList<Integer> expected_path = new ArrayList<Integer>();
    expected_path.add(11);
    expected_path.add(12);
    expected_path.add(13);
    ArrayList<Integer> actual_path = aStar1.getPath();
    assertEquals(expected_path, actual_path);

    AStar aStar2 = new AStar(graph, 13, 11);
    expected_path = new ArrayList<Integer>();
    expected_path.add(13);
    expected_path.add(12);
    expected_path.add(11);
    actual_path = aStar2.getPath();
    assertEquals(expected_path, actual_path);

    AStar aStar3 = new AStar(graph, 11, 15);
    expected_path = new ArrayList<Integer>();
    expected_path.add(11);
    expected_path.add(12);
    expected_path.add(13);
    expected_path.add(15);
    actual_path = aStar3.getPath();
    assertEquals(expected_path, actual_path);

    AStar aStar4 = new AStar(graph, 11, 16);
    expected_path = new ArrayList<Integer>();
    expected_path.add(11);
    expected_path.add(12);
    expected_path.add(16);
    actual_path = aStar4.getPath();
    assertEquals(expected_path, actual_path);
  }

  @Test
  public void testToStringBFS() {
    AStar aStar1 = new AStar(graph, 11, 13);
    String expectedString =
        "Start at node 11, then go to node 12, then go to node 13. You have reached your destination.";
    String actualString = aStar1.toString();
    assertEquals(expectedString, actualString);

    AStar aStar2 = new AStar(graph, 11, 11);
    expectedString = "Wow! You're already there! Good Job!";
    actualString = aStar2.toString();
    assertEquals(expectedString, actualString);
  }
}
