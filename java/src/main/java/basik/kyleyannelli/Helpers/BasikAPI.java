package basik.kyleyannelli.Helpers;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class BasikAPI {
    static final String urlBase = "http://localhost:30108";
    static final String deletePedalUrl = urlBase + "/effect";
    static final String patchPedalUrl = urlBase + "/effect";
    public static void deletePedalRequest(int position) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.custom().build()).build();
        HttpDelete request = new HttpDelete(deletePedalUrl + "/" + position);
        httpClient.execute(request);
    }

    public static void patchPedalRequest(int currentPosition, int newPosition, String effectParameters) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.custom().build()).build();
        HttpPatch request = new HttpPatch(patchPedalUrl);
        HttpEntity entity = MultipartEntityBuilder
                .create()
                .addTextBody("desired_position_in_board", String.valueOf(newPosition), ContentType.APPLICATION_FORM_URLENCODED)
                .addTextBody("parameters", effectParameters, ContentType.APPLICATION_FORM_URLENCODED)
                .addTextBody("current_position_in_board", String.valueOf(currentPosition), ContentType.APPLICATION_FORM_URLENCODED)
                .build();
        request.setEntity(entity);
        httpClient.execute(request);
    }
}
