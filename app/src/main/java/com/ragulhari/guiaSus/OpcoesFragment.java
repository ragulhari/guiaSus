package com.ragulhari.guiaSus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Spinner;
import com.ragulhari.guiaSus.listObjects.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OpcoesFragment extends Fragment implements OnItemSelectedListener {

    private static final String DEVMEDIA_KEY = "92I398TG6";
    private double dLatitudeAtual = 0;
    private double dLongitudeAtual = 0;
    private ProgressBar pLoading;

    private String[] arrListaUFs = null;

    private String strRequestedBy = "";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OpcoesFragment() {
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


        if (!strRequestedBy.equals("MapsActivity")) {
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

        pLoading = (ProgressBar) view.findViewById(R.id.progressOptions);

        Spinner objSpecialities = (Spinner) view.findViewById(R.id.list_especialidades);
        ArrayAdapter<String> adapterSpecialities = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, configureSpecialities());
        objSpecialities.setAdapter(adapterSpecialities);

        Button btnSubmit = (Button) view.findViewById(R.id.btnSubmit_Search);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pLoading.setVisibility(View.VISIBLE);
                findLocations();
            }
        });

        return view;
    }


    private String[] configureSpecialities(){
        List<String> arrSpecialities = new ArrayList<>();
        arrSpecialities.add("Todos os estabelecimentos");
        arrSpecialities.add("HOSPITAL");
        arrSpecialities.add("POSTO DE SAÚDE");
        arrSpecialities.add("URGÊNCIA");
        arrSpecialities.add("SAMU");
        arrSpecialities.add("FARMÁCIA");
        arrSpecialities.add("CLÍNICA");
        arrSpecialities.add("CONSULTÓRIO");
        arrSpecialities.add("LABORATÓRIO");
        arrSpecialities.add("APOIO À SAÚDE");
        arrSpecialities.add("ATENÇÃO ESPECÍFICA");
        arrSpecialities.add("UNIDADE ADMINISTRATIVA");
        arrSpecialities.add("ATENDIMENTO DOMICILIAR");

        return arrSpecialities.toArray(new String[0]);
    }


    private void findLocations()
    {

        Locations objLocation = new Locations();

        Spinner objUF = (Spinner) this.getView().findViewById(R.id.list_uf);
        Spinner objCidades = (Spinner) this.getView().findViewById(R.id.list_cidade);
        CheckBox chkSus = (CheckBox) this.getView().findViewById(R.id.checkBoxSus);
        CheckBox chkUrgencia = (CheckBox) this.getView().findViewById(R.id.checkBoxUrgencia);
        CheckBox chkObstetra = (CheckBox) this.getView().findViewById(R.id.checkBoxObstetra);
        CheckBox chkNeoNatal = (CheckBox) this.getView().findViewById(R.id.checkBoxNeoNatal);
        Spinner objEspecialidades = (Spinner) this.getView().findViewById(R.id.list_especialidades);

        String strResponseQuery;
        if (strRequestedBy.equals("MapsActivity"))
            strResponseQuery = objLocation.getLocationsByCoordinates("http://mobile-aceite.tcu.gov.br/mapa-da-saude/", dLatitudeAtual, dLongitudeAtual, 50, objEspecialidades.getSelectedItem().toString());
        else
            strResponseQuery = objLocation.getLocationsByFilters("http://mobile-aceite.tcu.gov.br/mapa-da-saude/", objUF.getSelectedItem().toString(), objCidades.getSelectedItem().toString(), objEspecialidades.getSelectedItem().toString());

        String strFinalJSON;
        placeListObject objNewPlacesList = new placeListObject();
        placeListObject objPlacesList = new placeListObject();

        try {
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

        for (int i = 0; i <objNewPlacesList.objListPlaces.size(); i++)
        {
            if (i > 0)
                strFinalJSON += ",";
            strFinalJSON += objNewPlacesList.objListPlaces.get(i).convertToString();
        }

        strFinalJSON += "]";

        if (strRequestedBy.equals("MapsActivity")) {
            ((MapsActivity) getActivity()).strLastQueryResponse = strFinalJSON;
            ((MapsActivity) getActivity()).renewLocations(objNewPlacesList);
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
        else {
            ((OnlyListActivity) getActivity()).strLastQueryResponseForList = strFinalJSON;
            ((OnlyListActivity)getActivity()).renewLocations();
        }

//        pLoading.setVisibility(View.VISIBLE);

    }


    @Override
    public void onDetach() {
        super.onDetach();
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



    private String[] BuscaUFs()
    {
        String response;
        List<String> list = null;

        String strUrl = "http://www.devmedia.com.br/api/estadoscidades/service/estados/?formato=json&chave=" + DEVMEDIA_KEY;

        APIConnector objConector = new APIConnector(strUrl, "GET");


        try {
            response = objConector.execute().get();
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

        return (list != null ? list.toArray(new String[0]) : null);
    }

    private String[] BuscaCidades(String strUF)
    {
        String response;
        List<String> list = null;

        String strUrl = "http://www.devmedia.com.br/api/estadoscidades/service/cidades/?formato=json&chave=" + DEVMEDIA_KEY + "&uf=" + strUF;

        APIConnector objConector = new APIConnector(strUrl, "GET");

        try {
            response = objConector.execute().get();
        }
        catch(Exception err)
        {
            response = null;
        }

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
                return (list != null ? list.toArray(new String[0]) : null);
            }


        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View v, int i, long l) {
        String[] strListaCidades;

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
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
