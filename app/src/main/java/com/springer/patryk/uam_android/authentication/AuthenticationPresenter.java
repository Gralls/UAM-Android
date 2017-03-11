package com.springer.patryk.uam_android.authentication;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Patryk on 2017-03-11.
 */

public class AuthenticationPresenter implements AuthenticationContract.Presenter {

    private static final String TAG = "AuthenticationPresenter";

    private AuthenticationContract.View mAuthenticationView;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public AuthenticationPresenter(@NonNull AuthenticationContract.View authenticationView) {
        mAuthenticationView = authenticationView;
        mAuthenticationView.setPresenter(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                Log.d(TAG, "onAuthStateChanged:signed_out");
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
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "signInWithEmail:failed", task.getException());
                        mAuthenticationView.showAuthenticationError();
                    } else {
                        mAuthenticationView.showMainPage();
                    }
                });
    }
}
