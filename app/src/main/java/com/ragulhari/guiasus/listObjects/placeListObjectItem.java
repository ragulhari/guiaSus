package com.ragulhari.guiasus.listObjects;

public class placeListObjectItem {

    //Dados Gerais do estabelecimento
    public String strNomeFantasia;
    public String strTipoUnidade;
    public String strTelefone;
    public String strEsferaAdministrativa;
    public String strVinculoSus;
    public String strTurnoAtendimento;

    //Serviços Prestados pelo estabelecimento
    public boolean blnAtendimentoUrgencia;
    public boolean blnObstetra;
    public boolean blnNeoNatal;

    //Endereço do estabelecimento
    public String strLogradouro;
    public String strNumero;
    public String strBairro;
    public String strCidade;
    public String strUf;
    public String strCep;


    public placeListObjectItem(){
        this.strNomeFantasia = "";
        this.strTipoUnidade = "";
        this.strTelefone = "";
        this.strEsferaAdministrativa = "";
        this.strVinculoSus = "";
        this.strTurnoAtendimento = "";
        this.blnAtendimentoUrgencia = false;
        this.blnObstetra = false;
        this.blnNeoNatal = false;
        this.strLogradouro = "";
        this.strNumero = "";
        this.strBairro = "";
        this.strCidade = "";
        this.strUf = "";
        this.strCep = "";
    }

    public placeListObjectItem(String pNomeFantasia, String pTipoUnidade, String pTelefone) {
        this.strNomeFantasia = pNomeFantasia;
        this.strTipoUnidade = pTipoUnidade;
        this.strTelefone = pTelefone;
        this.strEsferaAdministrativa = "";
        this.strVinculoSus = "";
        this.strTurnoAtendimento = "";
        this.blnAtendimentoUrgencia = false;
        this.blnObstetra = false;
        this.blnNeoNatal = false;
        this.strLogradouro = "";
        this.strNumero = "";
        this.strBairro = "";
        this.strCidade = "";
        this.strUf = "";
        this.strCep = "";
    }

    public placeListObjectItem(String pNomeFantasia, String pTipoUnidade, String pTelefone,
                               String pEsferaAdministrativa, String pVinculoSus, String pTurnoAtendimento,
                               String pLogradouro, String pNumero, String pBairro, String pCidade, String pUf,
                               String pCep, boolean pAtendimentoUrgencia, boolean pObstetra, boolean pNeoNatal) {
        this.strNomeFantasia = pNomeFantasia;
        this.strTipoUnidade = pTipoUnidade;
        this.strTelefone = pTelefone;
        this.strEsferaAdministrativa = pEsferaAdministrativa;
        this.strVinculoSus = pVinculoSus;
        this.strTurnoAtendimento = pTurnoAtendimento;
        this.blnAtendimentoUrgencia = pAtendimentoUrgencia;
        this.blnObstetra = pObstetra;
        this.blnNeoNatal = pNeoNatal;
        this.strLogradouro = pLogradouro;
        this.strNumero = pNumero;
        this.strBairro = pBairro;
        this.strCidade = pCidade;
        this.strUf = pUf;
        this.strCep = pCep;
    }

    public String convertToString()
    {
        return "[{" +
                "nomeFantasia:\"" + this.strNomeFantasia + "\"," +
                "tipoUnidade:\"" + this.strTipoUnidade + "\"," +
                "telefone:\"" + this.strTelefone + "\"," +
                "esferaAdministrativa:\"" + this.strEsferaAdministrativa + "\"," +
                "vinculoSus:\"" + this.strVinculoSus + "\"," +
                "turnoAtendimento:\"" + this.strTurnoAtendimento + "\"," +
                "temAtendimentoUrgencia:\"" + String.valueOf(this.blnAtendimentoUrgencia) + "\"," +
                "temObstetra:\"" + String.valueOf(this.blnObstetra) + "\"," +
                "temNeoNatal:\"" + String.valueOf(this.blnNeoNatal) + "\"," +
                "logradouro:\"" + this.strLogradouro + "\"," +
                "numero:\"" + this.strNumero + "\"," +
                "bairro:\"" + this.strBairro + "\"," +
                "cidade:\"" + this.strCidade + "\"," +
                "uf:\"" + this.strUf + "\"," +
                "cep:\"" + this.strCep + "\"" +
                "}]";
    }

}
