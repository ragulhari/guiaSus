package com.ragulhari.guiasus;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ragulhari.guiasus.listObjects.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlaceDetailedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlaceDetailedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaceDetailedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "selectedItem";

    // TODO: Rename and change types of parameters
    private placeListObjectItem objItem;
    private placeListObject objItemList;
    private String objJSONItem;

    TextView nomeFantasia_detail;
    TextView telefone_detail;
    TextView tipoUnidade_detail;
    TextView bairro_detail;
    TextView logradouro_numero_detail;
    TextView cidade_uf_detail;
    TextView atendimento_urgencia_detail;
    TextView obstetra_detail;
    TextView neonatal_detail;

    private OnFragmentInteractionListener mListener;

    public PlaceDetailedFragment() {
        objItem = new placeListObjectItem();
        objItemList = new placeListObject();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PlaceDetailedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaceDetailedFragment newInstance(placeListObjectItem param1) {
        PlaceDetailedFragment fragment = new PlaceDetailedFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, objJSONItem);
        fragment.setArguments(args);
        return fragment;
    }

    public void populateForm(placeListObjectItem objItem){
        nomeFantasia_detail.setText(objItem.strNomeFantasia);
        telefone_detail.setText(objItem.strTelefone);
        tipoUnidade_detail.setText(objItem.strTipoUnidade);
        bairro_detail.setText(objItem.strBairro);
        logradouro_numero_detail.setText(objItem.strLogradouro + " - " + objItem.strNumero);
        cidade_uf_detail.setText(objItem.strCidade + " - " + objItem.strUf);
        atendimento_urgencia_detail.setText("Tem atendimento de Urgência? - " + objItem.strAtendimentoUrgencia);
        obstetra_detail.setText("Tem Obstetra? - " + objItem.strObstetra);
        neonatal_detail.setText("Realiza exame Neo Natal? - " + objItem.strNeoNatal);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        if (getArguments() != null) {
            if (getArguments().containsKey("selectedItem"))
            {
                try{
                    objJSONItem = getArguments().getString("selectedItem");
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
                                    strTitulo.equals(objArray.getJSONObject(i).getString("nomeFantasia")))
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
