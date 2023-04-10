package basik.kyleyannelli.fx.Controllers;

import basik.kyleyannelli.Helpers.PedalLoader;
import basik.kyleyannelli.Models.Pedal;
import basik.kyleyannelli.fx.Components.BasikReverbComponent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BasikPedalsController implements Initializable {
    @FXML
    private HBox pedalHBox;
    private ArrayList<Pedal> pedals;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            pedals = PedalLoader.getPedalboardFromAPI();
        } catch (IOException e) {
//            e.printStackTrace();
            pedals = new ArrayList<>();
            Platform.runLater(() -> {
                pedalHBox.getChildren().add(new BasikReverbComponent());
            });
        }
    }
}
