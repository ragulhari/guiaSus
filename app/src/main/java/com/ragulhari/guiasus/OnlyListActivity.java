package com.ragulhari.guiasus;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OnlyListActivity extends FragmentActivity {

    protected String strLastQueryResponseForList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_list);

        Bundle bundle = new Bundle();
        bundle.putString("requestedBy", "OnlyListActivity");

        OpcoesFragment fragobj = new OpcoesFragment();
        fragobj.setArguments(bundle);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.list, fragobj).addToBackStack("list")
                .commit();



//        strLastQueryResponseForList = executeQuery();
//
//        Bundle bundle = new Bundle();
//        bundle.putString("queryResponse", strLastQueryResponseForList);
//
//        // set Fragmentclass Arguments
//        PlaceFragment fragobj = new PlaceFragment();
//        final FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.list, fragobj).commit();

    }

    protected void executeQuery()
    {
//        String response;
//
//        ApiConnector objConector = new ApiConnector();
//        int intSearchRay = 50;
//
//        String strUrl = "rest/estabelecimentos/latitude/" + Double.toString(latitude) + "/longitude/" + Double.toString(longitude) + "/raio/" + Integer.toString(intSearchRay);
//
//        try {
//            response = objConector.execute(strUrl).get();
//        }
//        catch(Exception err)
//        {
//            response = null;
//        }
//
//        if (response != null) {
//            try {
//                JSONArray objJSON = new JSONArray(response);
//                for (int i=0; i < objJSON.length(); i++)
//                {
//                    String tempTelefone;
//                    String tempNomeFantasia;
//                    String tempTipoUnidade;
//
//                    JSONObject objItem = objJSON.getJSONObject(i);
//                    MarkerOptions objMarkerOptions = new MarkerOptions();
//
//                    if (objItem.has("nomeFantasia"))
//                        tempNomeFantasia = objItem.getString("nomeFantasia");
//                    else
//                        tempNomeFantasia = "Sem nome cadastrado";
//
//                    if (objItem.has("tipoUnidade"))
//                        tempTipoUnidade = objItem.getString("tipoUnidade");
//                    else
//                        tempTipoUnidade = "N/A";
//
//                    if (objItem.has("telefone"))
//                        tempTelefone = objItem.getString("telefone");
//                    else
//                        tempTelefone = "Sem telefone";
//
//                    objMarkerOptions.title(tempNomeFantasia);
//                    objMarkerOptions.snippet(tempTipoUnidade +" - " + tempTelefone);
//                    //objMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                    objMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.health_map_icon));
//                    objMarkerOptions.position(new LatLng(objItem.getDouble("lat"), objItem.getDouble("long")));
//                    mMap.addMarker(objMarkerOptions);
//                }
//
//                this.strLastQueryResponse = response;
//
//            }
//            catch (JSONException err) {
//                err.getStackTrace();
//            }
//        }
    }

}
