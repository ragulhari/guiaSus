package com.ragulhari.guiasus;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import android.support.v4.app.FragmentManager;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ragulhari.guiasus.listObjects.placeListObject;
import com.ragulhari.guiasus.listObjects.placeListObjectItem;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OpcoesFragment.OnListFragmentInteractionListener, PlaceFragment.OnListFragmentInteractionListener,
        PlaceDetailedFragment.OnFragmentInteractionListener{

    private GoogleMap mMap;
    public double latitudeAtual = 12;
    public double longitudeAtual = 40;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    protected FusedLocationProviderApi fusedLocationProviderApi;
    static final int REQUEST_CODE_CONFIGURATION = 9560;
    public String strLastQueryResponse;

    MapFragment mMapFragment;

    public static final int  MY_PERMISSION_ACCESS_COURSE_LOCATION = 901;
    public static final int  INTERVAL_TIME = 30000;
    public static final int  FATEST_INTERVAL_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        strLastQueryResponse = "";

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


        Button btnOptionsButton = (Button)findViewById(R.id.btn_options_map);

        btnOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });

        Button btnGoToListButton = (Button)findViewById(R.id.btn_list);
        btnGoToListButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
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
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CONFIGURATION)
        {

        }
    }

    //Callback da conexão com o Location Services
    @Override
    public void onConnected(Bundle connectionHint) {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_COURSE_LOCATION);
        } else {


            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            //TODO: REmover essa chamada, deixar apenas o IF abaixo
            updateMarker(0, 0);

            if (location == null) {
                fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
            }
            else
            {
                this.updateMarker(location.getLatitude(), location.getLongitude());
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            this.updateMarker(location.getLatitude(), location.getLongitude());
        }
        else
            this.updateMarker(0, 0);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            public void onInfoWindowClick(Marker marker) {
                String strTitulo = marker.getTitle();
                Bundle bundle = new Bundle();
                bundle.putString("titulo", strTitulo);
                bundle.putString("queryResponse", strLastQueryResponse);

                // set Fragmentclass Arguments
                PlaceDetailedFragment fragobj = new PlaceDetailedFragment();
                fragobj.setArguments(bundle);

                final FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.map, fragobj).addToBackStack("detailFromMap")
                        .commit();
            }
        });

    }

    protected void renewLocations(placeListObject objListPlaces){
        pointYourself(this.latitudeAtual, this.longitudeAtual);

        for (int i = 0; i < objListPlaces.objListPlaces.size(); i++)
        {
            placeListObjectItem objTemp = objListPlaces.objListPlaces.get(i);

            MarkerOptions objMarkerOptions = new MarkerOptions();
            objMarkerOptions.title(objTemp.strNomeFantasia);
            objMarkerOptions.snippet(objTemp.strTipoUnidade +" - " + objTemp.strTelefone);
            //objMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            objMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable   .health_map_icon));
            objMarkerOptions.position(new LatLng(Double.parseDouble(objTemp.latitude), Double.parseDouble(objTemp.longitude)));
            mMap.addMarker(objMarkerOptions);

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

    public void getAllPlacesFromLocation() {
        String response;
        placeListObject objPlacesList;

        Locations objLocation = new Locations();
        response = objLocation.getLocationsByCoordinates("http://mobile-aceite.tcu.gov.br/mapa-da-saude/", this.latitudeAtual, this.longitudeAtual, 50);


        if (response != null) {
            try {
                objPlacesList = new placeListObject();
                objPlacesList.createArray(new JSONArray(response));
                renewLocations(objPlacesList);

            } catch (JSONException err){
                err.printStackTrace();
            }
        }

        this.strLastQueryResponse = response;

    }

    public void pointYourself(double latitudeAtual, double longitudeAtual)
    {
        mMap.clear();

        if ((latitudeAtual == 0) && (longitudeAtual == 0)) {
            /*TODO REMOVER - Para efeito de simulação apenas, se o resultado vier como 0, adotando local padrão*/
            latitudeAtual = -23.5865838;
            longitudeAtual = -46.6720745;
        }

        this.latitudeAtual = latitudeAtual;
        this.longitudeAtual = longitudeAtual;

        // Add a marker in Sydney and move the camera
        LatLng  newCoordinates = new LatLng(this.latitudeAtual, this.longitudeAtual);
        mMap.addMarker(new MarkerOptions().position(newCoordinates).title("Sua localização atual"));
        mMap.setMinZoomPreference(16);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(newCoordinates));
    }

    public void updateMarker(double latitudeAtual, double longitudeAtual) {
        pointYourself(latitudeAtual, longitudeAtual);
        getAllPlacesFromLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
//        Intent intent = new Intent(this, Fragment.class);
//        intent.putExtra("denied",true);
//        startActivity(intent);
    }


    @Override
    public void onListFragmentInteraction(placeListObject item) {

    }

    //Fragment PlaceFragment
    @Override
    public void onListFragmentInteraction(placeListObjectItem item) {

        Bundle bundle = new Bundle();
        bundle.putString("selectedItem", item.convertToString());

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
}
