package edu.wpi.teamA.controllers.Map;

import edu.wpi.teamA.database.DAOImps.EdgeDAOImp;
import edu.wpi.teamA.database.DAOImps.LocNameDAOImp;
import edu.wpi.teamA.database.DAOImps.MoveDAOImp;
import edu.wpi.teamA.database.DAOImps.NodeDAOImp;
import edu.wpi.teamA.database.ORMclasses.LocationName;
import edu.wpi.teamA.database.ORMclasses.Move;
import edu.wpi.teamA.database.ORMclasses.Node;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MapEditorEntity {
  // MapEditorController controller = new MapEditorController();
  private final NodeDAOImp nodeDAO = new NodeDAOImp();
  private final LocNameDAOImp locNameDAO = new LocNameDAOImp();
  private final MoveDAOImp moveDAO = new MoveDAOImp();
  private final EdgeDAOImp edgeDAO = new EdgeDAOImp();
  private ArrayList<Node> nodeArray = nodeDAO.loadNodesFromDatabase();
  private ArrayList<Node> levelGNodeArray = getFloorNodes(nodeArray, "G");
  private ArrayList<Node> levelL1NodeArray = getFloorNodes(nodeArray, "L1");
  private ArrayList<Node> levelL2NodeArray = getFloorNodes(nodeArray, "L2");
  private ArrayList<Node> level1NodeArray = getFloorNodes(nodeArray, "1");
  private ArrayList<Node> level2NodeArray = getFloorNodes(nodeArray, "2");
  private ArrayList<Node> level3NodeArray = getFloorNodes(nodeArray, "3");

  public ArrayList<Node> getFloorNodes(ArrayList<Node> nodeArray, String floor) {
    ArrayList<Node> updatedArray = new ArrayList<Node>();
    for (Node node : nodeArray) {
      if (node.getFloor().equals(floor)) {
        updatedArray.add(node);
      }
    }
    return updatedArray;
  }

  public ArrayList<Node> determineArray(String level) {
    if (level.equals("Level G")) {
      return levelGNodeArray;
    } else if (level.equals("Level L1")) {
      return levelL1NodeArray;
    } else if (level.equals("Level L2")) {
      return levelL2NodeArray;
    } else if (level.equals("Level 1")) {
      return level1NodeArray;
    } else if (level.equals("Level 2")) {
      return level2NodeArray;
    } else if (level.equals("Level 3")) {
      return level3NodeArray;
    } else {
      return null;
    }
  }

  public Circle addCircle(double X, double Y) {
    Circle circle = new Circle();
    circle.setCenterX(X);
    circle.setCenterY(Y);
    circle.setFill(Color.web("0x012D5A"));
    circle.setRadius(2);
    circle.setVisible(true);
    return circle;
  }

  public Node getNodeInfo(int nodeID) {
    return nodeDAO.getNode(nodeID);
  }

  public LocationName getLocationName(int nodeID) {
    Move move = moveDAO.getMove(nodeID);
    return locNameDAO.getLocName(move.getLongName());
  }

  // TODO get largest nodeID
  public void determineAddAction(
      String level,
      int x,
      int y,
      String longName,
      String shortName,
      String floor,
      String building,
      String nodeType) {
    int newNodeID = nodeDAO.getLargestNodeID().getNodeID() + 5;
    //    System.out.println(nodeArray.size());
    //    System.out.println(newNodeID);
    nodeDAO.Add(newNodeID, x, y, floor, building);
    String month = Integer.toString(LocalDate.now().getMonthValue());
    String day = Integer.toString(LocalDate.now().getDayOfMonth());
    if (month.length() == 1) {
      month = "0" + month;
    }
    if (day.length() == 1) {
      day = "0" + day;
    }

    String dateString = month + "/" + day + "/" + LocalDate.now().getYear();
    locNameDAO.Add(longName, shortName, nodeType);
    moveDAO.Add(newNodeID, longName, dateString);
    determineArray(level).add(nodeDAO.getNode(newNodeID));
  }

  public void determineRemoveAction(boolean removeNodeClicked, int nodeID) {
    nodeDAO.Delete(nodeID);
    locNameDAO.Delete(moveDAO.getMove(nodeID).getLongName());
    moveDAO.Delete(nodeID);
  }

  public void determineModifyAction(
      String level,
      int nodeID,
      int x,
      int y,
      String oldLongName,
      String oldShortName,
      String longName,
      String shortName,
      String floor,
      String building,
      String nodeType) {
    nodeDAO.Update(nodeID, x, y, floor, building);
    String month = Integer.toString(LocalDate.now().getMonthValue());
    String day = Integer.toString(LocalDate.now().getDayOfMonth());
    if (month.length() == 1) {
      month = "0" + month;
    }
    if (day.length() == 1) {
      day = "0" + day;
    }

    String dateString = month + "/" + day + "/" + LocalDate.now().getYear();
    locNameDAO.Update(oldLongName, oldShortName, longName, shortName, nodeType);
    moveDAO.Update(nodeID, longName, dateString);
    updateArrays();
  }

  private Node getNodeFromArray(ArrayList<Node> nodes, int nodeID) {
    int i = 0;
    int currentNodeID = nodes.get(0).getNodeID();
    while (currentNodeID != nodeID) {
      i++;
      currentNodeID = nodes.get(i).getNodeID();
    }
    return nodes.get(i);
  }

  private void updateArrays() {
    nodeArray = nodeDAO.loadNodesFromDatabase();
    levelGNodeArray = getFloorNodes(nodeArray, "G");
    levelL1NodeArray = getFloorNodes(nodeArray, "L1");
    levelL2NodeArray = getFloorNodes(nodeArray, "L2");
    level1NodeArray = getFloorNodes(nodeArray, "1");
    level2NodeArray = getFloorNodes(nodeArray, "2");
    level3NodeArray = getFloorNodes(nodeArray, "3");
  }
}
