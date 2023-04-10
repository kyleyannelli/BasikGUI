package basik.kyleyannelli.fx.Components;

import basik.kyleyannelli.fx.BasikGUI;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class BasikReverbComponent extends StackPane {
    private ImageView mixKnob, widthKnob, sizeKnob, dampingKnob;
    private ImageView basePedal;
    public BasikReverbComponent() {
        super();
        this.maxHeight(-Double.MAX_VALUE);
        this.maxWidth(-Double.MAX_VALUE);
        this.minHeight(-Double.MAX_VALUE);
        this.minWidth(-Double.MAX_VALUE);

        this.prefHeight(236.0);
        this.prefWidth(143.0);
        basePedal = new ImageView(getClass().getResource("ReverbPedal.png").toString());
        basePedal.setFitHeight(237.0);
        basePedal.setFitWidth(164);
        basePedal.setPickOnBounds(true);
        basePedal.setPreserveRatio(true);
        basePedal.setSmooth(true);
        this.getChildren().add(basePedal);
    }
}
