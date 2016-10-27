package com.ragulhari.guiaSus;

import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ragulhari.guiaSus.listObjects.placeListObjectItem;

public class OnlyListActivity extends AppCompatActivity implements PlaceFragment.OnListFragmentInteractionListener, PlaceDetailedFragment.OnFragmentInteractionListener {

    public String strLastQueryResponseForList = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private String userEmail = "";
    private String googleTokenId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean blnStartFromList = false;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_list);



        Bundle bundle = new Bundle();
        bundle.putString("requestedBy", "OnlyListActivity");

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            userEmail = extras.getString("email");
            googleTokenId = extras.getString("googleTokenId");
            strLastQueryResponseForList = extras.getString("queryResponse");
            if ((strLastQueryResponseForList != null) && (!strLastQueryResponseForList.equals(""))) {
                blnStartFromList = true;
            }

        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_list);
        myToolbar.setTitle("Busca Saúde");
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        if (!blnStartFromList) {

            OpcoesFragment fragobj = new OpcoesFragment();
            fragobj.setArguments(bundle);

            final FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.list, fragobj).addToBackStack("list")
                    .commit();
        }
        else {
            PlaceFragment fragobj = new PlaceFragment();

            fragobj.setArguments(bundle);
            bundle.putString("queryResponse", strLastQueryResponseForList);
            final FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.list, fragobj).addToBackStack("list")
                    .commit();

        }


    }

    void renewLocations() {
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

        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Visão em lista",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.ragulhari.guiaSus/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Visão em lista",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.ragulhari.guiaSus/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    //PlaceFragment
    @Override
    public void onListFragmentInteraction(placeListObjectItem item) {
        Bundle bundle = new Bundle();
        bundle.putString("selectedItem", item.convertToString());
        bundle.putString("userEmail", this.userEmail);
        bundle.putString("googleTokenId", this.googleTokenId);

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

    @Override
    public void onFragmentInteraction() {

    }
}
