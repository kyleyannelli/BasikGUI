package basik.kyleyannelli.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;

public class BasikPedalsGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BasikGUI.class.getResource("basik-pedals-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 413);
        JMetro jMetro = new JMetro(scene, Style.DARK);
        stage.setTitle("BasikGUI - Pedals");
        stage.setScene(scene);
        stage.show();
    }
}
