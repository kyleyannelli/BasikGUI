package basik.kyleyannelli.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BasikIOGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BasikGUI.class.getResource("basik-io-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 413);
        primaryStage.setTitle("BasikGUI - I/O");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
