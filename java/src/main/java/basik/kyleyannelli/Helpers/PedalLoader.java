package basik.kyleyannelli.Helpers;

import basik.kyleyannelli.Models.Pedal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PedalLoader {
    public static ArrayList<Pedal> getPedalboardFromAPI() throws IOException {
        ArrayList<Pedal> pedals = new ArrayList<>();
        System.out.println(basicGetReq("http://localhost:30108/pedalboard"));
        return pedals;
    }

    public static String basicGetReq(String urlString) throws IOException {
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
        return bufferIn.toString();
    }
}
