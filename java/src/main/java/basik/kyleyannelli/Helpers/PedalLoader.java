package basik.kyleyannelli.Helpers;

import basik.kyleyannelli.Models.Chorus;
import basik.kyleyannelli.Models.Distortion;
import basik.kyleyannelli.Models.Pedal;
import basik.kyleyannelli.Models.Reverb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class PedalLoader {
    public static ArrayList<Pedal> getPedalboardFromAPI() throws IOException {
        String[] pedals = basicGetReq("http://localhost:30108/pedalboard").replace("\"", "")
                .replace("[", "")
                .replace("]", "")
                .replace("\\", "")
                .split(Pattern.quote("|"));
        ArrayList<Pedal> pedalModels = new ArrayList<>();
        for(String pedal : pedals) {
            if(pedal.startsWith("name:reverb")) {
                pedalModels.add(Reverb.buildFromString(pedal));
            } else if(pedal.startsWith("name:distortion")) {
                pedalModels.add(Distortion.buildFromString(pedal));
            } else if(pedal.startsWith("name:chorus")) {
                pedalModels.add(Chorus.buildFromString(pedal));
            } else {
                System.out.println("IGNORED: " + pedal);
            }
        }
        return pedalModels;
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
