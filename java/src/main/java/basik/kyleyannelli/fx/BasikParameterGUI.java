package basik.kyleyannelli.fx;

import basik.kyleyannelli.fx.Controllers.BasikParameterController;
import basik.kyleyannelli.fx.Controllers.BasikPedalsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;

public class BasikParameterGUI extends Application {
    private Stage stage;
    private FXMLLoader fxmlLoader;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        this.fxmlLoader = new FXMLLoader(BasikParameterGUI.class.getResource("basik-parameter-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 200, 400);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void setStageTitle(String s) {
        stage.setTitle(s);
    }

    @Override
    public void stop() {
        if(fxmlLoader.getController().getClass() == BasikParameterController.class) {
            ((BasikParameterController) fxmlLoader.getController()).stopTimeline();
        }
    }
}
