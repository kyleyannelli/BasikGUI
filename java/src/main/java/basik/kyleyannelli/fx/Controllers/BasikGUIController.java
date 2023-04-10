package basik.kyleyannelli.fx.Controllers;

import basik.kyleyannelli.fx.BasikIOGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class BasikGUIController {
    private final double maxKnobRotate = 140;
    private final double minKnobRotate = -140;
    private double startingMouseY;
    private boolean  isStandby = false;
    @FXML
    private ToggleButton distChannelButton;
    @FXML
    private Button openPedalsButton;
    @FXML
    private MenuItem menuQuitItem;

    @FXML
    private ImageView trebleKnobImage;

    @FXML
    private ImageView volumeKnobImage;

    @FXML
    private ImageView bassKnobImage;

    @FXML
    private ImageView cleanAmpImage;

    // quit item
    @FXML
    void handleMenuQuitItem(ActionEvent event) {
        System.exit(0);
    }

    // volume knob adjustment
    @FXML
    void handleDragEnter(MouseEvent event) {
        this.startingMouseY = event.getY();
    }

    @FXML
    void handleMouseMoved(MouseEvent event) {
        rotateKnobBasedOnMouse((ImageView) event.getTarget(), event);
    }

    @FXML
    void onOpenIOMenu(ActionEvent event) throws IOException {
        BasikIOGUI basikIOGUI = new BasikIOGUI();
        basikIOGUI.start(new Stage());
    }

    @FXML
    public void onStandby(ActionEvent event) {
        if((isStandby = !isStandby)) {
            distChannelButton.setDisable(true);
            openPedalsButton.setDisable(true);
        } else {
            distChannelButton.setDisable(false);
            openPedalsButton.setDisable(false);
        }
    }

    private void rotateKnobBasedOnMouse(ImageView imageView, MouseEvent event) {
        if(!(imageView.equals(cleanAmpImage))) {
            double rotate = imageView.getRotate();
            double rotateChange = ((event.getY() - startingMouseY) * .04);
            if((event.getY() - startingMouseY >= 25 || event.getY() - startingMouseY <= -25) &&
                    ((rotate + rotateChange) <= maxKnobRotate && (rotate + rotateChange) >= minKnobRotate)) {
                imageView.setRotate(imageView.getRotate() + rotateChange);
            }
        }
    }
}