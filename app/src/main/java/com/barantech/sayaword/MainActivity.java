package com.barantech.sayaword;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.barantech.sayaword.util.Log;
import com.barantech.sayaword.web.Constant;
import com.barantech.sayaword.web.ErrorListener;
import com.barantech.sayaword.web.StringRequestWord;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 84;
    TextView mTvResult;
    TextView mTvName;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private EditText mEtWord;
    private EditText mEtID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.

        setContentView(R.layout.activity_main);
        mEtWord = (EditText) findViewById(R.id.et_word);
        mEtID = (EditText) findViewById(R.id.et_id);
        final View mBtnSend = findViewById(R.id.btn_sendword);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        // TODO: 11/15/15 move to proper place
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // Customize sign-in button. The sign-in button can be displayed in
        // multiple sizes and color schemes. It can also be contextually
        // rendered based on the requested scopes. For example. a red button may
        // be displayed when Google+ scopes are requested, but a white button
        // may be displayed when only basic profile is requested. Try adding the
        // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
        // difference.
        SignInButton googleSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);
        googleSignInButton.setScopes(gso.getScopeArray());
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        LoginButton facebookLoginButton = (LoginButton) findViewById(R.id.login_button);
        facebookLoginButton.setReadPermissions("user_friends");
        // If using in a fragment
//        facebookLoginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        callbackManager = CallbackManager.Factory.create();
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("fb.registerCallback", "onSuccess: "+loginResult.toString());

            }

            @Override
            public void onCancel() {
                // App code
                Log.d("fb.registerCallback", "onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d("fb.registerCallback", "onError: "+exception.toString());
            }
        });

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                // App code
                Log.d("fb:", "onCurrentProfileChanged");
                if(currentProfile!=null)
                    mTvName.setText(currentProfile.getName()+" "+currentProfile.getLastName());

            }
        };

        //END of TODO

        mEtWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0)
                    mBtnSend.setVisibility(View.VISIBLE);
                else
                    mBtnSend.setVisibility(View.INVISIBLE);
            }
        });

        try {
            PackageInfo info =     getPackageManager().getPackageInfo("com.barantech.sayaword",     PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sign=Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("MY KEY HASH:", sign);
                //  Toast.makeText(getApplicationContext(),sign,     Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Error MY KEY HASH:", e.toString());

        } catch (NoSuchAlgorithmException e) {
            Log.e("Error MY KEY HASH:", e.toString());

        }
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendWord();
            }
        });

    }

    private void sendWord(){
        mTvResult.setVisibility(View.INVISIBLE);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> param= new HashMap<>(2);
        final String word = mEtWord.getText().toString();
        param.put("word", word);
        param.put("userId",mEtID.getText().toString());

// Request a string response from the provided URL.
        StringRequestWord stringRequestWord = new StringRequestWord(Request.Method.POST,
                Constant.WORDS, param, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("owner")) {
                        mTvResult.setVisibility(View.VISIBLE);
                        String[] args = new String[1];
                        args[0] = word;
                        mTvResult.setText(getString(R.string.word_owner_message, args));
                    } else {
                        if(jsonObject.has("subscribers")){
                            Intent intent = new Intent(MainActivity.this,SubscribersActivity.class);
                            intent.putExtra(SubscribersActivity.EXTRA_SUBSCRIBERS, response);
                            startActivity(intent);
                        }
                        mTvResult.setVisibility(View.VISIBLE);
                        String[] args = new String[1];
                        args[0] = "5";
                        mTvResult.setText(getString(R.string.word_subscribers, args));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new ErrorListener(new ErrorListener.ErrorHandler() {
            @Override
            public void onErrorResponse(String message) {
                Log.e("error", message);
                mEtWord.setError(message);
            }
        }));
// Add the request to the RequestQueue.
        queue.add(stringRequestWord);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("onConnectionFailed", connectionResult.getErrorMessage());
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else
            callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("handleSignInResult", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d("result.isSuccess()", acct.getDisplayName());
            mTvName.setText(mTvName.getText().toString()+acct.getDisplayName());
//            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
//            updateUI(true);
        } else {
            Log.d("result.isSuccess()", "false");
            mTvName.setText(mTvName.getText().toString()+"error");
            // Signed out, show unauthenticated UI.
//            updateUI(false);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }
}
