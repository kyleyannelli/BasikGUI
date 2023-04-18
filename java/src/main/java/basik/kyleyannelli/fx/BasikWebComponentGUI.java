package basik.kyleyannelli.fx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BasikWebComponentGUI extends AnchorPane implements Initializable {
    @FXML
    private WebView webView;
    private String urlToLoad;
    public BasikWebComponentGUI(String urlToLoad) throws IOException {
        this.urlToLoad = urlToLoad;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "basik-web-component-view.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        webView = new WebView();
        Scene scene = new Scene(fxmlLoader.load(), 600, 413);
        Stage stage = new Stage();
        stage.setTitle("BasikGUI");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WebEngine engine = webView.getEngine();
        engine.load(urlToLoad);
    }
}
