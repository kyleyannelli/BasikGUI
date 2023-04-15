package basik.kyleyannelli.fx.Components;

import basik.kyleyannelli.Helpers.BasikAPI;
import basik.kyleyannelli.Models.Delay;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BasikDelayComponent extends StackPane implements Initializable {
    private final double minKnobRotation = -155.2, maxKnobRotation = 155.2;
    private Delay delay;
    @FXML
    private ImageView xButtonImage;
    @FXML
    private ImageView mixKnob;
    @FXML
    private ImageView feedbackKnob;
    @FXML
    private ImageView timeKnob;
    public BasikDelayComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "basik-delay-component.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            mixKnob.setOnMouseClicked((MouseEvent event) -> {
                try {
                    new BasikParameterComponent(delay.getName(), delay.getMix(), "Mix").setPedal(this.delay);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            feedbackKnob.setOnMouseClicked((MouseEvent event) -> {
                try {
                    new BasikParameterComponent(delay.getName(), delay.getFeedback(), "Feedback").setPedal(this.delay);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            timeKnob.setOnMouseClicked((MouseEvent event) -> {
                try {
                    new BasikParameterComponent(delay.getName(), delay.getDelaySeconds(), "Time").setPedal(this.delay);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

    public Delay getDelay() {
        return delay;
    }

    public void setDelay(Delay delay) {
        this.delay = delay;
    }

    public void setRemove(boolean doShowRemove) {
        if(doShowRemove) {
            xButtonImage.setDisable(false);
            xButtonImage.setVisible(true);
        } else {
            xButtonImage.setDisable(true);
            xButtonImage.setVisible(false);
        }
    }

    @FXML
    public void removeButtonImage(MouseEvent event) {
        try {
            BasikAPI.deletePedalRequest(this.delay.getPositionInBoard());
            ((ImageView)event.getSource()).setDisable(true);
            ((StackPane)((ImageView)event.getSource()).getParent()).setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setKnob(ImageView knob, float value) {
        if(value > 0.49999F) {
            float normalizedValue = (value - 0.5F)/(1.0F - 0.5F);
            knob.setRotate(maxKnobRotation * (normalizedValue));
        }
        else {
            float normalizedValue = (0.5F - value)/(0.5F);
            knob.setRotate(minKnobRotation * normalizedValue);
        }
    }

    public ImageView getMixKnob() {
        return mixKnob;
    }

    public ImageView getFeedbackKnob() {
        return feedbackKnob;
    }

    public ImageView getTimeKnob() {
        return timeKnob;
    }
}
