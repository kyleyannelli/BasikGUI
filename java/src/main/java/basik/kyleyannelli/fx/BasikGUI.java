package basik.kyleyannelli.fx;

import basik.kyleyannelli.fx.Preloaders.BasikAPIPreloader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.lang.Thread.sleep;

public class BasikGUI extends Application {
    @Override
    public void init() throws Exception {
//        URL url = new URL("http://localhost:30108/input");
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("GET");
//        int responseCode = 0;
//        while(responseCode != 200) {
//            try {
//                connection.connect();
//                sleep(250);
//                responseCode = connection.getResponseCode();
//            }
//            catch (Exception e) {
//
//            }
//        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BasikGUI.class.getResource("basik-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 413);
        JMetro jMetro = new JMetro(scene, Style.DARK);
        stage.setTitle("BasikGUI");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        System.setProperty("javafx.preloader", BasikAPIPreloader.class.getCanonicalName());
        launch();
    }
}