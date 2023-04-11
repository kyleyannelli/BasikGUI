package basik.kyleyannelli.fx.Controllers;

import basik.kyleyannelli.Helpers.PedalLoader;
import basik.kyleyannelli.Models.*;
import basik.kyleyannelli.fx.Components.BasikChorusComponent;
import basik.kyleyannelli.fx.Components.BasikDistortionComponent;
import basik.kyleyannelli.fx.Components.BasikReverbComponent;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class BasikPedalsController implements Initializable {
    @FXML
    private HBox pedalHBox;
    @FXML
    private Button closeButton, addButton, removeButton, cancelButton, reverbButton, distortionButton, chorusButton;
    private ArrayList<Pedal> pedals;
    private Timeline timeline;
    private boolean isAdding = false;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> updatePedals()));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.playFromStart();
        });
    }

    private void updatePedals() {
        pedalHBox.getChildren().removeAll(pedalHBox.getChildren());
        try {
            pedals = PedalLoader.getPedalboardFromAPI();
        } catch (IOException e) {
            pedals = new ArrayList<>();
        }
        for(Pedal pedal : pedals) {
            if(pedal.getClass() == Reverb.class) {
                pedalHBox.getChildren().add((BasikReverbComponent)pedal.viewize());
            } else if(pedal.getClass() == Chorus.class) {
                pedalHBox.getChildren().add((BasikChorusComponent)pedal.viewize());
            } else if(pedal.getClass() == Distortion.class) {
                pedalHBox.getChildren().add((BasikDistortionComponent)pedal.viewize());
            } else if(pedal.getClass() == Delay.class) {

            } else if(pedal.getClass() == NoiseGate.class) {

            }
        }
    }

    @FXML
    private void toggleButtonOptions(ActionEvent event) {
        toggleOptionsOpposite((isAdding = !isAdding));
    }

    @FXML
    private void addReverbPedal(ActionEvent event) throws IOException {
        putEffectAPIRequest(1);
    }

    @FXML
    private void addDistortionPedal(ActionEvent event) throws IOException {
        putEffectAPIRequest(2);
    }

    @FXML
    private void addChorusPedal(ActionEvent event) throws IOException {
        putEffectAPIRequest(3);
    }

    private void putEffectAPIRequest(int effectNumber) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.custom().build()).build();
        HttpPut request = new HttpPut("http://localhost:30108/effect");
        HttpEntity entity = MultipartEntityBuilder
                .create()
                .addTextBody("desired_position_in_board", "" + pedals.size(), ContentType.APPLICATION_FORM_URLENCODED)
                .addTextBody("effect_number", "" + effectNumber, ContentType.APPLICATION_FORM_URLENCODED)
                .addTextBody("parameters", "mix:0.32,room_size:0.40,damping:0.20", ContentType.APPLICATION_FORM_URLENCODED)
                .build();
        request.setEntity(entity);
        httpClient.execute(request);
    }

    private void toggleOptionsOpposite(boolean b) {
        fullToggleButton(closeButton, !b);
        fullToggleButton(addButton, !b);
        fullToggleButton(removeButton, !b);

        fullToggleButton(cancelButton, b);
        fullToggleButton(reverbButton, b);
        fullToggleButton(distortionButton, b);
        fullToggleButton(chorusButton, b);
    }

    private void fullToggleButton(Button bu, boolean bo) {
        bu.setDisable(!bo);
        bu.setVisible(bo);
    }

    public void stopTimeline() {
        timeline.stop();
        timeline = null;
    }
}
