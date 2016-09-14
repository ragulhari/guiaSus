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

    private OnFragmentInteractionListener mListener;

    public PlaceDetailedFragment() {
        objItemList = new placeListObject();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView nomeFantasia_detail = (TextView)getView().findViewById(R.id.nomeFantasia_detail);
        TextView telefone_detail = (TextView)getView().findViewById(R.id.telefone_detail);
        TextView tipoUnidade_detail = (TextView)getView().findViewById(R.id.tipoUnidade_detail);
        TextView bairro_detail = (TextView)getView().findViewById(R.id.bairro_detail);
        TextView logradouro_numero_detail = (TextView)getView().findViewById(R.id.logradouro_numero_detail);
        TextView cidade_uf_detail = (TextView)getView().findViewById(R.id.cidade_uf_detail);
        TextView atendimento_urgencia_detail = (TextView)getView().findViewById(R.id.atendimento_urgencia_detail);
        TextView obstetra_detail = (TextView)getView().findViewById(R.id.obstetra_detail);
        TextView neonatal_detail = (TextView)getView().findViewById(R.id.neonatal_detail);

        if (getArguments() != null) {
            if (getArguments().containsKey("selectedItem"))
            {
                placeListObjectItem objDetailedPlace;

                try{
                    objJSONItem = getArguments().getString(ARG_PARAM1);
                    JSONArray objArray = new JSONArray(objJSONItem);
                    objItemList.createArray(objArray);
                    objDetailedPlace = objItemList.objListPlaces.get(0);

                    nomeFantasia_detail.setText(objDetailedPlace.strNomeFantasia);
                    telefone_detail.setText(objDetailedPlace.strTelefone);
                    tipoUnidade_detail.setText(objDetailedPlace.strTipoUnidade);
                    bairro_detail.setText(objDetailedPlace.strBairro);
                    logradouro_numero_detail.setText(objDetailedPlace.strLogradouro + " - " + objDetailedPlace.strNumero);
                    cidade_uf_detail.setText(objDetailedPlace.strCidade + " - " + objDetailedPlace.strUf);
                    atendimento_urgencia_detail.setText("Tem atendimento de Urgência? - " + String.valueOf(objDetailedPlace.blnAtendimentoUrgencia));
                    obstetra_detail.setText("Tem atendimento de Urgência? - " + String.valueOf(objDetailedPlace.blnObstetra));
                    neonatal_detail.setText("Tem atendimento de Urgência? - " + String.valueOf(objDetailedPlace.blnNeoNatal));

                }
                catch (Exception err)
                {
                    err.getStackTrace();
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_place_detailed, container, false);
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
