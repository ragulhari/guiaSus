package com.ragulhari.guiasus;

import java.util.ArrayList;

/**
 * Created by ricardo on 21/09/16.
 */
public class Locations {

    public Locations(){

    }

    public String getLocationsByCoordinates(String dominio, double latitude, double longitude, int ray)
    {
        String response;
        ApiConnector objConector;


        objConector = new ApiConnector("http://mobile-aceite.tcu.gov.br/mapa-da-saude/");

        if (objConector == null) {
            return null;
        }

        String strUrl = "rest/estabelecimentos/latitude/" + Double.toString(latitude) + "/longitude/" + Double.toString(longitude) + "/raio/" + Integer.toString(ray);

        try {
            response = objConector.execute(strUrl).get();
        }
        catch(Exception err)
        {
            response = null;
        }

        return response;

    }

    public String getLocationsByFilters(String dominio, String uf, String cidade)
    {
        String response;
        ApiConnector objConector;


        objConector = new ApiConnector("http://mobile-aceite.tcu.gov.br/mapa-da-saude/");

        if (objConector == null) {
            return null;
        }

        String strUrl = "rest/estabelecimentos?uf=" + uf + "&municipio=" + cidade;

        try {
            response = objConector.execute(strUrl).get();
        }
        catch(Exception err)
        {
            response = null;
        }

        return response;

    }

}
