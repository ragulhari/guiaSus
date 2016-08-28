
package com.ragulhari.guiasus;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by ricardo on 27/08/16.
 */
public class ApiConnector extends AsyncTask<String, Void, String> {

    private String urlDominio;
    private String strUrl;

    //TODO: Construtor
    public ApiConnector()
    {
        this.urlDominio = "http://mobile-aceite.tcu.gov.br/mapa-da-saude/";
    }

    @Override
    protected String doInBackground(String... urls) {

        String total = null;
        URL url;

        try {
            url = new URL(this.urlDominio + urls[0]);
            HttpURLConnection objConnection = (HttpURLConnection) url.openConnection();

            int responseCode = objConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                BufferedReader r = new BufferedReader(new InputStreamReader(objConnection.getInputStream()));
                String line;
                while ((line = r.readLine()) != null) {

                    if (total != null)
                        total += '\n' + line;
                    else
                        total = line;
                }
            }

        }
        catch (Exception err){
            System.out.println(err.getStackTrace());
        }

        return (total == null?"":total);
    }

    protected void onPostExecute(String value) {
    }

}
