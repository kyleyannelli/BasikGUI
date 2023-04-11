package basik.kyleyannelli.fx.Components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BasikReverbComponent extends StackPane implements Initializable {
    private final float minKnobRotation = -154.0F, maxKnobRotation = 154F;
    @FXML
    private ImageView mixKnob;
    @FXML
    private ImageView widthKnob;
    @FXML
    private ImageView dampingKnob;
    @FXML
    private ImageView sizeKnob;
    private ImageView basePedal;
    public BasikReverbComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "basik-reverb-component.fxml"));
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

    public ImageView getMixKnob() {
        return mixKnob;
    }

    public ImageView getWidthKnob() {
        return widthKnob;
    }

    public ImageView getSizeKnob() {
        return sizeKnob;
    }

    public ImageView getDampingKnob() {
        return dampingKnob;
    }
}