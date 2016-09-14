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

    public static List<placeListObjectItem> objListPlaces = new ArrayList<placeListObjectItem>();

    private static void addItem(placeListObjectItem item) {
        objListPlaces.add(item);
    }

    private String strTelefone;
    private String strNomeFantasia;
    private String strTipoUnidade;
    private String strEsferaAdministrativa;
    private String strVinculoSus;
    private String strTurnoAtendimento;

    //Serviços Prestados pelo estabelecimento
    private boolean blnAtendimentoUrgencia;
    private boolean blnObstetra;
    private boolean blnNeoNatal;

    //Endereço do estabelecimento
    private String strLogradouro;
    private String strNumero;
    private String strBairro;
    private String strCidade;
    private String strUf;
    private String strCep;


    public void createArray(JSONArray objJSONPlaces) {

        try {
            for (int i = 0; i < objJSONPlaces.length(); i++) {
                JSONObject objItem = objJSONPlaces.getJSONObject(i);
                validateArray(objItem);

                    placeListObjectItem obj = new placeListObjectItem(strNomeFantasia, strTipoUnidade,strTelefone,
                        strEsferaAdministrativa, strVinculoSus, strTurnoAtendimento, strLogradouro, strNumero, strBairro,
                        strCidade, strUf, strCep, blnAtendimentoUrgencia, blnObstetra, blnNeoNatal);

                addItem(obj);
            }
        }
        catch(JSONException err){
            err.getStackTrace();
        }
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
                blnAtendimentoUrgencia = objJSON.getBoolean("temAtendimentoUrgencia");
            else
                blnAtendimentoUrgencia = false;

            if (objJSON.has("temObstetra"))
                blnObstetra = objJSON.getBoolean("temObstetra");
            else
                blnObstetra = false;

            if (objJSON.has("temNeoNatal"))
                blnNeoNatal = objJSON.getBoolean("temNeoNatal");
            else
                blnNeoNatal = false;

            if (objJSON.has("logradouro") && (objJSON.getString("logradouro") != "null"))
                strLogradouro = objJSON.getString("logradouro");
            else
                strLogradouro = "";

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

        }
        catch(JSONException err) {
            err.getStackTrace();
        }
    }

}


