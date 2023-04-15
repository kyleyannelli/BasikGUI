package basik.kyleyannelli.fx.Controllers;

import basik.kyleyannelli.Helpers.PedalLoader;
import basik.kyleyannelli.Models.*;
import basik.kyleyannelli.fx.Components.BasikChorusComponent;
import basik.kyleyannelli.fx.Components.BasikDelayComponent;
import basik.kyleyannelli.fx.Components.BasikDistortionComponent;
import basik.kyleyannelli.fx.Components.BasikReverbComponent;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
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
    private Button closeButton, addButton, cancelButton, reverbButton, distortionButton, chorusButton, delayButton;
    @FXML
    private ToggleButton removeToggleButton, reorderToggleButton;
    private ArrayList<Pedal> pedals;
    private Timeline timeline;
    private boolean isAdding = false;
    private boolean doShowRemove, doShowArrows;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        doShowRemove = false;
        doShowArrows = false;
        Platform.runLater(() -> {
            timeline = new Timeline(new KeyFrame(Duration.seconds(0.65), e -> updatePedals()));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.playFromStart();
        });
    }

    private void updatePedals() {
        boolean isChanged =  false;
        try {
            ArrayList<Pedal> newPedals = PedalLoader.getPedalboardFromAPI();
            if(!newPedals.equals(pedals) || pedals == null) {
                isChanged = true;
                pedals = newPedals;
            }
        } catch (IOException e) {
            pedals = new ArrayList<>();
        }
        if(isChanged) {
            pedalHBox.getChildren().removeAll(pedalHBox.getChildren());
            for(Pedal pedal : pedals) {
                if(pedal.getClass() == Reverb.class) {
                    BasikReverbComponent basikReverbComponent = (BasikReverbComponent)pedal.viewize();
                    basikReverbComponent.setRemove(doShowRemove);
                    basikReverbComponent.setArrows(doShowArrows);
                    pedalHBox.getChildren().add(basikReverbComponent);
                } else if(pedal.getClass() == Chorus.class) {
                    BasikChorusComponent basikChorusComponent = (BasikChorusComponent)pedal.viewize();
                    basikChorusComponent.setRemove(doShowRemove);
                    basikChorusComponent.setArrows(doShowArrows);
                    pedalHBox.getChildren().add(basikChorusComponent);
                } else if(pedal.getClass() == Distortion.class) {
                    BasikDistortionComponent basikDistortionComponent = (BasikDistortionComponent)pedal.viewize();
                    basikDistortionComponent.setRemove(doShowRemove);
                    basikDistortionComponent.setArrows(doShowArrows);
                    pedalHBox.getChildren().add(basikDistortionComponent);
                } else if(pedal.getClass() == Delay.class) {
                    BasikDelayComponent basikDelayComponent = (BasikDelayComponent)pedal.viewize();
                    basikDelayComponent.setRemove(doShowRemove);
                    basikDelayComponent.setArrows(doShowArrows);
                    pedalHBox.getChildren().add(basikDelayComponent);
                } else if(pedal.getClass() == NoiseGate.class) {

                }
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

    @FXML
    private void addDelayPedal(ActionEvent event) throws IOException {
        putEffectAPIRequest(5);
    }

    @FXML
    private void toggleRemoveFromButton(ActionEvent event) {
        if(doShowRemove) {
            doShowRemove = false;
            reorderToggleButton.setDisable(false);
        }
        else {
            doShowRemove = true;
            reorderToggleButton.setDisable(true);
        }
        if(doShowRemove) {
            pedalHBox.setPadding(new Insets(30,0,0,0));
        } else {
            pedalHBox.setPadding(new Insets(20,0,0,0));
        }
        for(Object o : pedalHBox.getChildren()) {
            if(o.getClass().equals(BasikReverbComponent.class)) {
                ((BasikReverbComponent) o).setRemove(doShowRemove);
            } else if(o.getClass().equals(BasikChorusComponent.class)) {
                ((BasikChorusComponent) o).setRemove(doShowRemove);
            } else if(o.getClass().equals(BasikDistortionComponent.class)) {
                ((BasikDistortionComponent) o).setRemove(doShowRemove);
            } else if(o.getClass().equals(BasikDelayComponent.class)) {
                ((BasikDelayComponent) o).setRemove(doShowRemove);
            }
        }
    }

    @FXML
    private void toggleReorderButton(ActionEvent event) {
        if(doShowArrows) {
            doShowArrows = false;
            removeToggleButton.setDisable(false);
        }
        else {
            doShowArrows = true;
            removeToggleButton.setDisable(true);
        }
        if(doShowArrows) {
            pedalHBox.setPadding(new Insets(10,0,0,0));
        } else {
            pedalHBox.setPadding(new Insets(20,0,0,0));
        }
        for(Object o : pedalHBox.getChildren()) {
            if(o.getClass().equals(BasikReverbComponent.class)) {
                ((BasikReverbComponent) o).setArrows(doShowArrows);
            } else if(o.getClass().equals(BasikChorusComponent.class)) {
                ((BasikChorusComponent) o).setArrows(doShowArrows);
            } else if(o.getClass().equals(BasikDistortionComponent.class)) {
                ((BasikDistortionComponent) o).setArrows(doShowArrows);
            } else if(o.getClass().equals(BasikDelayComponent.class)) {
                ((BasikDelayComponent) o).setArrows(doShowArrows);
            }
        }
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
        removeToggleButton.setDisable(b);removeToggleButton.setVisible(!b);

        fullToggleButton(cancelButton, b);
        fullToggleButton(reverbButton, b);
        fullToggleButton(distortionButton, b);
        fullToggleButton(chorusButton, b);
        fullToggleButton(delayButton, b);
    }

    @FXML
    public void closeScene(ActionEvent event) {
        // get a handle to the stage
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        // do what you have to do
        stage.close();
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
