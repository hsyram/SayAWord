package com.barantech.sayaword;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.barantech.sayaword.util.Log;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment  implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 84;
    TextView mTvName;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;

    Context mContext;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getContext());
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
        // Inflate the layout for this fragment
        mContext = getContext();
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mTvName = (TextView) view.findViewById(R.id.tv_name);
        // TODO: 11/15/15 move to proper place
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.registerConnectionFailedListener(this);
        if(!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
        // Customize sign-in button. The sign-in button can be displayed in
        // multiple sizes and color schemes. It can also be contextually
        // rendered based on the requested scopes. For example. a red button may
        // be displayed when Google+ scopes are requested, but a white button
        // may be displayed when only basic profile is requested. Try adding the
        // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
        // difference.
        SignInButton googleSignInButton = (SignInButton) view.findViewById(R.id.sign_in_button);
        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);
        googleSignInButton.setScopes(gso.getScopeArray());
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        //====== facebook =======
        LoginButton facebookLoginButton = (LoginButton) view.findViewById(R.id.login_button);
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
                Log.d("fb.registerCallback", "onSuccess: " + loginResult.toString());

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
                if(currentProfile!=null){
                    mTvName.setText(currentProfile.getName() + " " + currentProfile.getLastName());
                    Log.d("fb:", "currentProfile.getFirstName():" + currentProfile.getFirstName());
                    Log.d("fb:", "currentProfile.getLastName():" + currentProfile.getLastName());
                    Log.d("fb:", "currentProfile.getId():" + currentProfile.getId());
                    Log.d("fb:", "currentProfile.getName():" + currentProfile.getName());
                    Log.d("fb:", "currentProfile.getMiddleName():" + currentProfile.getMiddleName());
                    Log.d("fb:", "currentProfile.getLinkUri():" + currentProfile.getLinkUri());
                    Log.d("fb:", "currentProfile.getProfilePictureUri():"+currentProfile.getProfilePictureUri(100,100));

                }

            }
        };

        // TODO: 12/1/15 remove this
/*        try {
            PackageInfo info = getContext().getPackageManager().
                    getPackageInfo("com.barantech.sayaword", PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sign= Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("MY KEY HASH:", sign);
                //  Toast.makeText(getApplicationContext(),sign,     Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Error MY KEY HASH:", e.toString());

        } catch (NoSuchAlgorithmException e) {
            Log.e("Error MY KEY HASH:", e.toString());

        }*/
        return view;
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
            Log.d("result.isSuccess()", acct.getId());
            Log.d("result.isSuccess()", acct.getEmail());
            if(acct.getIdToken() != null)
                Log.d("result.isSuccess()", acct.getIdToken());
            Log.d("result.isSuccess()", acct.getPhotoUrl().toString());
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
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

}
