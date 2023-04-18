package basik.kyleyannelli.fx.Components;

import basik.kyleyannelli.Helpers.BasikAPI;
import basik.kyleyannelli.Models.Reverb;
import basik.kyleyannelli.fx.BasikWebComponentGUI;
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

public class BasikReverbComponent extends StackPane implements Initializable {
    private Reverb reverb;
    private final float minKnobRotation = -154.0F, maxKnobRotation = 154F;
    @FXML
    private ImageView xButtonImage, moveLeftArrowImage, moveRightArrowImage, questionMarkImage;
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
        Platform.runLater(() -> {
            questionMarkImage.setOnMousePressed((MouseEvent event) -> {
                try {
                    new BasikWebComponentGUI("https://basik.kyle.city/pedals-songs/" + this.reverb.getName());
                } catch (Exception e) {

                }
            });
            mixKnob.setOnMouseClicked((MouseEvent event) -> {
                try {
                    new BasikParameterComponent(reverb.getName(), reverb.getMix(), "Mix").setPedal(this.reverb);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            dampingKnob.setOnMouseClicked((MouseEvent event) -> {
                try {
                    new BasikParameterComponent(reverb.getName(), reverb.getDamping(), "Damping").setPedal(this.reverb);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            sizeKnob.setOnMouseClicked((MouseEvent event) -> {
                try {
                    new BasikParameterComponent(reverb.getName(), reverb.getRoomSize(), "Size").setPedal(this.reverb);
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

    public void setReverb(Reverb reverb) { this.reverb = reverb; }

    public Reverb getReverb() { return reverb; }

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
            if(this.reverb.isOnlyPedal()) {
                moveLeftArrowImage.setDisable(true);
                moveLeftArrowImage.setVisible(false);
                moveRightArrowImage.setDisable(true);
                moveRightArrowImage.setVisible(false);
            } else if(this.reverb.isLastInChain()) {
                moveLeftArrowImage.setDisable(false);
                moveLeftArrowImage.setVisible(true);
            } else if(this.reverb.getPositionInBoard() == 0) {
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
            this.reverb.sendAPIUpdate(this.reverb.getPositionInBoard() - 1);
            ((ImageView)event.getSource()).setDisable(true);
            ((StackPane)((ImageView)event.getSource()).getParent()).setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void moveRight(MouseEvent event) {
        try {
            this.reverb.sendAPIUpdate(this.reverb.getPositionInBoard() + 1);
            ((ImageView)event.getSource()).setDisable(true);
            ((StackPane)((ImageView)event.getSource()).getParent()).setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void removeButtonImage(MouseEvent event) {
        try {
            BasikAPI.deletePedalRequest(this.reverb.getPositionInBoard());
            ((ImageView)event.getSource()).setDisable(true);
            ((StackPane)((ImageView)event.getSource()).getParent()).setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
