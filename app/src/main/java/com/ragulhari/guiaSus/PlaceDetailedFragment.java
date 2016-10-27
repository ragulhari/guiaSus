package com.ragulhari.guiaSus;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ragulhari.guiaSus.listObjects.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlaceDetailedFragment extends Fragment {
    private final placeListObjectItem objItem;
    private static final int CODIGO_APLICATIVO = 310;
    private static final int CODIGO_TIPO_POSTAGEM = 195;
    private DatabaseHandler dbHandler;

    private TextView nomeFantasia_detail;
    private TextView telefone_detail;
    private TextView tipoUnidade_detail;
    private TextView bairro_detail;
    private TextView logradouro_numero_detail;
    private TextView cidade_uf_detail;
    private TextView atendimento_urgencia_detail;
    private TextView obstetra_detail;
    private TextView neonatal_detail;
    private TextView turnoAtendimento_detail;
    private Button btnSubmit;
    private EditText txtFeedback;
    private RatingBar ratingBar_feedback;

    private String strCnes = "";
    private String userEmail = "";
    private String googleTokenId = "";
    private String codigoUsuario = "";

    private OnFragmentInteractionListener mListener;

    public PlaceDetailedFragment() {
        objItem = new placeListObjectItem();
    }


    private void populateForm(placeListObjectItem objItem){
        strCnes = objItem.strCnes;
        nomeFantasia_detail.setText(objItem.strNomeFantasia);
        telefone_detail.setText(objItem.strTelefone);
        tipoUnidade_detail.setText(objItem.strTipoUnidade);
        bairro_detail.setText(objItem.strBairro);
        turnoAtendimento_detail.setText(objItem.strTurnoAtendimento);
        logradouro_numero_detail.setText(objItem.strLogradouro + getString(R.string.separator) + objItem.strNumero);
        cidade_uf_detail.setText(objItem.strCidade + getString(R.string.separator) + objItem.strUf);
        atendimento_urgencia_detail.setText(getString(R.string.urgency_title) + objItem.strAtendimentoUrgencia);
        obstetra_detail.setText(getString(R.string.obstetra_title) + objItem.strObstetra);
        neonatal_detail.setText(getString(R.string.neonatal_title) + objItem.strNeoNatal);

        OpinionItem objOpinionItem = dbHandler.searchData(strCnes, this.userEmail);

        if (objOpinionItem != null) {
            txtFeedback.setText(objOpinionItem.opinionText);
            ratingBar_feedback.setRating(objOpinionItem.rating);
            btnSubmit.setVisibility(View.GONE);
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHandler = new DatabaseHandler(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vDetailed = inflater.inflate(R.layout.fragment_place_detailed, container, false);

        nomeFantasia_detail = (TextView) vDetailed.findViewById(R.id.nomeFantasia_detail);
        telefone_detail = (TextView) vDetailed.findViewById(R.id.telefone_detail);
        tipoUnidade_detail = (TextView) vDetailed.findViewById(R.id.tipoUnidade_detail);
        bairro_detail = (TextView) vDetailed.findViewById(R.id.bairro_detail);
        logradouro_numero_detail = (TextView) vDetailed.findViewById(R.id.logradouro_numero_detail);
        cidade_uf_detail = (TextView) vDetailed.findViewById(R.id.cidade_uf_detail);
        atendimento_urgencia_detail = (TextView) vDetailed.findViewById(R.id.atendimento_urgencia_detail);
        obstetra_detail = (TextView) vDetailed.findViewById(R.id.obstetra_detail);
        neonatal_detail = (TextView) vDetailed.findViewById(R.id.neonatal_detail);
        turnoAtendimento_detail = (TextView) vDetailed.findViewById(R.id.turnoAtendimento_detail);
        btnSubmit = (Button) vDetailed.findViewById(R.id.submitRating);
        txtFeedback = (EditText) vDetailed.findViewById(R.id.rating_feedback);
        ratingBar_feedback = (RatingBar) vDetailed.findViewById(R.id.ratingStars);
        ratingBar_feedback.setNumStars(5);

        //Para trocar a cor da estrela para Vermelho
        LayerDrawable stars = (LayerDrawable) ratingBar_feedback.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#9f0101"), PorterDuff.Mode.SRC_ATOP);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSubmit.setText(R.string.sending);
                submitFeedback();
            }
        });

        if (getArguments() != null) {

            if (getArguments().containsKey("userEmail"))
                this.userEmail = getArguments().getString("userEmail");

            if (getArguments().containsKey("googleTokenId"))
                googleTokenId = getArguments().getString("googleTokenId");

            if (getArguments().containsKey("selectedItem"))
            {
                try{
                    String objJSONItem = getArguments().getString("selectedItem");
                    objItem.createObjectFromJSON(new JSONObject(objJSONItem));
                    populateForm(objItem);
                }
                catch (Exception err)
                {
                    err.printStackTrace();
                }
            }
            else
            {
                if (getArguments().containsKey("titulo")){
                    try {
                        String strTitulo = getArguments().getString("titulo");
                        JSONArray objArray = new JSONArray(getArguments().getString("queryResponse"));
                        for (int i = 0; i < objArray.length(); i++)
                        {
                            if (objArray.getJSONObject(i).has("nomeFantasia") &&
                                    strTitulo != null &&
                                    strTitulo.equals(objArray.getJSONObject(i).getString("nomeFantasia"))

                                    )
                            {
                                objItem.createObjectFromJSON(objArray.getJSONObject(i));
                                populateForm(objItem);
                            }
                        }

                    }
                    catch (JSONException err){
                        err.printStackTrace();
                    }

                }
            }

        }

        // Inflate the layout for this fragment
        return vDetailed;
    }

    private void submitFeedback()
    {
        ProgressDialog dialog = new ProgressDialog((getActivity()));
        dialog.setMessage("Enviando avaliação");
        dialog.setIndeterminate(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();


        int iRatingStars = Math.round(ratingBar_feedback.getRating());
        String sOpinion = txtFeedback.getText().toString();
        String sPostCode;

        String appToken = autenticar();
        if ((appToken != null) && (!codigoUsuario.equals("")))
        {
            APIConnector objAPIConnector = new APIConnector("http://mobile-aceite.tcu.gov.br:80/appCivicoRS/rest/postagens", "POST");
            objAPIConnector.addHeader("appToken", appToken);
            objAPIConnector.addHeader("appIdentifier", Integer.toString(CODIGO_APLICATIVO));

            String strJSON = "{ \"autor\": { \"codPessoa\": " + codigoUsuario + "}, \"tipo\": { \"codTipoPostagem\": " + Integer.toString(CODIGO_TIPO_POSTAGEM) + "}}";
            objAPIConnector.InsertJSON(strJSON);
            objAPIConnector.hasLocationHeader = true;
            try {
                objAPIConnector.execute();
            }
            catch(Exception err)
            {
                err.printStackTrace();
            }

            try {
                while(!objAPIConnector.finished)
                    Thread.sleep(100);
            }
            catch (InterruptedException err){

    err.getStackTrace();
            }

            String sPostCodeURL = objAPIConnector.location;
            if ((sPostCodeURL != null) && (!sPostCodeURL.equals("")))
            {
                sPostCode = sPostCodeURL.substring(sPostCodeURL.lastIndexOf('/') + 1);

                APIConnector objAPIConnectorConteudo = new APIConnector("http://mobile-aceite.tcu.gov.br:80/appCivicoRS/rest/postagens/" + sPostCode + "/conteudos", "POST");
                objAPIConnectorConteudo.addHeader("appToken", appToken);

                String strJSONConteudo = "{ \\\"rating\\\": " + Float.toString(iRatingStars) + ", \\\"comentario\\\": \\\"" + sOpinion + "\\\"}";
                String strJSONBody = "{\"JSON\": \"" + strJSONConteudo + "\", \"texto\":\"#buscasaude\", \"valor\":" + Float.toString(iRatingStars) + "}";
                objAPIConnectorConteudo.InsertJSON(strJSONBody);
                objAPIConnectorConteudo.hasLocationHeader = true;
                try {
                    objAPIConnectorConteudo.execute();
                }
                catch(Exception err)
                {
                    err.printStackTrace();
                }

                try {
                    while(!objAPIConnectorConteudo.finished)
                        Thread.sleep(100);
                }
                catch (InterruptedException err){
                    err.getStackTrace();
                }

                String sFinalPostResult = objAPIConnectorConteudo.location;
                sFinalPostResult = sFinalPostResult.substring(sFinalPostResult.lastIndexOf('/'));

                if (!sFinalPostResult.equals("")) {
                    Toast toast = Toast.makeText(getActivity(), "Opinião enviada com sucesso",Toast.LENGTH_SHORT);
                    toast.show();
                    dbHandler.insertData(strCnes,this.userEmail,iRatingStars,sOpinion);
                    getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                }
                else
                {
                    Toast toast = Toast.makeText(getActivity(), "Houve um problema no envio, tente novamente",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            else
            {
                Toast toast = Toast.makeText(getActivity(), "Houve um problema no envio, tente novamente",Toast.LENGTH_SHORT);
                toast.show();
            }

        }
        dialog.hide();
    }

    private String autenticar()
    {
        String response;


        String strUrl = "http://mobile-aceite.tcu.gov.br:80/appCivicoRS/rest/pessoas/autenticar";
        APIConnector objConector = new APIConnector(strUrl, "GET");
        objConector.addHeader("email",userEmail);
        objConector.addHeader("googleToken",googleTokenId);

        String appToken;
        try {
            response = objConector.execute().get();
            appToken = objConector.tokenAppCivico;
            try {
                JSONObject objJSON = new JSONObject(response);
                codigoUsuario = objJSON.get("cod").toString();
            }
            catch (JSONException err)
            {
                err.printStackTrace();
            }
        }
        catch(Exception err)
        {
            appToken = null;
        }

        return appToken;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }
}
