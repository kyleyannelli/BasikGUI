package basik.kyleyannelli.fx.Controllers;

import basik.kyleyannelli.fx.BasikIOGUI;
import basik.kyleyannelli.fx.BasikPedalsGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class BasikGUIController {
    private final double maxKnobRotate = 140;
    private final double minKnobRotate = -140;
    private double startingMouseY;
    private boolean  isStandby = false;
    private boolean isDist = false;

    @FXML
    private ImageView distortionAmp, distPreKnob, distLowKnob,
            distMidKnob, distHighKnob, distPostKnob,
            distResKnob, distPresKnob;
    @FXML
    private ImageView reverbKnob, speedKnob, intenKnob;

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
        if(event.getTarget().getClass().equals(ImageView.class)) {
            rotateKnobBasedOnMouse((ImageView) event.getTarget(), event);
        }
    }

    @FXML
    void openIOMenu(ActionEvent event) throws IOException {
        BasikIOGUI basikIOGUI = new BasikIOGUI();
        basikIOGUI.start(new Stage());
    }

    @FXML
    public void openPedalsButton(ActionEvent event) throws IOException {
        BasikPedalsGUI basikPedalsGUI = new BasikPedalsGUI();
        basikPedalsGUI.start(new Stage());
    }

    @FXML
    public void toggleDistortionChannel(ActionEvent event) throws IOException {
        fullToggleImage(cleanAmpImage, isDist);
        fullToggleImage(bassKnobImage, isDist);
        fullToggleImage(trebleKnobImage, isDist);
        fullToggleImage(volumeKnobImage, isDist);
        fullToggleImage(intenKnob, isDist);
        fullToggleImage(speedKnob, isDist);
        fullToggleImage(reverbKnob, isDist);

        fullToggleImage(distortionAmp, !isDist);
        fullToggleImage(distPreKnob, !isDist);
        fullToggleImage(distLowKnob, !isDist);
        fullToggleImage(distMidKnob, !isDist);
        fullToggleImage(distHighKnob, !isDist);
        fullToggleImage(distPostKnob, !isDist);
        fullToggleImage(distResKnob, !isDist);
        fullToggleImage(distPresKnob, !isDist);

        isDist = !isDist;

        putAPIToggleAmp();
    }

    private void putAPIToggleAmp() throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.custom().build()).build();
        HttpPut request = new HttpPut("http://localhost:30108/cli");
        HttpEntity entity = MultipartEntityBuilder
                .create()
                .addTextBody("command", "4", ContentType.APPLICATION_FORM_URLENCODED)
                .build();
        request.setEntity(entity);
        httpClient.execute(request);
    }

    private void fullToggleImage(ImageView imageView, boolean bo) {
        imageView.setDisable(!bo);
        imageView.setVisible(bo);
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