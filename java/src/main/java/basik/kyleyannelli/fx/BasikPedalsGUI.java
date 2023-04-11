package basik.kyleyannelli.fx;

import basik.kyleyannelli.fx.Controllers.BasikPedalsController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;

public class BasikPedalsGUI extends Application {
    private FXMLLoader fxmlLoader;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        fxmlLoader = new FXMLLoader(BasikGUI.class.getResource("basik-pedals-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 413);
        JMetro jMetro = new JMetro(scene, Style.DARK);
        stage.setTitle("BasikGUI - Pedals");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                stop();
            }
        });
    }

    @Override
    public void stop() {
        if(fxmlLoader.getController().getClass() == BasikPedalsController.class) {
            ((BasikPedalsController) fxmlLoader.getController()).stopTimeline();
        }
    }
}
