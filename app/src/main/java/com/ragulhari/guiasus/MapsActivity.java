package com.ragulhari.guiasus;

import android.content.pm.PackageManager;
import android.location.Location;
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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.support.v4.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    public double latitudeAtual = 12;
    public double longitudeAtual = 40;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    protected FusedLocationProviderApi fusedLocationProviderApi;

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

    }

    //Callback da conexão com o Location Services
    @Override
    public void onConnected(Bundle connectionHint) {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_COURSE_LOCATION);
        } else {


            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            //TODO: REmover essa chamada, deixar apenas o IF abaixo
            updateMarker(location);

            if (location == null) {
                fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
            }
            else
            {
                updateMarker(location);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.updateMarker(location);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
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

    private void getAllPlacesFromLocation(double latitude, double longitude) {
        String response;

        ApiConnector objConector = new ApiConnector();
        int intSearchRay = 50;

        String strUrl = "rest/estabelecimentos/latitude/" + Double.toString(latitude) + "/longitude/" + Double.toString(longitude) + "/raio/" + Integer.toString(intSearchRay);

        try {
            response = objConector.execute(strUrl).get();
        }
        catch(Exception err)
        {
            response = null;
        }

        if (response != null) {
            try {
                JSONArray objJSON = new JSONArray(response);
                for (int i=0; i < objJSON.length(); i++)
                {
                    JSONObject objItem = objJSON.getJSONObject(i);
                    MarkerOptions objMarkerOptions = new MarkerOptions();
                    objMarkerOptions.title(objItem.getString("tipoUnidade") + " - " + objItem.getString("nomeFantasia"));
                    objMarkerOptions.position(new LatLng(objItem.getDouble("lat"), objItem.getDouble("long")));
                    mMap.addMarker(objMarkerOptions);
                }

            }
            catch (JSONException err) {
                err.getStackTrace();
            }

        }
    }


    private void updateMarker(Location mLastLocation) {

        if (mLastLocation != null) {
            latitudeAtual = mLastLocation.getLatitude();
            longitudeAtual = mLastLocation.getLongitude();
        }
        else {
            /*TODO REMOVER - Para efeito de simulação apenas, se o resultado vier como 0, adotando local padrão*/
            //latitudeAtual = -22.3086645;
            //longitudeAtual = -49.05382;

            latitudeAtual = -23.5865838;
            longitudeAtual = -46.6720745;
        }


        // Add a marker in Sydney and move the camera
        LatLng  newCoordinates = new LatLng(latitudeAtual, longitudeAtual);
        mMap.addMarker(new MarkerOptions().position(newCoordinates).title("Sua localização atual"));
        mMap.setMinZoomPreference(14);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(newCoordinates));

        getAllPlacesFromLocation(latitudeAtual, longitudeAtual);

    }


}
