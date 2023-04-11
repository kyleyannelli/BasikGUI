package basik.kyleyannelli.fx.Components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BasikChorusComponent extends StackPane implements Initializable {
    private final float minKnobRotation = -150.0F, maxKnobRotation = 150.0F;
    @FXML
    private ImageView mixKnobImage;
    @FXML
    private ImageView depthKnobImage;
    @FXML
    private ImageView feedbackKnobImage;
    @FXML
    private ImageView rateKnobImage;
    private ImageView basePedal;
    public BasikChorusComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "basik-chorus-component.fxml"));
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

    }

    public void setKnob(ImageView knob, float value) {
        if(value > 0.49999F) {
            float normalizedValue = (value - 0.5F)/(1.0F - 0.5F);
            knob.setRotate(maxKnobRotation * (normalizedValue));
        }
        else {
            float normalizedValue = (value)/(0.5F);
            knob.setRotate(minKnobRotation * normalizedValue);
        }
    }

    public ImageView getMixKnobImage() {
        return mixKnobImage;
    }

    public ImageView getDepthKnobImage() {
        return depthKnobImage;
    }

    public ImageView getFeedbackKnobImage() {
        return feedbackKnobImage;
    }

    public ImageView getRateKnobImage() { return rateKnobImage; }
}
