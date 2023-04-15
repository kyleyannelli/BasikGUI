package basik.kyleyannelli.fx.Components;

import basik.kyleyannelli.Helpers.BasikAPI;
import basik.kyleyannelli.Models.Chorus;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BasikChorusComponent extends StackPane implements Initializable {
    private Chorus chorus;
    private final float minKnobRotation = -150.0F, maxKnobRotation = 150.0F;
    @FXML
    private ImageView xButtonImage, moveLeftArrowImage, moveRightArrowImage;
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
        Platform.runLater(() -> {
            mixKnobImage.setOnMouseClicked((MouseEvent event) -> {
                try {
                    new BasikParameterComponent(chorus.getName(), chorus.getMix(), "Mix").setPedal(this.chorus);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            depthKnobImage.setOnMouseClicked((MouseEvent event) -> {
                try {
                    new BasikParameterComponent(chorus.getName(), chorus.getDepth(), "Depth").setPedal(this.chorus);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            rateKnobImage.setOnMouseClicked((MouseEvent event) -> {
                try {
                    new BasikParameterComponent(chorus.getName(), chorus.getRateHz(), "Rate").setPedal(this.chorus);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
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

    public Chorus getChorus() {
        return chorus;
    }

    public void setChorus(Chorus chorus) {
        this.chorus = chorus;
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

    public void setArrows(boolean doShowArrows) {
        if(doShowArrows) {
            if(this.chorus.isLastInChain()) {
                moveLeftArrowImage.setDisable(false);
                moveLeftArrowImage.setVisible(true);
            } else if(this.chorus.getPositionInBoard() == 0) {
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
            this.chorus.sendAPIUpdate(this.chorus.getPositionInBoard() - 1);
            ((ImageView)event.getSource()).setDisable(true);
            ((StackPane)((ImageView)event.getSource()).getParent()).setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void moveRight(MouseEvent event) {
        try {
            this.chorus.sendAPIUpdate(this.chorus.getPositionInBoard() + 1);
            ((ImageView)event.getSource()).setDisable(true);
            ((StackPane)((ImageView)event.getSource()).getParent()).setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void removeButtonImage(MouseEvent event) {
        try {
            BasikAPI.deletePedalRequest(this.chorus.getPositionInBoard());
            ((ImageView)event.getSource()).setDisable(true);
            ((StackPane)((ImageView)event.getSource()).getParent()).setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
