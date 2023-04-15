package basik.kyleyannelli.fx.Components;

import basik.kyleyannelli.Helpers.BasikAPI;
import basik.kyleyannelli.Models.Distortion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BasikDistortionComponent extends StackPane implements Initializable {
    private final float minKnobRotation = -144.0F, maxKnobRotation = 144F;
    @FXML
    private ImageView xButtonImage, moveLeftArrowImage, moveRightArrowImage;;
    @FXML
    private ImageView toneKnobImage;
    @FXML
    private ImageView levelKnobImage;
    @FXML
    private ImageView distKnobImage;
    private ImageView basePedal;
    private Distortion distortion;

    public BasikDistortionComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "basik-distortion-component.fxml"));
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
        distKnobImage.setOnMouseClicked((MouseEvent event) -> {
            try {
                new BasikParameterComponent(distortion.getName(), distortion.getGainPercent(), "Gain").setPedal(this.distortion);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setKnob(ImageView knob, float value) {
        if(value > 0.49999F) {
            float normalizedValue = (value - 0.5F)/(1.0F - 0.5F);
            knob.setRotate(maxKnobRotation * (normalizedValue));
        }
        else {
            float normalizedValue = (0.5F - value)/(0.5F);
            knob.setRotate((minKnobRotation * normalizedValue));
        }
    }

    public ImageView getToneKnobImage() {
        return toneKnobImage;
    }

    public ImageView getLevelKnobImage() {
        return levelKnobImage;
    }

    public ImageView getDistKnobImage() {
        return distKnobImage;
    }

    public void setDistortion(Distortion distortion) {
        this.distortion = distortion;
    }

    public Distortion getDistortion() { return this.distortion; }

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
            BasikAPI.deletePedalRequest(this.distortion.getPositionInBoard());
            ((ImageView)event.getSource()).setDisable(true);
            ((ImageView)event.getSource()).getParent().setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setArrows(boolean doShowArrows) {
        if(doShowArrows) {
            if(this.distortion.isLastInChain()) {
                moveLeftArrowImage.setDisable(false);
                moveLeftArrowImage.setVisible(true);
            } else if(this.distortion.getPositionInBoard() == 0) {
                moveRightArrowImage.setDisable(false);
                moveRightArrowImage.setVisible(true);
            } else {
                moveLeftArrowImage.setDisable(false);
                moveLeftArrowImage.setVisible(true);
                moveRightArrowImage.setDisable(false);
                moveRightArrowImage.setVisible(true);
            }
        } else {
            moveLeftArrowImage.setDisable(true);
            moveLeftArrowImage.setVisible(false);
            moveRightArrowImage.setDisable(true);
            moveRightArrowImage.setVisible(false);
        }
    }

    @FXML
    public void moveLeft(MouseEvent event) {
        try {
            this.distortion.sendAPIUpdate(this.distortion.getPositionInBoard() - 1);
            ((ImageView)event.getSource()).setDisable(true);
            ((StackPane)((ImageView)event.getSource()).getParent()).setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void moveRight(MouseEvent event) {
        try {
            this.distortion.sendAPIUpdate(this.distortion.getPositionInBoard() + 1);
            ((ImageView)event.getSource()).setDisable(true);
            ((StackPane)((ImageView)event.getSource()).getParent()).setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
