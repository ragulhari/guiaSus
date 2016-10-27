package com.ragulhari.guiaSus;
/**
* Classe construída com o objetivo de centralizar todas as buscas por estabelecimentos junto à API do TCU.
* São dois os tipos de consulta: consulta baseada em coordenadas latitude/longitude (obtida pelo GPS) e consulta baseada em município.
* Está pendente ainda de construção a implementação do filtro variável do raio de busca para busca baseada em coordenadas (hoje é fixo no código)
*
* */
class Locations {

    /**
    * Busca de locais por coordenadas
    * As coordenadas são obtidas pelo GPS do device
    * A API de back-end é a do TCU, sendo a documentação disponibilizada em https://github.com/AppCivicoPlataforma/AppCivico
     * @param dominio URL completa do endereço a buscar os endereços
     * @param latitude latitude do local referência para obter os estabelecimentos
     * @param longitude longitude do local referência para obter os estabelecimentos
     * @param ray raio de busca a partir do local referência
     * @param tipoEstabelecimento String que identifica o tipo de estabelecimento a ser pesquisado
    * */
    public String getLocationsByCoordinates(String dominio, double latitude, double longitude, int ray, String tipoEstabelecimento)
    {
        String response;
        APIConnector objConector;

        String strUrl = "rest/estabelecimentos/latitude/" + Double.toString(latitude) + "/longitude/" + Double.toString(longitude) + "/raio/" + Integer.toString(ray) + "?quantidade=200";

        if (!tipoEstabelecimento.equals("Todos os estabelecimentos"))
            strUrl += "&categoria=" + tipoEstabelecimento;

        objConector = new APIConnector(dominio + strUrl, "GET");

        try {
            response = objConector.execute().get();
        }
        catch(Exception err)
        {
            response = null;
        }

        return response;

    }

   /**
    * Busca de locais por UF e município
    * Como filtro temos o tipo de estabelecimento (consultório, laboratório, etc)
    * A API de back-end é a do TCU, sendo a documentação disponibilizada em https://github.com/AppCivicoPlataforma/AppCivico
    * @param dominio URL completa do endereço a buscar os endereços
    * @param uf Estado de busca de estabelecimentos
    * @param cidade Cidade de busca de estabelecimentos
    * @param tipoEstabelecimento String que identifica o tipo de estabelecimento a ser pesquisado
    * */

    public String getLocationsByFilters(String dominio, String uf, String cidade, String tipoEstabelecimento)
    {
        String response;
        APIConnector objConector;

        String strUrl = "rest/estabelecimentos?uf=" + uf + "&municipio=" + cidade + "&quantidade=100";
        if (!tipoEstabelecimento.equals("Todos os estabelecimentos"))
            strUrl += "&categoria=" + tipoEstabelecimento;

        objConector = new APIConnector(dominio + strUrl, "GET");

        try {
            response = objConector.execute().get();
        }
        catch(Exception err)
        {
            response = null;
        }

        return response;

    }

}
