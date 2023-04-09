package basik.kyleyannelli.fx;

import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.MultipartContent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BasikIOController {
    private boolean isShowing = false;
    @FXML
    public void initialize() {
    }

    @FXML
    public void closeScene(ActionEvent event) {
        // get a handle to the stage
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    @FXML
    public void onInputComboBoxSelection(ActionEvent event) throws IOException {
        if(isShowing) {
            putAPIRequest("http://localhost:30108/input", "input_number",
                    ((String)((ComboBox)event.getSource()).getValue()).replaceAll("[^0-9]", ""));
            isShowing = false;
        }
    }

    @FXML
    public void onOutputComboBoxSelection(ActionEvent event) throws IOException {
        if(isShowing) {
            putAPIRequest("http://localhost:30108/output", "output_number",
                    ((String)((ComboBox)event.getSource()).getValue()).replaceAll("[^0-9]", ""));
            isShowing = false;
        }
    }

    @FXML
    public void onInputComboBoxShown(Event event) throws IOException {
        ComboBox comboBox = (ComboBox) event.getSource();
        List<String> list = getAPIRequest("http://localhost:30108/inputs");
        comboBox.getItems().setAll();
        comboBox.getItems().addAll(list);
        isShowing = true;
    }

    @FXML
    public void onOutputComboBoxShown(Event event) throws IOException {
        ComboBox comboBox = (ComboBox) event.getSource();
        List<String> list = getAPIRequest("http://localhost:30108/outputs");
        comboBox.getItems().setAll();
        comboBox.getItems().addAll(list);
        isShowing = true;
    }

    private void putAPIRequest(String urlString, String name, String value) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.custom().build()).build();
        HttpPut request = new HttpPut(urlString);
        HttpEntity entity = MultipartEntityBuilder
                .create()
                .addTextBody(name, value, ContentType.APPLICATION_FORM_URLENCODED)
                .build();
        request.setEntity(entity);
        httpClient.execute(request);
    }

    private List<String> getAPIRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer bufferIn = new StringBuffer();
        while((inputLine = inputStream.readLine()) != null) {
            bufferIn.append(inputLine);
        }
        inputStream.close();
        String stringyBuffer = bufferIn.toString();
        return Arrays.stream(stringyBuffer.replace("\"",  "")
                .replace("\\", "")
                .replace("[", "")
                .replace("]", "")
                .split(",")).toList();
    }
}