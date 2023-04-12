package edu.wpi.teamA.database.DAOImps;

import edu.wpi.teamA.database.Connection.DBConnectionProvider;
import edu.wpi.teamA.database.Interfaces.INodeDAO;
import edu.wpi.teamA.database.ORMclasses.Node;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class NodeDAOImp implements IDataBase, INodeDAO {
  ArrayList<Node> NodeArray;

  static DBConnectionProvider nodeProvider = new DBConnectionProvider();

  public NodeDAOImp(ArrayList<Node> NodeArray) {
    this.NodeArray = NodeArray;
  }

  public NodeDAOImp() {
    this.NodeArray = new ArrayList<Node>();
  }

  // ResultSet

  public static ArrayList<Node> loadNodesFromCSV(String filePath) {
    ArrayList<Node> nodes = new ArrayList<>();

    try {
      BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
      csvReader.readLine(); // Skip the header line
      String row;

      while ((row = csvReader.readLine()) != null) {
        String[] data = row.split(",");

        int nodeID = Integer.parseInt(data[0]);
        int xcoord = Integer.parseInt(data[1]);
        int ycoord = Integer.parseInt(data[2]);
        String floor = data[3];
        String building = data[4];

        Node node = new Node(nodeID, xcoord, ycoord, floor, building);
        nodes.add(node);
      }

      csvReader.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return nodes;
  }

  public static ArrayList<Node> Import(String filePath) {
    ArrayList<Node> NodeArray = loadNodesFromCSV(filePath);

    try {
      BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
      csvReader.readLine();
      String row;

      String sqlCreateNode =
          "Create Table if not exists \"Prototype2_schema\".\"Node\""
              + "(nodeid   int PRIMARY KEY,"
              + "xcoord    int,"
              + "ycoord    int,"
              + "floor     Varchar(600),"
              + "building  Varchar(600))";
      Statement stmtNode = nodeProvider.createConnection().createStatement();
      stmtNode.execute(sqlCreateNode);

      while ((row = csvReader.readLine()) != null) {
        String[] data = row.split(",");

        PreparedStatement ps =
            nodeProvider
                .createConnection()
                .prepareStatement(
                    "INSERT INTO \"Prototype2_schema\".\"Node\" VALUES (?, ?, ?, ?, ?)");
        ps.setInt(1, Integer.parseInt(data[0]));
        ps.setInt(2, Integer.parseInt(data[1]));
        ps.setInt(3, Integer.parseInt(data[2]));
        ps.setString(4, data[3]);
        ps.setString(5, data[4]);
        ps.executeUpdate();
      }
      csvReader.close();
    } catch (SQLException | IOException e) {

      throw new RuntimeException(e);
    }
    return NodeArray;
  }

  public static void Export(String folderExportPath) {
    try {
      String newFile = folderExportPath + "/Node.csv";
      Statement st = nodeProvider.createConnection().createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Prototype2_schema\".\"Node\"");

      FileWriter csvWriter = new FileWriter(newFile);

      csvWriter.append("nodeid,xcoord,ycoord,floor,building\n");

      while (rs.next()) {
        csvWriter.append((rs.getInt("nodeid")) + (","));
        csvWriter.append((rs.getInt("xcoord")) + (","));
        csvWriter.append((rs.getInt("ycoord")) + (","));
        csvWriter.append(rs.getString("floor")).append(",");
        csvWriter.append(rs.getString("building")).append("\n");
      }

      csvWriter.flush();
      csvWriter.close();

      System.out.println("Node table exported to Node.csv");

    } catch (SQLException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public ArrayList<Node> loadNodesFromDatabase() {
    ArrayList<Node> nodes = new ArrayList<>();

    try {
      Statement st = nodeProvider.createConnection().createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM \"Prototype2_schema\".\"Node\"");

      while (rs.next()) {
        int nodeID = rs.getInt("nodeID");
        int xcoord = rs.getInt("xcoord");
        int ycoord = rs.getInt("ycoord");
        String floor = rs.getString("floor");
        String building = rs.getString("building");

        Node node = new Node(nodeID, xcoord, ycoord, floor, building);
        nodes.add(node);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return nodes;
  }

  public void Add(int nodeID, int xcoord, int ycoord, String floor, String building) {
    /** Insert new node object to the existing node table */
    try {

      PreparedStatement ps =
          nodeProvider
              .createConnection()
              .prepareStatement("INSERT INTO Prototype2_schema.\"Node\" VALUES (?, ?, ?, ?, ?)");
      ps.setInt(1, nodeID);
      ps.setInt(2, xcoord);
      ps.setInt(3, ycoord);
      ps.setString(4, floor);
      ps.setString(5, building);
      ps.executeUpdate();

      NodeArray.add(new Node(nodeID, xcoord, ycoord, floor, building));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void Delete(int nodeID) {
    /** delete one of the node according to the nodeID, also delete the node from the arraylist */
    try {
      PreparedStatement ps =
          nodeProvider
              .createConnection()
              .prepareStatement("DELETE FROM Prototype2_schema.\"Node\" WHERE nodeID = ?");
      ps.setInt(1, nodeID);
      ps.executeUpdate();

      NodeArray.removeIf(node -> node.getNodeID().equals(nodeID));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void Update(int nodeID, int xcoord, int ycoord, String floor, String building) {
    /** update the node fields in the database and arraylist according to the inserts */
    try {

      PreparedStatement ps =
          nodeProvider
              .createConnection()
              .prepareStatement(
                  "UPDATE Prototype2_schema.\"Node\" SET xcoord = ?, ycoord = ?, floor = ?, building = ? WHERE nodeID = ?");
      ps.setInt(1, xcoord);
      ps.setInt(2, ycoord);
      ps.setString(3, floor);
      ps.setString(4, building);
      ps.setInt(5, nodeID);
      ps.executeUpdate();

      NodeArray.forEach(
          node -> {
            if (node.getNodeID().equals(nodeID)) {
              node.setXcoord(xcoord);
              node.setYcoord(ycoord);
              node.setFloor(floor);
              node.setBuilding(building);
            }
          });

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Node getNode(int nodeID) {
    Node node = null;
    try {
      PreparedStatement ps =
          nodeProvider
              .createConnection()
              .prepareStatement("SELECT * FROM \"Prototype2_schema\".\"Node\" WHERE nodeID = ?");
      ps.setInt(1, nodeID);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        int xcoord = rs.getInt("xcoord");
        int ycoord = rs.getInt("ycoord");
        String floor = rs.getString("floor");
        String building = rs.getString("building");

        node = new Node(nodeID, xcoord, ycoord, floor, building);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return node;
  }
}
