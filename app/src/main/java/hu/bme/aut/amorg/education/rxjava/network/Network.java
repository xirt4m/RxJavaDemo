package hu.bme.aut.amorg.education.rxjava.network;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Network {

    public static final String URL_TIME_API_NOW = "http://www.timeapi.org/utc/now";

    public static String getTimeFromApi() throws IOException {
        URL url = new URL(URL_TIME_API_NOW);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStream in = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            is = urlConnection.getInputStream();
            in = new BufferedInputStream(is);
            br = new BufferedReader(new InputStreamReader(in));

            String read;
            StringBuilder sb = new StringBuilder();
            while ((read = br.readLine()) != null) {
                sb.append(read);
            }
            return sb.toString();
        } finally {
            if (br != null) {
                br.close();
            }
            if (is != null) {
                is.close();
            }
            if (in != null) {
                in.close();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}
