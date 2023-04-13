package basik.kyleyannelli.fx.Controllers;

import com.gluonhq.charm.glisten.control.ProgressBar;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.robot.Robot;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class BasikParameterController implements Initializable {
    private Robot robot;
    private Timeline timeline;
    private final int height = 400;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Text parameterNameText, percentageText;
    private boolean isMouseDown;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        robot = new Robot();
        isMouseDown = false;
        Platform.runLater(() -> {
            timeline = new Timeline(new KeyFrame(Duration.seconds(0.05), e -> updatePercentIfMouseDown()));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.playFromStart();
        });
    }

    private void updatePercentIfMouseDown() {
        if(isMouseDown) {
            double progress = (1.0 - (robot.getMouseY() -  progressBar.getScene().getWindow().getY())/height);
            if(progress > 1.0) {
                progress = 1.0;
            } else if(progress < 0.0) {
                progress = 0.0;
            }
            progressBar.setProgress(progress);
            this.percentageText.setText("" + (int)(progress*100) + "%");
        }
    }

    @FXML
    private void onMouseDown(MouseEvent event) {
        isMouseDown = true;
        progressBar.setProgress((1.0 - event.getY()/height));
        this.percentageText.setText("" + (int)(progressBar.getProgress()*100) + "%");
    }

    @FXML
    private void onMouseUp(MouseEvent event) {
        isMouseDown = false;
    }

    public void stopTimeline() {
        timeline.stop();
        timeline = null;
    }

    public void setParameterNameText(String text) {
        parameterNameText.setText(text);
    }
}
