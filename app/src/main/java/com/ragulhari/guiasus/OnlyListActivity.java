package com.ragulhari.guiasus;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ragulhari.guiasus.listObjects.placeListObject;
import com.ragulhari.guiasus.listObjects.placeListObjectItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OnlyListActivity extends FragmentActivity implements PlaceFragment.OnListFragmentInteractionListener, PlaceDetailedFragment.OnFragmentInteractionListener {

    public String strLastQueryResponseForList = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    protected void renewLocations(placeListObject objListPlaces) {
        Bundle bundle = new Bundle();
        bundle.putString("queryResponse", strLastQueryResponseForList);

        // set Fragmentclass Arguments
        PlaceFragment fragobj = new PlaceFragment();
        fragobj.setArguments(bundle);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.list, fragobj).addToBackStack("list")
                .commit();

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "OnlyList Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.ragulhari.guiasus/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "OnlyList Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.ragulhari.guiasus/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    //PlaceFragment
    @Override
    public void onListFragmentInteraction(placeListObjectItem item) {
        Bundle bundle = new Bundle();
        bundle.putString("selectedItem", item.convertToString());

        // set Fragmentclass Arguments
        PlaceDetailedFragment fragobj = new PlaceDetailedFragment();
        fragobj.setArguments(bundle);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.list, fragobj).addToBackStack("detail")
                .commit();

    }

    //PlaceDetailedFragment
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
