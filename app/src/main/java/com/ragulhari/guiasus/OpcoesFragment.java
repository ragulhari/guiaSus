package com.ragulhari.guiasus;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.ragulhari.guiasus.listObjects.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class OpcoesFragment extends Fragment implements OnItemSelectedListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private static final String DEVMEDIA_KEY = "92I398TG6";
    private double dLatitudeAtual = 0;
    private double dLongitudeAtual = 0;
    private String strResponseQuery;

    private String[] arrListaUFs = null;

    private String strRequestedBy = "";

    private boolean isReady = false;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OpcoesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static OpcoesFragment newInstance(int columnCount) {
        OpcoesFragment fragment = new OpcoesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey("requestedBy"))
            strRequestedBy = getArguments().getString("requestedBy");
        if (getArguments().containsKey("latitudeAtual"))
            dLatitudeAtual = getArguments().getDouble("latitudeAtual");
        if (getArguments().containsKey("longitudeAtual"))
            dLongitudeAtual = getArguments().getDouble("longitudeAtual");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_opoes_list, container, false);


        if (strRequestedBy != "MapsActivity") {
            arrListaUFs = BuscaUFs();

            Spinner objUF = (Spinner) view.findViewById(R.id.list_uf);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, arrListaUFs);
            objUF.setAdapter(adapter);
            objUF.setOnItemSelectedListener(this);
        } else {

            Spinner objUF = (Spinner) view.findViewById(R.id.list_uf);
            objUF.setVisibility(View.GONE);
            Spinner objCidades = (Spinner) view.findViewById(R.id.list_cidade);
            objCidades.setVisibility(View.GONE);

        }



        Button btnSubmit = (Button) view.findViewById(R.id.btnSubmit_Search);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findLocations(view);
            }
        });

        return view;
    }


    public void findLocations(View v)
    {
        String strQuery = "";
        Locations objLocation = new Locations();
        placeListObject objPlacesList = null;


        Spinner objUF = (Spinner) this.getView().findViewById(R.id.list_uf);
        Spinner objCidades = (Spinner) this.getView().findViewById(R.id.list_cidade);
        CheckBox chkSus = (CheckBox) this.getView().findViewById(R.id.checkBoxSus);
        CheckBox chkUrgencia = (CheckBox) this.getView().findViewById(R.id.checkBoxUrgencia);
        CheckBox chkObstetra = (CheckBox) this.getView().findViewById(R.id.checkBoxObstetra);
        CheckBox chkNeoNatal = (CheckBox) this.getView().findViewById(R.id.checkBoxNeoNatal);

        if (strRequestedBy == "MapsActivity")
            strResponseQuery = objLocation.getLocationsByCoordinates("http://mobile-aceite.tcu.gov.br/mapa-da-saude/", dLatitudeAtual, dLongitudeAtual, 50);
        else
            strResponseQuery = objLocation.getLocationsByFilters("http://mobile-aceite.tcu.gov.br/mapa-da-saude/", objUF.getSelectedItem().toString(), objCidades.getSelectedItem().toString());

        String strFinalJSON = "";
        placeListObject objNewPlacesList = null;

        try {
            objPlacesList = new placeListObject();
            objNewPlacesList = new placeListObject();
            objPlacesList.createArray(new JSONArray(strResponseQuery));

            for (int i = 0 ; i < objPlacesList.objListPlaces.size() ; i++)
            {
                boolean blnRemove = false;
                placeListObjectItem objTemp = objPlacesList.objListPlaces.get(i);

                if ((chkUrgencia.isChecked() && objTemp.strAtendimentoUrgencia.equals("Não")) ||
                        (!chkUrgencia.isChecked() && objTemp.strAtendimentoUrgencia.equals("Sim"))
                        ){
                    blnRemove = true;
                }

                if ((chkSus.isChecked() && objTemp.strVinculoSus.equals("Não")) ||
                        (!chkSus.isChecked() && objTemp.strVinculoSus.equals("Sim"))){
                    blnRemove = true;
                }

                if ((chkObstetra.isChecked() && objTemp.strObstetra.equals("Não")) ||
                        (!chkObstetra.isChecked() && objTemp.strObstetra.equals("Sim"))){
                    blnRemove = true;
                }

                if ((chkNeoNatal.isChecked() && objTemp.strNeoNatal.equals("Não")) ||
                        (!chkNeoNatal.isChecked() && objTemp.strNeoNatal.equals("Sim"))){
                    blnRemove = true;
                }

                if (!blnRemove){
                    objNewPlacesList.objListPlaces.add(objTemp);
                }
            }


        }
        catch (JSONException err)
        {
            err.getStackTrace();
        }

        strFinalJSON = "[";

        if (objNewPlacesList != null)
        {
            for (int i = 0; i <objNewPlacesList.objListPlaces.size(); i++)
            {
                if (i > 0)
                    strFinalJSON += ",";
                strFinalJSON += objNewPlacesList.objListPlaces.get(i).convertToString();
            }
        }

        strFinalJSON += "]";

        if (strRequestedBy == "MapsActivity") {
            ((MapsActivity) getActivity()).strLastQueryResponse = strFinalJSON;
            ((MapsActivity) getActivity()).renewLocations(objNewPlacesList);
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
        else {
            ((OnlyListActivity) getActivity()).strLastQueryResponseForList = strFinalJSON;
            ((OnlyListActivity)getActivity()).renewLocations(objNewPlacesList);
        }



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


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



    public String[] BuscaUFs()
    {
        String response;
        List<String> list = null;

        ApiConnector objConector = new ApiConnector("http://www.devmedia.com.br/api/estadoscidades/service/");

        String strUrl = "estados/?formato=json&chave=" + DEVMEDIA_KEY;

        try {
            response = objConector.execute(strUrl).get();
        }
        catch(Exception err)
        {
            response = null;
        }

        try {
            if (response != null) {
                JSONObject objJSON = new JSONObject(response);

                if (objJSON.has("estados")) {

                    if (objJSON.getJSONObject("estados").has("uf")) {

                        JSONArray objArrayUF = objJSON.getJSONObject("estados").getJSONArray("uf");
                        list = new ArrayList<>();
                        list.add("Selecione");
                        for (int i = 0; i < objArrayUF.length(); i++) {
                            list.add(objArrayUF.get(i).toString());
                        }
                    }
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return list.toArray(new String[0]);
    }

    public String[] BuscaCidades(String strUF)
    {
        String response;
        List<String> list = null;

        ApiConnector objConector = new ApiConnector("http://www.devmedia.com.br/api/estadoscidades/service/");

        String strUrl = "cidades/?formato=json&chave=" + DEVMEDIA_KEY + "&uf=" + strUF;

        try {
            response = objConector.execute(strUrl).get();
        }
        catch(Exception err)
        {
            response = null;
        }
            Spinner objCidades = (Spinner) this.getView().findViewById(R.id.list_cidade);

        try {
            if ((response != null) && !response.contains("falha ")) {
                JSONObject objJSON = new JSONObject(response);

                    if (objJSON.has("municipios")) {

                        if (objJSON.getJSONObject("municipios").has("cidade")) {

                            JSONArray objArrayUF = objJSON.getJSONObject("municipios").getJSONArray("cidade");
                            list = new ArrayList<>();
                            list.add("Selecione");
                            for (int i = 0; i < objArrayUF.length(); i++) {
                                list.add(objArrayUF.get(i).toString());
                        }
                    }
                }
                return list.toArray(new String[0]);
            }


        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View v, int i, long l) {
        String[] strListaCidades = null;

        if (arrListaUFs != null)
        {
            Spinner objCidades = (Spinner) this.getView().findViewById(R.id.list_cidade);
            ArrayAdapter<String> adapter;

            if (i > 0) {
                strListaCidades = BuscaCidades(arrListaUFs[i]);

                if (strListaCidades == null)
                {
                    ArrayList<String> ar = new ArrayList<>();
                    ar.add("Selecione");
                    strListaCidades = ar.toArray(new String[0]);
                }
            }
            else
            {
                ArrayList<String> ar = new ArrayList<>();
                ar.add("Selecione");
                strListaCidades = ar.toArray(new String[0]);
            }

            adapter = new ArrayAdapter<>(this.getContext(),android.R.layout.simple_spinner_item,strListaCidades);
            objCidades.setAdapter(adapter);

        }

        isReady = true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(placeListObject item);
    }

}
