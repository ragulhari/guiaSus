
package com.ragulhari.guiaSus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Classe que representa a tela de login do aplicativo
 * Neste momento o login é feito apenas com o Google Accounts
 */
public class LoginActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, AboutFragment.OnFragmentInteractionListener {

    //Signin constant to check the activity result
    private final int RC_SIGN_IN = 9001;
    private final static String GOOGLE_ID_TOKEN = "916970814029-k0t57egr31007radis7ilj8jroknatij.apps.googleusercontent.com";


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(GOOGLE_ID_TOKEN)
                .build();

        /*Controle que implementa o link "Sobre" */
        TextView objAbout = (TextView)findViewById(R.id.about_text);
        objAbout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutFragment fragobj = new AboutFragment();

                final FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.loginFragment, fragobj).addToBackStack("about")
                        .commit();

            }
        });

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton mSignInButton = (SignInButton)findViewById(R.id.sign_in_button);
        mSignInButton.setSize(SignInButton.SIZE_WIDE);
        mSignInButton.setScopes(gso.getScopeArray());
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    // ...
                }
            }


        });

    }

    /**
     * Realiza o sign-in do usuário caso ele selecone a opção de login (neste momento apenas pelo Google)
     */
    private void signIn() {

        ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("Conectando aos servidores, aguarde...");
        dialog.setIndeterminate(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Callback de retorno do Login no Google
     * @param requestCode Código do request
     * @param resultCode Código do retorno
     * @param data Bundle de dados, reservado pelo parent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    /**
     * Método criado para realizar o cadastro do usuário no appcivico se o mesmo ainda não for registrado.
     * @param tokenId ID Google do usuário
     * @param email e-mail do usuário
     * @param fullName Nome completo do usuário
     * @param userName Nome do usuário
     */
    private void addUserinAppCivico(String tokenId, String email, String fullName, String userName) {
        APIConnector objConector = new APIConnector("http://mobile-aceite.tcu.gov.br:80/appCivicoRS/rest/pessoas", "POST");

        String strJSON = "{ \"email\": \"" + email + "\", \"nomeCompleto\": \"" + fullName +
                "\", \"tokenGoogle\": \"" + tokenId + "\", \"nomeUsuario\": \"" + userName + "\"}";

        objConector.InsertJSON(strJSON);
        try {
            objConector.execute();
        }
        catch(Exception err)
        {
            err.printStackTrace();
        }


    }

    /**
     * Método para verificar se o usuário está cadastrado na plataforma Appcivico
     * @param email e-mail do usuário a ser verificado
     * @return booleano que indica se o usuário está cadastrado no appcivico
     */
    private boolean verifyUser(String email) {
        String response;

        APIConnector objConector = new APIConnector("http://mobile-aceite.tcu.gov.br:80/appCivicoRS/rest/pessoas", "GET");
        objConector.addHeader("email", email);

        try {
            response = objConector.execute().get();
        }
        catch(Exception err)
        {
            response = null;
        }

        return !((response == null) || (objConector.responseCode == 204));
    }

    /**
     * Verifica se o resultado do login foi OK e encaminha para a Activity de Mapas
     * @param result Objeto que contém dados do usuário logado.
     */
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("SignInActivity", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

            GoogleSignInAccount acct = result.getSignInAccount();

            try
            {
                //Verifica se o usuário já está cadastrado no appcivico. Caso contrário, realiza o cadastro.
                if ((acct !=null) && !verifyUser(acct.getEmail()))
                {
                    //Realiza o cadastro do usuário no metamodelo do appcivico caso não exista o cadastro do usuário
                    addUserinAppCivico(acct.getId(), acct.getEmail(), acct.getGivenName() + " " + acct.getFamilyName(),acct.getEmail());
                }

            }
            catch (Exception err)
            {
                err.printStackTrace();
            }

            //Inicia a Activity de mapas
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("email",(acct != null ? acct.getEmail() : ""));
            intent.putExtra("googleTokenId", (acct != null ? acct.getId() : ""));
            startActivity(intent);

            findViewById(R.id.progressLogin).setVisibility(View.GONE);


        } else {
            // Signed out, show unauthenticated UI.
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();

    }

    @Override
    public void onStop() {
        super.onStop();

        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}

