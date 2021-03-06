package com.springer.patryk.geo_photo.screens.authentication.login;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.springer.patryk.geo_photo.R;

/**
 * Created by Patryk on 2017-03-11.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private static final String TAG = "RegisterPresenter";

    private LoginContract.View mAuthenticationView;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public LoginPresenter(@NonNull LoginContract.View authenticationView) {
        mAuthenticationView = authenticationView;
        mAuthenticationView.setPresenter(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                mAuthenticationView.showMainPage();
            }
        };

    }

    @Override
    public void subscribe() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void unsubscribe() {
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void checkCredentials(String email, String password) {
        boolean validation = true;
        if (email.isEmpty()) {
            validation = false;
            mAuthenticationView.setEmailError(R.string.required_field);
        }
        if (password.isEmpty()) {
            validation = false;
            mAuthenticationView.setPasswordError(R.string.required_field);
        }
        if (validation) {
            loginUser(email, password);
        }
    }

    public void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful())
                        mAuthenticationView.showAuthenticationError();
                });
    }
}
