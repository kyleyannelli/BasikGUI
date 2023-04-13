package basik.kyleyannelli.Helpers;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class BasikAPI {
    static final String urlBase = "http://localhost:30108";
    static final String deletePedalUrl = urlBase + "/effect";
    public static void deletePedalRequest(int position) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.custom().build()).build();
        HttpDelete request = new HttpDelete(deletePedalUrl + "/" + position);
        httpClient.execute(request);
    }
}
