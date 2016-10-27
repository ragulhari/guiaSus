package com.ragulhari.guiaSus;

import android.os.AsyncTask;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
* Classe genérica criada com o objetivo de centralizar o código referente à chamada de APIs no sistema.
* */
class APIConnector extends AsyncTask<Void,Void,String> {

    private final String urlDominio;
    private String strJSON;
    private final List<String> header = new ArrayList<>();
    private final List<String> headerValue = new ArrayList<>();
    public boolean hasLocationHeader = false;
    String location = "";
    boolean finished = false;
    private String strMethod;
    public String tokenAppCivico = "";
    public int responseCode = 0;

    /**
    O construtor tem dois parâmetros:
    @param method O endereço COMPLETO da chamada da API;
    @param strUrl O metodo de chamada de API, em String. Aceita "POST" e "GET" (não foi previsto tratamento para outros métodos por não haver necessidade no momento).
     */
    public APIConnector(String strUrl, String method)
    {
        this.urlDominio = strUrl;
        this.strMethod = method;
    }

    /**
     * Este método deve ser invocado para inserir no body da requisição um JSON genérico, não há validação sobre este JSON neste método
     * @param json JSON a ser inserido no corpo da requisição, neste método não há validação sobre o formato do JSON
     * */
    public void InsertJSON(String json)
    {
        this.strJSON = json;
    }

    /**
     * Este método foi construído para adicionar headers na requisição
     * @param name Nome do cabeçalho a ser inserido
     * @param value Valor do cabeçalho
     */

    public void addHeader(String name, String value){
        this.header.add(name);
        this.headerValue.add(value);
    }


    /**
     *
     * @param voids sem parâmetro
     * @return em caso de um GET, retorna o contéudo obtido. Em caso de POST retorna uma string vazia
     */
    @Override
    protected String doInBackground(Void... voids) {
        String total = null;

        try {

            URL url = new URL(urlDominio);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

            //Em caso de POST, alguns tratamentos devem ser aplicados ao objeto, como o cabeçalho "Content-Type". Só será utilizado JSON neste momento, por isso está fixo
            if (strMethod.equals("POST")){
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
            }

            //Em caso de haver headers a serem adicionados na requisição, é feito por este código
            for(int i=0; i<header.size();i++)
                httpURLConnection.setRequestProperty(header.get(i), headerValue.get(i));

            //Inicia conexão
            httpURLConnection.connect();

            //Executa o código da chamada apropriado para cada método aceito pela classe (GET e POST)
            if (strMethod.equals("POST")) {
                JSONObject jsonObject = new JSONObject(strJSON);

                OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
                wr.write(jsonObject.toString());
                wr.close();

                responseCode = httpURLConnection.getResponseCode();
                if (responseCode == 201) {
                    //Caso seja indicado que é necessário obter o header "Location", recupera nesta etapa
                    if (hasLocationHeader)
                        location = httpURLConnection.getHeaderField("location");

                }
            } else {
                responseCode = httpURLConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK)
                {
                    tokenAppCivico = httpURLConnection.getHeaderField("apptoken");

                    //Este código prepara o retorno do método
                    BufferedReader r = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String line;

                    while ((line = r.readLine()) != null) {

                        if (total != null)
                            total += '\n' + line;
                        else
                            total = line;
                    }
                }

            }

            httpURLConnection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            finished = true;
        }

        return (total == null?"":total);

    }

}
