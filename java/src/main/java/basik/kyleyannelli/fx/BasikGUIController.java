package basik.kyleyannelli.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class BasikGUIController {
    private final double maxKnobRotate = 140;
    private final double minKnobRotate = -140;
    private double startingMouseY;
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