package edu.wpi.teamA;

import edu.wpi.teamA.database.DAOImps.UserDAOImp;
import edu.wpi.teamA.navigation.Navigation;
import edu.wpi.teamA.navigation.Screen;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {

  @Setter @Getter private static Stage primaryStage;
  @Setter @Getter private static BorderPane rootPane;

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    /* primaryStage is generally only used if one of your components require the stage to display */
    App.primaryStage = primaryStage;

    final FXMLLoader loader = new FXMLLoader(App.class.getResource("views/Root.fxml"));
    final BorderPane root = loader.load();

    App.rootPane = root;

    UserDAOImp un = new UserDAOImp();
    un.createUserTable();

    final Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
    //    primaryStage.setMaxHeight(720);
    //    primaryStage.setMaxWidth(1280);

    Navigation.navigate(Screen.LOGIN);
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
