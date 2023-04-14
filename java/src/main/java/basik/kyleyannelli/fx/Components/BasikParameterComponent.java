package basik.kyleyannelli.fx.Components;

import com.gluonhq.charm.glisten.control.ProgressBar;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.robot.Robot;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BasikParameterComponent extends AnchorPane implements Initializable {
    private Robot robot;
    private Timeline timeline;
    private final int height = 400;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Text parameterNameText, percentageText;
    private boolean isMouseDown;
    public BasikParameterComponent(String effectName, float effectPercent, String parameterName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "basik-parameter-component.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        Scene scene = new Scene(fxmlLoader.load(), 200, 400);
        this.parameterNameText.setText(parameterName);
        if(effectPercent > 1.0) effectPercent = 1.0F;
        else if(effectPercent < 0.0) effectPercent = 0.0F;
        this.percentageText.setText((int)(effectPercent*100) + "%");
        this.progressBar.setProgress(effectPercent);
        Stage stage = new Stage();
        stage.setTitle("BasikGUI - " + effectName);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
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
            double progress = (1.0 - ((robot.getMouseY() - 30) -  progressBar.getScene().getWindow().getY())/height);
            if(progress > 1.0) {
                progress = 1.0;
            } else if(progress < 0.0) {
                progress = 0.0;
            }
            progressBar.setProgress(progress);
            this.percentageText.setText((int)(progress*100) + "%");
        }
    }

    @FXML
    private void onMouseDown(MouseEvent event) {
        isMouseDown = true;
        progressBar.setProgress((1.0 - event.getY()/(height)));
        this.percentageText.setText((int)(progressBar.getProgress()*100) + "%");
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
