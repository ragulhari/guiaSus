package com.ragulhari.guiaSus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import android.support.v4.app.FragmentManager;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ragulhari.guiaSus.listObjects.placeListObject;
import com.ragulhari.guiaSus.listObjects.placeListObjectItem;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Classe principal do app, que apreseta o mapa com as localizações de estabelecimentos.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, PlaceFragment.OnListFragmentInteractionListener,
        PlaceDetailedFragment.OnFragmentInteractionListener {

    private GoogleMap mMap;
    private double latitudeAtual = 12;
    private double longitudeAtual = 40;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    private FusedLocationProviderApi fusedLocationProviderApi;
    public String strLastQueryResponse;

    private String userEmail = "";
    private String googleTokenId = "";

    private static final int MY_PERMISSION_ACCESS_COURSE_LOCATION = 901;
    private static final int INTERVAL_TIME = 30000;
    private static final int FATEST_INTERVAL_TIME = 2000;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dialog = new ProgressDialog(MapsActivity.this);
        dialog.setMessage("Obtendo locais no servidor... Aguarde");
        dialog.setIndeterminate(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        //Configuração da toolbar da activity
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        //Os dados de e-mail e token ID do Google são trafegados como parâmetros pelas Intents para uso em várias ocasiões
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            this.userEmail = extras.getString("email");
            this.googleTokenId = extras.getString("googleTokenId");
        }

        strLastQueryResponse = "";

        //Configuração do objeto de localização
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(INTERVAL_TIME);
        locationRequest.setFastestInterval(FATEST_INTERVAL_TIME);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            fusedLocationProviderApi = LocationServices.FusedLocationApi;
        }


        Button btnOptionsButton = (Button) findViewById(R.id.btn_options_map);
        btnOptionsButton.setVisibility(View.GONE);

        Button btnGoToListButton = (Button) findViewById(R.id.btn_list);
        btnGoToListButton.setVisibility(View.GONE);
    }

    /**
     * Inicia a activity de filtros de busca
     */
    private void startSearch(){

        Bundle bundle = new Bundle();
        bundle.putString("requestedBy", "MapsActivity");
        bundle.putDouble("latitudeAtual", latitudeAtual);
        bundle.putDouble("longitudeAtual", longitudeAtual);

        OpcoesFragment fragobj = new OpcoesFragment();
        fragobj.setArguments(bundle);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.map, fragobj).addToBackStack("map")
                .commit();
    }


    /**
     * Inicia a activity de lista de estabelecimentos
     */
    private void startList() {
        Bundle bundle = new Bundle();
        bundle.putString("queryResponse", strLastQueryResponse);

        // set Fragmentclass Arguments
        PlaceFragment fragobj = new PlaceFragment();
        fragobj.setArguments(bundle);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.map, fragobj).addToBackStack("list")
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * Atualmente são duas opções no menu da ActionBar:
         * Abrir os filtros de busca (search_list)
         * Inicia a visão de lista dos estabelecimentos (action_list)
         */

        switch (item.getItemId()) {
            case R.id.search_list:
                startSearch();
                return true;

            case R.id.action_list:
                startList();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    //Callback da conexão com o Location Services
    @Override
    public void onConnected(Bundle connectionHint) {

        /*
        Verifica se o usuário tem permissão para visualização de mapa. Caso tenha, inicia a obtenção da localização no GPS
        Se não tem permissão, solicita ao usuário permissão para acesso ao GPS
         */
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_COURSE_LOCATION);
        } else {

            //Obtém a localização atual do usuário e inicia a busca da localização
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location == null)
                fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
            else
                this.updateMarker(location.getLatitude(), location.getLongitude());
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        //Caso o GPS detecte mudança de localização, atualiza o marcador do usuário
        if (location != null) {
            this.updateMarker(location.getLatitude(), location.getLongitude());
        } else
            this.updateMarker(0, 0);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //Configura o marcadores após o mapa estar pronto
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            public void onInfoWindowClick(Marker marker) {
                String strTitulo = marker.getTitle();
                Bundle bundle = new Bundle();
                bundle.putString("titulo", strTitulo);
                bundle.putString("queryResponse", strLastQueryResponse);
                bundle.putString("userEmail", userEmail);
                bundle.putString("googleTokenId", googleTokenId);

                PlaceDetailedFragment fragobj = new PlaceDetailedFragment();
                fragobj.setArguments(bundle);

                final FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.map, fragobj).addToBackStack("detailFromMap")
                        .commit();
            }
        });

    }

    /**
     * Método que monta a lista de marcadores para o locais obtidos
     * @param objListPlaces Lista de lugares
     */
    void renewLocations(placeListObject objListPlaces) {
        pointYourself(this.latitudeAtual, this.longitudeAtual);
        boolean blnHiddenLocals = false;

        for (int i = 0; i < objListPlaces.objListPlaces.size(); i++) {
            placeListObjectItem objTemp = objListPlaces.objListPlaces.get(i);

            MarkerOptions objMarkerOptions = new MarkerOptions();
            objMarkerOptions.title(objTemp.strNomeFantasia);
            objMarkerOptions.snippet(objTemp.strTipoUnidade + '\n' + objTemp.strTelefone);
            objMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.health_map_icon));
            if ((objTemp.latitude != null) && (objTemp.longitude != null)) {

                objMarkerOptions.position(new LatLng(Double.parseDouble(objTemp.latitude), Double.parseDouble(objTemp.longitude)));
                mMap.addMarker(objMarkerOptions);
                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                    @Override
                    public View getInfoWindow(Marker arg0) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {

                        LinearLayout info = new LinearLayout(getBaseContext());
                        info.setOrientation(LinearLayout.VERTICAL);

                        TextView title = new TextView(getBaseContext());
                        title.setTextColor(Color.RED);
                        title.setGravity(Gravity.START);
                        title.setTypeface(null, Typeface.BOLD);
                        title.setText(marker.getTitle());

                        TextView snippet = new TextView(getBaseContext());
                        snippet.setTextColor(Color.BLACK);
                        snippet.setText(marker.getSnippet());

                        info.addView(title);
                        info.addView(snippet);
                        info.setDividerPadding(4);

                        return info;
                    }
                });
            } else {
                blnHiddenLocals = true;
            }
        }

        dialog.hide();

        if (blnHiddenLocals){
            Toast toast = Toast.makeText(this, "Existem locais não identificados no mapa, veja a lista",Toast.LENGTH_SHORT);
            toast.show();

        }

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onResume() {
        mGoogleApiClient.connect();
        super.onResume();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult obj) {
    }

    private void getAllPlacesFromLocation() {
        String response;
        placeListObject objPlacesList;

        Locations objLocation = new Locations();
        response = objLocation.getLocationsByCoordinates("http://mobile-aceite.tcu.gov.br/mapa-da-saude/", this.latitudeAtual, this.longitudeAtual, 100, "Todos os estabelecimentos");


        if (response != null) {
            try {
                objPlacesList = new placeListObject();
                objPlacesList.createArray(new JSONArray(response));
                renewLocations(objPlacesList);

            } catch (JSONException err) {
                err.printStackTrace();
            }
        }

        this.strLastQueryResponse = response;

    }

    //Método para inserir o marcador da posição atual do usuário
    private void pointYourself(double latitudeAtual, double longitudeAtual) {
        mMap.clear();

        this.latitudeAtual = latitudeAtual;
        this.longitudeAtual = longitudeAtual;

        LatLng newCoordinates = new LatLng(this.latitudeAtual, this.longitudeAtual);
        mMap.addMarker(new MarkerOptions().position(newCoordinates).title("Sua localização atual"));
        mMap.setMinZoomPreference(14);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(newCoordinates));
    }

    private void updateMarker(double latitudeAtual, double longitudeAtual) {
        pointYourself(latitudeAtual, longitudeAtual);
        getAllPlacesFromLocation();
    }

    /**
     * Método que é invocado caso app solicite permissão de uso ao mapa. Analisa o resultado e, caso rejeitado, abre a activity de listagem
     * @param requestCode
     * @param permissions
     * @param grantResults
     */

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (requestCode == MY_PERMISSION_ACCESS_COURSE_LOCATION) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                if (location == null)
                    fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
                else
                    this.updateMarker(location.getLatitude(), location.getLongitude());
            }
            else
            {
                Intent intent = new Intent(this, OnlyListActivity.class);
                startActivity(intent);
            }
        }
    }


    //Fragment PlaceFragment
    @Override
    public void onListFragmentInteraction(placeListObjectItem item) {

        Bundle bundle = new Bundle();
        bundle.putString("selectedItem", item.convertToString());
        bundle.putString("userEmail", userEmail);
        bundle.putString("googleTokenId", googleTokenId);

        // set Fragmentclass Arguments
        PlaceDetailedFragment fragobj = new PlaceDetailedFragment();
        fragobj.setArguments(bundle);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.map, fragobj).addToBackStack("detail")
                .commit();

    }

    //Fragment PlaceDetailFragment
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction() {

    }
}
