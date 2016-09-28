package com.ragulhari.guiasus.listObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricardo on 14/09/16.
 */
public class placeListObject {

    public List<placeListObjectItem> objListPlaces = new ArrayList<placeListObjectItem>();

    private String strTelefone;
    private String strNomeFantasia;
    private String strTipoUnidade;
    private String strEsferaAdministrativa;
    private String strVinculoSus;
    private String strTurnoAtendimento;

    //Serviços Prestados pelo estabelecimento
    private String strAtendimentoUrgencia;
    private String strObstetra;
    private String strNeoNatal;

    //Endereço do estabelecimento
    private String strLogradouro;
    private String strNumero;
    private String strBairro;
    private String strCidade;
    private String strUf;
    private String strCep;
    private String latitude;
    private String longitude;

    public void addItem(placeListObjectItem item) {
        objListPlaces.add(item);
    }

    public void createArray(JSONArray objJSONPlaces) {

        try {
            for (int i = 0; i < objJSONPlaces.length(); i++) {
                JSONObject objItem = objJSONPlaces.getJSONObject(i);
                validateArray(objItem);

                    placeListObjectItem obj = new placeListObjectItem(strNomeFantasia, strTipoUnidade,strTelefone,
                        strEsferaAdministrativa, strVinculoSus, strTurnoAtendimento, strLogradouro, strNumero, strBairro,
                        strCidade, strUf, strCep, strAtendimentoUrgencia, strObstetra, strNeoNatal, latitude, longitude);

                addItem(obj);
            }
        }
        catch(JSONException err){
            err.getStackTrace();
        }
    }


    public List<placeListObjectItem> createExternalArray(JSONArray objJSONPlaces) {

        List<placeListObjectItem> objRetorno = new ArrayList<>();

        try {
            for (int i = 0; i < objJSONPlaces.length(); i++) {
                JSONObject objItem = objJSONPlaces.getJSONObject(i);
                validateArray(objItem);

                objRetorno.add(new placeListObjectItem(strNomeFantasia, strTipoUnidade,strTelefone,
                        strEsferaAdministrativa, strVinculoSus, strTurnoAtendimento, strLogradouro, strNumero, strBairro,
                        strCidade, strUf, strCep, strAtendimentoUrgencia, strObstetra, strNeoNatal, latitude, longitude));
            }
        }
        catch(JSONException err){
            err.getStackTrace();
            return null;
        }

        return objRetorno;
    }


    public void validateArray(JSONObject objJSON)
    {
        try{
            if (objJSON.has("nomeFantasia"))
                strNomeFantasia = objJSON.getString("nomeFantasia");
            else
                strNomeFantasia = "Sem nome cadastrado";

            if (objJSON.has("tipoUnidade"))
                strTipoUnidade = objJSON.getString("tipoUnidade");
            else
                strTipoUnidade = "N/A";

            if (objJSON.has("telefone"))
                strTelefone = objJSON.getString("telefone");
            else
                strTelefone = "Sem telefone";

            if (objJSON.has("esferaAdministrativa"))
                strEsferaAdministrativa = objJSON.getString("esferaAdministrativa");
            else
                strEsferaAdministrativa = "Não informado";

            if (objJSON.has("vinculoSus"))
                strVinculoSus = objJSON.getString("vinculoSus");
            else
                strVinculoSus = "Não informado";

            if (objJSON.has("turnoAtendimento"))
                strTurnoAtendimento = objJSON.getString("turnoAtendimento");
            else
                strTurnoAtendimento = "Não informado";

            if (objJSON.has("temAtendimentoUrgencia"))
                strAtendimentoUrgencia = objJSON.getString("temAtendimentoUrgencia");
            else
                strAtendimentoUrgencia = "Não";

            if (objJSON.has("temObstetra"))
                strObstetra = objJSON.getString("temObstetra");
            else
                strObstetra = "Não";

            if (objJSON.has("temNeoNatal"))
                strNeoNatal = objJSON.getString("temNeoNatal");
            else
                strNeoNatal = "Não";

            if (objJSON.has("logradouro") && (objJSON.getString("logradouro") != "null") && objJSON.getString("logradouro") != null)
            {
                strLogradouro = objJSON.getString("logradouro");

                if (objJSON.has("numero") && (objJSON.getString("numero") != "null"))
                    strNumero = objJSON.getString("numero");
                else
                    strNumero = "";

                if (objJSON.has("bairro") && (objJSON.getString("bairro") != "null"))
                    strBairro = objJSON.getString("bairro");
                else
                    strBairro = "";

                if (objJSON.has("cidade") && (objJSON.getString("cidade") != "null"))
                    strCidade = objJSON.getString("cidade");
                else
                    strCidade = "";

                if (objJSON.has("uf") && (objJSON.getString("uf") != "null"))
                    strUf = objJSON.getString("uf");
                else
                    strUf = "";

                if (objJSON.has("cep") && (objJSON.getString("cep") != "null"))
                    strCep = objJSON.getString("cep");
                else
                    strCep = "";

                if (objJSON.has("lat") && (objJSON.getString("lat") != "null"))
                    latitude = objJSON.getString("lat");
                else
                    latitude = "0";

                if (objJSON.has("long") && (objJSON.getString("long") != "null"))
                    longitude = objJSON.getString("long");
                else
                    longitude = "0";

            }
            else
                strLogradouro = "Não informado";


        }
        catch(JSONException err) {
            err.getStackTrace();        }
    }

}


